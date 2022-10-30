package ru.practicum.shareit.exceptions;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String message) {
        super("this email " + message + " had been registered before");
    }
}
