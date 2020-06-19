package project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;
import project.handlerExceptions.CustomAccessDeniedHandler;

/**
 * Основные конфигурации security находятся здесь*/

@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;

    private TokenProvider tokenProvider;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, TokenProvider tokenProvider) {
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    /** Настраиваю конфигурацию хранения Person для security. antMatchers("/**").permitAll() создает полный доступ
     * ко всем urls */

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(
                    "/api/v1/auth/*",
                    "/api/v1/account/register",
                    "/api/v1/account/password/recovery",
                    "/api/v1/account/password/set",
                    "/api/v1/platform/languages",
                    "/api/v1/storage/*"
                )
                .permitAll()
                .antMatchers("/api/v1/**").hasRole("USER")
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                .apply(new TokenConfig(tokenProvider));
    }
}
