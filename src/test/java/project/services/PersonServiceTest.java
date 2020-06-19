package project.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import project.dto.requestDto.LoginRequestDto;
import project.dto.requestDto.PasswordSetDto;
import project.dto.requestDto.RegistrationRequestDto;
import project.dto.requestDto.UpdatePersonDto;
import project.dto.responseDto.PersonDtoWithToken;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.EntityAlreadyExistException;
import project.models.Person;
import project.models.Role;
import project.models.enums.MessagesPermission;
import project.models.enums.RoleEnum;
import project.repositories.PersonRepository;
import project.repositories.RoleRepository;
import project.security.TokenProvider;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@TestPropertySource("/test.properties")
public class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RoleRepository roleRepository;

    private static final ObjectMapper om = new ObjectMapper();

    private static String token;

    private MockHttpServletRequest mockHttpServletRequest;

    @Autowired
    public PersonServiceTest(TokenProvider tokenProvider) {
        mockHttpServletRequest = new MockHttpServletRequest();

        this.token = tokenProvider.createToken("test1@mail.ru");
        mockHttpServletRequest.addHeader("Authorization",token);
        mockHttpServletRequest.addHeader("referer", "можен кто разберется что сюда кидать"); //!!!!!!!!!!
    }

    @Test
    void add() throws EntityAlreadyExistException {

        RegistrationRequestDto person = new RegistrationRequestDto(
                "testMan@mail.ru",
                "123123123",
                "123123123",
                "firstTest",
                "lastTest" ,
                "3675");

        personService.add(person, roleRepository.findByName(RoleEnum.ROLE_USER).get());

        assertNotNull(personRepository.findByEmail("test5@mail.ru").orElse(null));
    }

    @Test
    void login() {
//        LoginRequestDto loginRequestDto = new LoginRequestDto("ilyxa043@gmail.com", "qweasdzxc");
//        Person person = new Person();
//        person.setEmail("ilyxa043@gmail.com");
//        person.setPassword(encoder.encode(loginRequestDto.getPassword()));
//        Mockito.doReturn(Optional.of(person)).when(personRepository).findPersonByEmail(person.getEmail());
//
//        ResponseDto responseDto = personService.login(loginRequestDto);
//
//        PersonDtoWithToken personDto = (PersonDtoWithToken) responseDto.getData();
//        assertEquals(loginRequestDto.getEmail(), personDto.getEmail());

        LoginRequestDto dto = new LoginRequestDto("test1@mail.ru", "123123123");
        PersonDtoWithToken personDtoWithToken = personService.login(dto).getData();
        assertEquals(personDtoWithToken.getLastName(), "last1");
    }

    @Test
    void sendRecoveryPasswordEmail() {

    }

    @Test
    void setNewPassword() { // referer нужен в header
        String password = "testPass";
        personService.setNewPassword(new PasswordSetDto(token, password), mockHttpServletRequest);
        Person person = personRepository.findById(10).get();
        assertEquals(person.getPassword(), encoder.encode(password));
    }

    @Test
    void findPersonById() {
        Person person = personService.findPersonById(2);
        assertEquals(person.getEmail(), "test2@mail.ru");
    }

    @Test
    void blockPersonById() {
        personService.blockPersonById(2, true, 1);
        Person person = personRepository.findById(2).get();
        assertTrue(person.isBlocked());
    }

    @Test
    void deletePersonByEmail() {
        personService.deletePersonByEmail("test4@mail.ru");
        Person person = personRepository.findPersonByEmail("test4@mail.ru").orElse(null);
        assertNull(person);
    }


    @Test
    void editBody() {
        personService.editBody(new UpdatePersonDto(
                "first1",
                "last1",
                new Date(),
                "03",
                null,
                null,
                null,
                null,
                MessagesPermission.ALL),mockHttpServletRequest );
        Person person = personRepository.findById(10).get();
        assertEquals(person.getPhone(), "03");
    }

    @Test
    void saveLastOnlineTime() {
        //не знаю как это проверить
    }

    @Test
    void updatePhoto() {
        Person person = personRepository.findById(10).get();
        String url = "images/default-user-image.png";
        personService.updatePhoto(person, url);

        Person expected = personRepository.findById(10).get();
        assertEquals(expected.getPhoto(), url);
    }

    @Test
    void search() {
        Person person = personRepository.findById(10).get();
        Page<Person> people = personService.search(
                person,
                "first2",
                null,
                null,
                null,
                null,
                null,
                0,
                20);
        assertEquals(people.getTotalElements(), 1L);
    }
}