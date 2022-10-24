package ru.practicum.shareit.sql;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class UserServiceTest {
    private EntityManager entityManager;
    private UserService userService;

    @Autowired
    public UserServiceTest(EntityManager entityManager, UserService userService) {
        this.entityManager = entityManager;
        this.userService = userService;
    }

    private UserDto userDto1;
    private UserDto userDto2;

    @BeforeEach
    void setUp() {
        userDto1 = new UserDto(null, "user1", "user1@icloud.com");
        userDto2 = new UserDto(null, "user1", "user2@icloud.com");
    }

    @Test
    void shouldAddNewUser() {
        userService.addUser(userDto1);

        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.email= :email", User.class);
        User user = query.setParameter("email", userDto1.getEmail()).getSingleResult();
        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), is(userDto1.getName()));
    }

    @Test
    void shouldThrowExceptionWhenAddUserWithRepeatedEmail() {
        userService.addUser(userDto1);
        userDto2 = new UserDto(null, "user1", "user1@icloud.com");

        EmailAlreadyExistException e = Assertions.assertThrows(EmailAlreadyExistException.class,
                () -> userService.addUser(userDto2));
    }

    @Test
    void shouldUpdateUser() {
        userDto1 = userService.addUser(userDto1);

        userService.updateUser(userDto1.getId(), userDto2);
        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.id= :id", User.class);
        User userDtoAfter = query.setParameter("id", userDto1.getId()).getSingleResult();

        assertThat(userDtoAfter.getName(), is(userDto2.getName()));
        assertThat(userDtoAfter.getEmail(), is(userDto2.getEmail()));
        assertThat(userDtoAfter.getId(), is(userDto1.getId()));
    }

    @Test
    void shouldThrowExceptionWhenUpdateUserOnExistingEmail() {
        userDto1 = userService.addUser(userDto1);
        userDto2 = userService.addUser(userDto2);

        UserDto upd = new UserDto(null, "user3", "user1@icloud.com");

        EmailAlreadyExistException e = Assertions.assertThrows(EmailAlreadyExistException.class,
                () -> userService.updateUser(2L, upd));
        Assertions.assertEquals(e.getMessage(), "this email " + upd.getEmail() + " had been registered before");
    }

    @Test
    void shouldThroeExceptionWhenUpdateNotExistingUser() {
        UserNotFoundException e = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(1L, userDto1));
    }

    @Test
    void shouldThroeExceptionWhenDeleteNotExistingUser() {
        UserNotFoundException e = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.deleteUserById(1L));
    }

    @Test
    void shouldGetUserById() {
        userService.addUser(userDto1);
        userDto1 = userService.getUserById(1L);

        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.id= :id", User.class);
        User user = query.setParameter("id", userDto1.getId()).getSingleResult();

        assertThat(user.getId(), is(userDto1.getId()));
        assertThat(user.getName(), is(userDto1.getName()));
        assertThat(user.getEmail(), is(userDto1.getEmail()));
    }

    @Test
    void shouldGetAllUsers() {
        userService.addUser(userDto1);
        userService.addUser(userDto2);
        List<UserDto> listDto = userService.getAllUSer();

        TypedQuery<User> query = entityManager.createQuery("select u from User u", User.class);
        List<User> users = query.getResultList();

        assertThat(listDto, hasSize(users.size()));
        for (User user : users) {
            assertThat(listDto, hasItem(allOf(
                    hasProperty("id", is(user.getId())),
                    hasProperty("name", is(user.getName())),
                    hasProperty("email", is(user.getEmail()))
            )));
        }

    }
}
