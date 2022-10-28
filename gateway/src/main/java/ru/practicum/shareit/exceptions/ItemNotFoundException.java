package ru.practicum.shareit.exceptions;



public class ItemNotFoundException extends EntityNotFoundException {
    public ItemNotFoundException(long itemId) {
        super(String.format("this item does not exist", itemId));
    }
}
