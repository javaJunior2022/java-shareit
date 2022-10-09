package ru.practicum.shareit.exceptions;


public class RequestNotFoundException extends EntityNotFoundException {
    public RequestNotFoundException(long id) {
        super(String.format("no request", id));

    }
}
