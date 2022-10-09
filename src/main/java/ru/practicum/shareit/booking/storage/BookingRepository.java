package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("from Booking b where (b.item.owner.id=?1 or b.booker.id=?1) AND b.id=?2")
    Optional<Booking> findBooking(long userId, long bookingId);

    Optional<Booking> findByIdAndItem_Owner_Id(long bookingId, long ownerId);

    Optional<Booking> findFirstByItem_IdAndItem_Owner_IdAndStartAfterOrderByStartDesc(long itemId, long ownerId,
                                                                                      LocalDateTime start);

    Optional<Booking> findFirstByItem_IdAndItem_Owner_IdAndEndBeforeOrderByEndAsc(long itemId, long ownerId,
                                                                                  LocalDateTime end);

    Optional<Booking> findFirstByItem_IdAndBooker_IdAndEndBefore(long itemId, long bookerId, LocalDateTime date);

    @Query("from Booking b where b.booker.id=?1 and (?2 between b.start and b.end)")
    List<Booking> findCurrentBookingsByBookerId(long bookerId, LocalDateTime date, Pageable pageable);

    List<Booking> findBookingsByBooker_IdAndStartAfter(Long bookerId, LocalDateTime date, Pageable pageable);

    List<Booking> findBookingsByBooker_IdAndEndBefore(Long bookerId, LocalDateTime date, Pageable pageable);

    List<Booking> findBookingsByBooker_IdAndStatus(Long bookerId, BookingStatus status, Pageable page);

    List<Booking> findAllByBooker_Id(long userId, Pageable pageable);

    @Query("from Booking b where b.item.owner.id=?1 and (?2 between b.start and b.end)")
    List<Booking> findCurrentBookingsByItemOwnerId(long ownerId, LocalDateTime date, Pageable pageable);

    List<Booking> findBookingsByItem_Owner_IdAndStartAfter(Long ownerId, LocalDateTime date, Pageable pageable);

    List<Booking> findBookingsByItem_Owner_IdAndEndBefore(Long bookerId, LocalDateTime date, Pageable pageable);

    List<Booking> findBookingsByItem_Owner_IdAndStatus(Long bookerId, BookingStatus status, Pageable page);

    List<Booking> findBookingsByItem_Owner_Id(long userId, Pageable pageable);
}
