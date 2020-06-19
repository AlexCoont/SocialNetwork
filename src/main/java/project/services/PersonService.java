package project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.dto.requestDto.LoginRequestDto;
import project.dto.requestDto.RegistrationRequestDto;
import project.dto.requestDto.UpdatePersonDto;
import project.dto.responseDto.PersonDtoWithToken;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.handlerExceptions.EntityAlreadyExistException;
import project.handlerExceptions.UnauthorizationException401;
import project.models.Person;
import project.models.Role;
import project.models.enums.MessagesPermission;
import project.models.util.entity.ImagePath;
import project.repositories.PersonRepository;
import project.security.TokenProvider;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private ImagePath imagePath;

    //    @PostConstruct
//    public void init() {
//        Person person = new Person();
//        person.setFirstName("Ilya");
//        person.setEmail("il@mail.ru");
//
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String password = passwordEncoder.encode("qweasdzxc");
//        person.setPassword(password);
//        personRepository.save(person);
//
//    }

    public Person add(RegistrationRequestDto dto, Role role) throws EntityAlreadyExistException {
        if (findPersonByEmail(dto.getEmail()).isPresent())
            throw new EntityAlreadyExistException("");

        Person person = new Person();
        person.setEmail(dto.getEmail());
        person.setPassword(encoder.encode(dto.getPasswd1()));
        person.setPhoto(imagePath.getDefaultImagePath());
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setRegDate(new Date());
        person.setRoles(Collections.singleton(role));
        return personRepository.save(person);
    }

    public ResponseDto<PersonDtoWithToken> login(LoginRequestDto dto){
        String email = dto.getEmail();
        Person person = personRepository.findPersonByEmail(email).orElseThrow(BadRequestException400::new);

        if (!encoder.matches(dto.getPassword(), person.getPassword())){
            throw new BadRequestException400();
        }

        person.setLastOnlineTime(new Date());
        saveLastOnlineTime(person);

        String token = tokenProvider.createToken(email);//необходимо оставить
        PersonDtoWithToken personDto = new PersonDtoWithToken();
        personDto.setId(person.getId());
        personDto.setFirstName(person.getFirstName());
        personDto.setLastName(person.getLastName());
        personDto.setRegDate(person.getRegDate());
        personDto.setBirthDate(person.getBirthDate());
        personDto.setEmail(person.getEmail());
        personDto.setPhone(person.getPhone());
        personDto.setPhoto(person.getPhoto());
        personDto.setAbout(person.getAbout());
        personDto.setCity(person.getCity());
        personDto.setCountry(person.getCountry());
        personDto.setMessagesPermission(person.getMessagesPermission());
        personDto.setLastOnlineTime(person.getLastOnlineTime());
        personDto.setBlocked(person.isBlocked());
        personDto.setToken(token);
        return new ResponseDto<>(personDto);
    }

    public void updatePassword(Person person, String password) {
        password = encoder.encode(password);
        person.setPassword(password);
        personRepository.save(person);
    }

    public Optional<Person> findPersonByEmail(String email) {
        return personRepository.findPersonByEmail(email);
    }

    public Optional<Person> findPersonById(Integer id) {
        return personRepository.findById(id);
    }

    public void blockPersonById(Integer id, Boolean block, Integer blockerId) {
        Person person = findPersonById(id).orElseThrow(BadRequestException400::new);
        person.setBlockedBy(block ? blockerId : null);
        personRepository.save(person);
    }

    public void deletePersonByEmail(String email){
        Optional<Person> person = findPersonByEmail(email);
        if (person.isPresent())
            personRepository.deleteByEmail(email);
    }

    public Person getPersonByToken(ServletRequest servletRequest){
        String token = tokenProvider.resolveToken((HttpServletRequest) servletRequest);
        String email = tokenProvider.getUserEmail(token);
        return findPersonByEmail(email).orElse(null);
    }

    public Person editBody(UpdatePersonDto dto, HttpServletRequest request) throws UnauthorizationException401
    {
        Person person = getPersonByToken(request);
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setBirthDate(dto.getBirthDate());
        person.setPhone(dto.getPhone());
        person.setAbout(dto.getAbout());
        person.setCity(dto.getCity());
        person.setCountry(dto.getCountry());
        person.setMessagesPermission(MessagesPermission.ALL);
        // person.setMessagesPermission(dto.getMessagePermission());
        personRepository.save(person);
        return person;
    }

    public void saveLastOnlineTime(Person person) {
        personRepository.save(person);
    }

    public void updatePhoto(Person person, String url) {
        person.setPhoto(url);
        personRepository.save(person);
    }

    public Page<Person> search(Person person,
                               String firstName,
                               String lastName,
                               Integer ageFrom,
                               Integer ageTo,
                               String country,
                               String city,
                               Integer offset,
                               Integer itemPerPage) {
        Pageable pageable = PageRequest.of(offset, itemPerPage);
        return personRepository.search(person.getId(), firstName, lastName, ageFrom, ageTo, country, city, pageable);
    }
}