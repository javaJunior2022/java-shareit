package ru.practicum.shareit.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(long userId) {
        super(String.format("could not find this user", userId));
    }
}
