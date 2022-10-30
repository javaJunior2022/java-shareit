package ru.practicum.shareit.exceptions;

public class ItemAvailableException extends RuntimeException {
    public ItemAvailableException(long itemId) {
        super("this item is not available " + itemId);
    }
}
