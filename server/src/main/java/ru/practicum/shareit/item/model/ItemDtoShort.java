package ru.practicum.shareit.item.model;

import lombok.Data;

@Data
public class ItemDtoShort {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;
}
