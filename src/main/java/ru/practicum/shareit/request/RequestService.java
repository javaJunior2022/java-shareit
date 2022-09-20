package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface RequestService {
    ItemRequestDto addRequest(long userId, ItemRequestDto itemRequestDto);

    ItemRequestDto updateRequest(long userId, long requestId, ItemRequestDto itemRequestDto);

    void deleteRequest(long userId, long requestId);

    ItemRequestDto getRequest(long userId, long requestId);

    List<ItemRequestDto> getUserRequests(long userId);
}
