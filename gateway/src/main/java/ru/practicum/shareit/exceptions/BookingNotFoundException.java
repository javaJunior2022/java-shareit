package ru.practicum.shareit.exceptions;

public class BookingNotFoundException extends EntityNotFoundException {

    public BookingNotFoundException(long bookingId) {
        super(String.format("this booking not found", bookingId));
    }
}
