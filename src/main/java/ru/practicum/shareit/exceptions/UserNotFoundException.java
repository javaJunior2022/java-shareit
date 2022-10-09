package ru.practicum.shareit.exceptions;



public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(long userId) {
        super(String.format("user not found=", userId));
    }
}
