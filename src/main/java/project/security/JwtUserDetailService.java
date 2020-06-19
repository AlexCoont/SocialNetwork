package project.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.handlerExceptions.BadRequestException400;
import project.models.Person;
import project.models.Role;
import project.repositories.PersonRepository;
import project.services.PersonService;

/** Используется для работы с Security, т.к. Security должен работать с UserDetails*/

@Slf4j
@Service
public class JwtUserDetailService implements UserDetailsService
{
    private final PersonService personService;

    @Autowired
    public JwtUserDetailService(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = personService.findPersonByEmail(email).orElse(null);
        if(person == null){
            throw new BadRequestException400();
        }

        return JwtUserFactory.create(person);
    }
}
