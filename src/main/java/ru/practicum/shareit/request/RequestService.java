package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoEntry;

import java.util.*;

public interface RequestService {

    ItemRequestDto addRequest(long userId, ItemRequestDtoEntry itemRequestDto);

    List<ItemRequestDto> getUserRequests(long userId);

    ItemRequestDto getRequest(long userId, long requestId);

    List<ItemRequestDto> getRequests(long userId, Integer from, Integer size);
}