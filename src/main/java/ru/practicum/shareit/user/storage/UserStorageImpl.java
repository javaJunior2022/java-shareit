package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserStorageImpl implements UserStorage{

    private final Map<Long,User> users=new HashMap<>();

    @Override
    public User addUser(User user) {
        users.put(user.getId(),user);
        return user;
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (users.containsKey(user.getId())){
            return Optional.empty();
        }else {
            if (user.getName()!=null){
                users.get(user.getId()).setName(user.getName());
            }
            if (user.getEmail()!=null){
                users.get(user.getId()).setEmail(user.getEmail());
            }
            return Optional.ofNullable(users.get(user.getId()));
        }
    }

    @Override
    public void deleteUserById(long id) {
        users.remove(id);
    }

    @Override
    public Optional<User> getUserById(long id) {
        if (!users.containsKey(id)){
            return Optional.empty();
        }else {
            return Optional.ofNullable(users.get(id));
        }
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
