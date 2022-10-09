package ru.practicum.shareit.booking.strategy.strategyimpl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.booking.strategy.BookingState;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class StrategyFuture implements Strategy {

    private final BookingRepository bookingRepository;

    @Override
    public BookingState getState() {
        return BookingState.FUTURE;
    }

    @Override
    public List<BookingDto> findBookingByStrategy(Long bookerId) {
        LocalDateTime date = LocalDateTime.now();
        return bookingRepository.findBookingsByBooker_IdAndStartAfterOrderByStartDesc(bookerId, date)
                .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }
}
