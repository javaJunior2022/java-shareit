package ru.practicum.shareit.booking.strategy.strategyimpl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.booking.strategy.BookingState;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class StrategyWaiting implements Strategy {
    private final BookingRepository bookingRepository;

    @Override
    public BookingState getState() {
        return BookingState.WAITING;
    }

    @Override
    public List<BookingDto> findBookingByStrategy(Long bookerId, Pageable pageable) {
        return bookingRepository.findBookingsByBooker_IdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING, pageable)
                .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }
}