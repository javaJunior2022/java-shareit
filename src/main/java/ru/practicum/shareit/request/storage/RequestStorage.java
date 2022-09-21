package ru.practicum.shareit.request.storage;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface RequestStorage {

    ItemRequest addRequest(long userId, ItemRequest itemRequest);

    Optional<ItemRequest> updateRequest(long userId, long requestId, ItemRequest itemRequest);

    void deleteRequest(long userId, long requestId);

    Optional<ItemRequest> getRequest(long userId, long requestId);

    List<ItemRequest> getUserRequests(long userId);
}