package ru.practicum.shareit.booking.strategy.strategyimpl;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.strategy.BookingState;

import java.util.List;

public interface Strategy {

    BookingState getState();

    List<BookingDto> findBookingByStrategy(Long bookerId);
}
