package ru.practicum.shareit.sql;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoEntry;
import ru.practicum.shareit.booking.model.Booking;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
@Sql(scripts = {"/schema.sql", "/add_users.sql", "/add_items.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookingServiceTest {
    private final EntityManager entityManager;
    private final BookingService bookingService;

    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    void setUp() {
        start = LocalDateTime.now().plusDays(2);
        end = LocalDateTime.now().plusDays(3);
    }

    @Test
    void shouldAddBooking() {
        Long itemId = 1L;
        Long bookerId = 1L;
        BookingDtoEntry bookingDtoEntry = new BookingDtoEntry(start, end, itemId);

        BookingDto response = bookingService.addBooking(bookerId, bookingDtoEntry);
        TypedQuery<Booking> query = entityManager.createQuery("select t1 from Booking as t1 " +
                "where t1.item.id=:itemId and t1.booker.id=:bookerId", Booking.class);
        Booking responseQuery = query.setParameter("itemId", itemId).setParameter("bookerId", bookerId)
                .getSingleResult();

        assertThat(response.getId(), is(responseQuery.getId()));
        assertThat(response.getStart(), is(responseQuery.getStart()));
        assertThat(response.getStatus(), allOf(is(responseQuery.getStatus()), is(BookingStatus.WAITING)));
        assertThat(response.getItem().getId(), is(responseQuery.getItem().getId()));
        assertThat(response.getBooker(), is(responseQuery.getBooker()));
    }

    @Test
    void shouldApproveStatus() {
        Long ownerItemId = 2L;
        BookingDtoEntry bookingDtoEntry = new BookingDtoEntry(start, end, 1L);
        bookingService.addBooking(1L, bookingDtoEntry);

        BookingDto response = bookingService.approveStatus(ownerItemId, 1L, true);
        TypedQuery<Booking> query = entityManager.createQuery("select t1 from Booking as t1 " +
                "where t1.id=:bookingId", Booking.class);
        Booking responseQuery = query.setParameter("bookingId", 1L).getSingleResult();

        assertThat(response.getId(), is(responseQuery.getId()));
        assertThat(response.getStatus(), allOf(is(responseQuery.getStatus()), is(BookingStatus.APPROVED)));
    }

    @Test
    void shouldGetBookingByBookerOrOwnerIdIdAndBookingId() {
        Long ownerItemId = 2L;
        Long bookerId = 1L;
        BookingDtoEntry bookingDtoEntry = new BookingDtoEntry(start, end, 1L);
        bookingService.addBooking(bookerId, bookingDtoEntry);

        BookingDto response = bookingService.getBookingByUserIdAndBookingId(ownerItemId, 1L);
        BookingDto responseByBooker = bookingService.getBookingByUserIdAndBookingId(bookerId, 1L);
        TypedQuery<Booking> query = entityManager.createQuery("select b from Booking b " +
                "where b.id=:bookingId and b.booker.id=:bookerId", Booking.class);
        Booking responseQuery = query.setParameter("bookingId", 1L).setParameter("bookerId", bookerId)
                .getSingleResult();

        assertThat(response.getId(), is(responseQuery.getId()));
        assertThat(response.getStart().toLocalDate(), is(responseQuery.getStart().toLocalDate()));
        assertThat(response.getStatus(), allOf(is(responseQuery.getStatus()), is(BookingStatus.WAITING)));
        assertThat(response.getItem().getId(), is(responseQuery.getItem().getId()));
        assertThat(response, is(responseByBooker));
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/add_users.sql", "/add_items.sql", "/add_bookings.sql"})
    void shouldGetUserBookingByStateFuture() {
        Long bookerId = 3L;

        List<BookingDto> response = bookingService.getUserBookingByState(bookerId, "future", 0, 2);
        TypedQuery<Booking> query = entityManager.createQuery("select t1 from Booking as t1 " +
                "where t1.booker.id=:bookerId and t1.start > :date order by t1.start desc", Booking.class);
        List<Booking> responseQuery = query.setParameter("bookerId", bookerId).setParameter("date", LocalDateTime.now())
                .setFirstResult(0).setMaxResults(2).getResultList();

        assertThat(response, hasSize(allOf(is(responseQuery.size()), is(2))));
        for (BookingDto bookingDto : response) {
            assertThat(responseQuery, hasItem(allOf(
                    hasProperty("id", is(bookingDto.getId())),
                    hasProperty("booker", is(bookingDto.getBooker())),
                    hasProperty("item", hasProperty("id", is(bookingDto.getItem().getId()))),
                    hasProperty("start", is(bookingDto.getStart())),
                    hasProperty("end", is(bookingDto.getEnd()))
            )));
            assertThat(bookingDto.getBooker().getId(), is(bookerId));
        }
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/add_users.sql", "/add_items.sql", "/add_bookings.sql"})
    void shouldGetUserBookingByStateCurrent() {
        Long bookerId = 3L;

        List<BookingDto> response = bookingService.getUserBookingByState(bookerId, "current", 0, 2);
        TypedQuery<Booking> query = entityManager.createQuery("select t1 from Booking as t1 " +
                "where t1.booker.id=:bookerId and :date between t1.start and t1.end order by t1.start desc", Booking.class);
        List<Booking> responseQuery = query.setParameter("bookerId", bookerId).setParameter("date", LocalDateTime.now())
                .setFirstResult(0).setMaxResults(2).getResultList();

        assertThat(response, hasSize(allOf(is(responseQuery.size()), is(1))));
        for (BookingDto bookingDto : response) {
            assertThat(responseQuery, hasItem(allOf(
                    hasProperty("id", is(bookingDto.getId())),
                    hasProperty("booker", is(bookingDto.getBooker())),
                    hasProperty("item", hasProperty("id", is(bookingDto.getItem().getId()))),
                    hasProperty("start", is(bookingDto.getStart())),
                    hasProperty("end", is(bookingDto.getEnd()))
            )));
            assertThat(bookingDto.getBooker().getId(), is(bookerId));
        }
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/add_users.sql", "/add_items.sql", "/add_bookings.sql"})
    void shouldGetUserBookingByStateAll() {
        Long bookerId = 3L;

        List<BookingDto> response = bookingService.getUserBookingByState(bookerId, "all", 0, 4);
        TypedQuery<Booking> query = entityManager.createQuery("select t1 from Booking as t1 " +
                "where t1.booker.id=:bookerId", Booking.class);
        List<Booking> responseQuery = query.setParameter("bookerId", bookerId).setFirstResult(0)
                .setMaxResults(4).getResultList();

        assertThat(response, hasSize(allOf(is(responseQuery.size()), is(3))));
        for (BookingDto bookingDto : response) {
            assertThat(responseQuery, hasItem(allOf(
                    hasProperty("id", is(bookingDto.getId())),
                    hasProperty("booker", is(bookingDto.getBooker())),
                    hasProperty("item", hasProperty("id", is(bookingDto.getItem().getId()))),
                    hasProperty("start", is(bookingDto.getStart())),
                    hasProperty("end", is(bookingDto.getEnd()))
            )));
            assertThat(bookingDto.getBooker().getId(), is(bookerId));
        }
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/add_users.sql", "/add_items.sql", "/add_bookings.sql"})
    void shouldGetUserBookingByStatePast() {
        Long bookerId = 1L;

        List<BookingDto> response = bookingService.getUserBookingByState(bookerId, "past", 0, 3);
        TypedQuery<Booking> query = entityManager.createQuery("select t1 from Booking as t1 " +
                "where t1.booker.id=:bookerId and t1.end < :date order by t1.start desc", Booking.class);
        List<Booking> responseQuery = query.setParameter("bookerId", bookerId).setParameter("date", LocalDateTime.now())
                .setFirstResult(0).setMaxResults(3).getResultList();

        assertThat(response, hasSize(allOf(is(responseQuery.size()), is(1))));
        for (BookingDto bookingDto : response) {
            assertThat(responseQuery, hasItem(allOf(
                    hasProperty("id", is(bookingDto.getId())),
                    hasProperty("booker", is(bookingDto.getBooker())),
                    hasProperty("item", hasProperty("id", is(bookingDto.getItem().getId()))),
                    hasProperty("start", is(bookingDto.getStart())),
                    hasProperty("end", is(bookingDto.getEnd()))
            )));
            assertThat(bookingDto.getBooker().getId(), is(bookerId));
        }
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/add_users.sql", "/add_items.sql", "/add_bookings.sql"})
    void shouldGetBookingByItemOwnerAndStateRejected() {
        Long itemsOwner = 1L;

        List<BookingDto> response = bookingService.getBookingForUsersItem(itemsOwner, "rejectEd", 0, 2);
        TypedQuery<Booking> query = entityManager.createQuery("select t1 from Booking as t1 " +
                "where t1.item.owner.id=:ownerId and t1.status = :status order by t1.start desc", Booking.class);
        List<Booking> responseQuery = query.setParameter("ownerId", itemsOwner).setParameter("status", BookingStatus.REJECTED)
                .setFirstResult(0).setMaxResults(4).getResultList();
        assertThat(response, hasSize(allOf(is(responseQuery.size()), is(2))));

        for (BookingDto bookingDto : response) {
            assertThat(responseQuery, hasItem(allOf(
                    hasProperty("id", is(bookingDto.getId())),
                    hasProperty("booker", is(bookingDto.getBooker())),
                    hasProperty("item", hasProperty("id", is(bookingDto.getItem().getId()))),
                    hasProperty("start", is(bookingDto.getStart())),
                    hasProperty("end", is(bookingDto.getEnd()))
            )));
        }
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/add_users.sql", "/add_items.sql", "/add_bookings.sql"})
    void shouldGetBookingByItemOwnerAndStateFuture() {
        Long itemsOwner = 1L;

        List<BookingDto> response = bookingService.getBookingForUsersItem(itemsOwner, "Future", 0, 2);
        TypedQuery<Booking> query = entityManager.createQuery("select t1 from Booking as t1 " +
                "where t1.item.owner.id=:ownerId and t1.start > :date order by t1.start desc", Booking.class);
        List<Booking> responseQuery = query.setParameter("ownerId", itemsOwner).setParameter("date", LocalDateTime.now())
                .setFirstResult(0).setMaxResults(4).getResultList();
        assertThat(response, hasSize(allOf(is(responseQuery.size()), is(2))));

        for (BookingDto bookingDto : response) {
            assertThat(responseQuery, hasItem(allOf(
                    hasProperty("id", is(bookingDto.getId())),
                    hasProperty("booker", is(bookingDto.getBooker())),
                    hasProperty("item", hasProperty("id", is(bookingDto.getItem().getId()))),
                    hasProperty("start", is(bookingDto.getStart())),
                    hasProperty("end", is(bookingDto.getEnd()))
            )));
        }
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/add_users.sql", "/add_items.sql", "/add_bookings.sql"})
    void shouldGetBookingByItemOwnerAndStatePast() {
        Long itemsOwner = 1L;

        List<BookingDto> response = bookingService.getBookingForUsersItem(itemsOwner, "Past", 0, 2);
        TypedQuery<Booking> query = entityManager.createQuery("select t1 from Booking as t1 " +
                "where t1.item.owner.id=:ownerId and t1.end < :date order by t1.start desc", Booking.class);
        List<Booking> responseQuery = query.setParameter("ownerId", itemsOwner).setParameter("date", LocalDateTime.now())
                .setFirstResult(0).setMaxResults(4).getResultList();

        assertThat(response, hasSize(allOf(is(responseQuery.size()), is(1))));
        for (BookingDto bookingDto : response) {
            assertThat(responseQuery, hasItem(allOf(
                    hasProperty("id", is(bookingDto.getId())),
                    hasProperty("booker", is(bookingDto.getBooker())),
                    hasProperty("item", hasProperty("id", is(bookingDto.getItem().getId()))),
                    hasProperty("start", is(bookingDto.getStart())),
                    hasProperty("end", is(bookingDto.getEnd()))
            )));
        }
    }
}
