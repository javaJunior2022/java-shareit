package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Repository
public class UserStorageImpl implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long userCountId = 1;

    @Override
    public User addUser(User user) {
        user.setId(userCountId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> updateUser(User user) {
        log.info("'updateUser' ", user);
        if (!users.containsKey(user.getId())) {
            return Optional.empty();
        } else {
            if (user.getName() != null) {
                users.get(user.getId()).setName(user.getName());
            }
            if (user.getEmail() != null) {
                users.get(user.getId()).setEmail(user.getEmail());
            }
            return Optional.ofNullable(users.get(user.getId()));
        }
    }

    @Override
    public void deleteUserById(long userId) {
        log.info("'deleteUserById' ", userId);
        users.remove(userId);
    }

    @Override
    public Optional<User> getUserById(long userId) {
        log.info("'getUserById' ", userId);
        if (!users.containsKey(userId)) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(users.get(userId));
        }
    }

    @Override
    public List<User> getUsers() {
        log.info("'getUsers' ");
        return new ArrayList<>(users.values());
    }

    public boolean emailExistingCheck(String email) {
        log.info("'emailExistingCheck' ", email);
        return users.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }
}
