package ru.practicum.shareit.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(long id) {
        super(String.format("request not found", id));

    }
}