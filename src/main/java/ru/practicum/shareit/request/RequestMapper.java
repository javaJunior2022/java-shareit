package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoEntry;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {

    public static ItemRequestDto toRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(), itemRequest.getDescription(), itemRequest.getRequestor().getId(),
                itemRequest.getCreated(), toItemShortSet(itemRequest.getItems()));
    }

    public static ItemRequest toRequest(User requestor, ItemRequestDtoEntry itemRequestDtoEntry) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(requestor);
        itemRequest.setDescription(itemRequestDtoEntry.getDescription());
        return itemRequest;
    }

    public static Set<ItemDtoShort> toItemShortSet(Set<Item> items) {
        if (items == null) {
            return Collections.emptySet();
        }
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toSet());
    }



}
