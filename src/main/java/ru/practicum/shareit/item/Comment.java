package ru.practicum.shareit.item;

import lombok.Data;
import ru.practicum.shareit.LocalDateTimeConverter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text")
    private String text;

    @ManyToOne
    private Item item;

    @ManyToOne
    private User author;

    @Column(name = "created")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime created = LocalDateTime.now();
}
