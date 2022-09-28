package ru.practicum.shareit.booking.model;

import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;
import ru.practicum.shareit.LocalDateTimeConverter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
@Table (name="bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="start_date")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime start;

    @Column(name="end_date")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name="booker_id")
    private User booker;

    @Enumerated(EnumType.ORDINAL)
    private BookingStatus status=BookingStatus.WAITING;

}
