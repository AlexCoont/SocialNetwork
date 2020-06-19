package project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * В данном классе я устанавливаю наш токен фильтр в Security и данный фильтр будет вызыватся перед основным
 * фильтром*/

public class TokenConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private TokenProvider tokenProvider;

    @Autowired
    public TokenConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        TokenFilter tokenFilter = new TokenFilter(tokenProvider);
        //Установка нашего фильтра до использования основного
        httpSecurity.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }




}
