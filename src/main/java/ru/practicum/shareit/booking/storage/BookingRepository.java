package ru.practicum.shareit.booking.storage;

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
    List<Booking> findCurrentBookingsByBookerId(long bookerId, LocalDateTime date);

    List<Booking> findBookingsByBooker_IdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime date);

    List<Booking> findBookingsByBooker_IdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime date);

    List<Booking> findBookingsByBooker_IdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    List<Booking> findAllByBooker_IdOrderByStartDesc(long userId);

    @Query("from Booking b where b.item.owner.id=?1 and (?2 between b.start and b.end)")
    List<Booking> findCurrentBookingsByItemOwnerIdOrderByStartDesc(long ownerId, LocalDateTime date);

    List<Booking> findBookingsByItem_Owner_IdAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime date);

    List<Booking> findBookingsByItem_Owner_IdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime date);

    List<Booking> findBookingsByItem_Owner_IdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    List<Booking> findBookingsByItem_Owner_IdOrderByStartDesc(long userId);
}
