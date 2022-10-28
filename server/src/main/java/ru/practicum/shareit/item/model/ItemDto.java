package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingShort;

import java.util.List;

@Data
public class ItemDto {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long request;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private BookingShort lastBooking;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private BookingShort nextBooking;

    private List<CommentDto> comments;
}
