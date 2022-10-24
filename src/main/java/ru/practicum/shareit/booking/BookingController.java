package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoEntry;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody @Valid BookingDtoEntry bookingDtoEntry) {
        return bookingService.addBooking(userId, bookingDtoEntry);
    }

    @PatchMapping("/{bookingId}")
    BookingDto approveStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @PathVariable(name = "bookingId") Long bookingId,
                             @RequestParam(name = "approved") Boolean approved) {
        return bookingService.approveStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable(name = "bookingId") Long bookingId) {
        return bookingService.getBookingByUserIdAndBookingId(userId, bookingId);
    }

    @GetMapping
    List<BookingDto> getUserBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestParam(name = "state", defaultValue = "ALL", required = false) String state,
                                    @RequestParam(name = "from", defaultValue = "0") Integer from,
                                    @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return bookingService.getUserBookingByState(userId, state, from, size);
    }

    @GetMapping("/owner")
    List<BookingDto> getBookingForUsersItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(name = "state", defaultValue = "ALL", required = false) String state,
                                            @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return bookingService.getBookingForUsersItem(userId, state, from, size);
    }

}
