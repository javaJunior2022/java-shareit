package ru.practicum.shareit.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(long itemId) {
        super(String.format("Can't find the item with this id %d",itemId));
    }
}