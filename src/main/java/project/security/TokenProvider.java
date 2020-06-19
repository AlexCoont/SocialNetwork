package project.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import project.models.Role;
import project.repositories.PersonRepository;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * Данный класс работает с нашим токеном*/
@Slf4j
@Component
public class TokenProvider
{
        @Autowired
    PersonRepository personRepository;

    @Value("${jwt.token.secret}")
    private String secret; // секретное слово из application.yml

    @Value("${jwt.token.expired}")
    private long validityMillisecond;

    private UserDetailsService userDetailsService;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public TokenProvider(UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @PostConstruct
    protected void init(){
        secret = Base64.getEncoder().encodeToString(secret.getBytes()); // шифрование токена перед запуском класса
    }

    public String createToken(String email){ //создание токена

        Claims claims = Jwts.claims().setSubject(email); //создаем клайм
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityMillisecond);
        return Jwts.builder()       // создаем токен
                .setClaims(claims)  // установка клайм
                .setIssuedAt(now)
                .setExpiration(validity)// установка даты создания
                .signWith(SignatureAlgorithm.HS256, secret) //хэширование секретного кода
                .compact();
    }

    /** Получение аутентификации по токену*/
    public Authentication getAuthentication(String token){
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /** Получение email по токену*/
    public String getUserEmail(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    /** Получение токена из header запроса*/
    public String resolveToken(HttpServletRequest request){
        return request.getHeader("Authorization");
    }

    /** Получение емейла из запроса*/
    public String getEmailByRequest(HttpServletRequest request){
        return getUserEmail(resolveToken(request));
    }

    /** Получение Person по запросу*/
//    public Person getPersonByRequest(HttpServletRequest request) throws UnauthorizationException401
//    {
//        Optional<Person> person = personRepository.findByEmail(getEmailByRequest(request));
//        if(person.isPresent()){
//            return person.get();
//        }
//        throw new UnauthorizationException401();
//    }

    /** Валидация токена*/
    public boolean validateToken(String token) throws AccessDeniedException {

         try {
             Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
             if(claims.getBody().getExpiration().before(new Date())){
                 return false;
             }
             return true;
         } catch (JwtException | IllegalArgumentException e){
             throw new AccessDeniedException("Token was expired");
         }
    }

    public List<String> getRoleName(List<Role> personRole){
        List<String> result = new ArrayList<>();

        personRole.forEach(role -> result.add(role.getName().name()));
        return result;
    }
}
