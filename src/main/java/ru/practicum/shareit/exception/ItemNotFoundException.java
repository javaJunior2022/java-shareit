package ru.practicum.shareit.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(long itemId) {
        super("Can't find the item with id: " + itemId);
    }
}