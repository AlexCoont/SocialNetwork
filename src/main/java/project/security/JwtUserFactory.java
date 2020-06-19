package project.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import project.models.Person;
import project.models.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(Person person){
        return new JwtUser.JwtUserBuilder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .regDate(person.getRegDate())
                .email(person.getEmail())
                .password(person.getPassword())
                .isBlocked(person.isBlocked())
                .authorities(mapToGrantedAuthorities(new ArrayList<>(person.getRoles())))
                .build();
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> personRoles){
        return personRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }
}

