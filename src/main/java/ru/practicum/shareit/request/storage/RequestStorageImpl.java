package ru.practicum.shareit.request.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.*;

@Slf4j
@Repository
public class RequestStorageImpl implements RequestStorage {
    private final Map<Long, Map<Long, ItemRequest>> storage = new HashMap<>();

    @Override
    public ItemRequest addRequest(long userId, ItemRequest itemRequest) {
        if (storage.containsKey(userId)) {
            storage.get(userId).put(itemRequest.getId(), itemRequest);
        } else {
            storage.put(userId, Map.of(itemRequest.getId(), itemRequest));
        }
        log.info("'addRequest'", userId, itemRequest);
        return itemRequest;
    }

    @Override
    public Optional<ItemRequest> updateRequest(long userId, long requestId, ItemRequest itemRequest) {
        log.info("'updateRequest'", userId, requestId, itemRequest);

        if (storage.get(userId).containsKey(requestId)) {
            if (itemRequest.getDescription() != null) {
                storage.get(userId).get(requestId).setDescription(itemRequest.getDescription());
            }
            return Optional.of(storage.get(userId).get(requestId));
        }
        return Optional.empty();
    }

    @Override
    public void deleteRequest(long userId, long requestId) {
        log.info ("'deleteRequest'", userId,requestId);
        storage.get(userId).remove(requestId);
    }

    @Override
    public Optional<ItemRequest> getRequest(long userId, long requestId) {
        log.info ("'getRequest'", userId,requestId);
        if (storage.get(userId).containsKey(requestId)) {
            return Optional.of(storage.get(userId).get(requestId));
        }
        log.info ("'getRequest' is empty", userId,requestId);
        return Optional.empty();
    }

    @Override
    public List<ItemRequest> getUserRequests(long userId) {
        log.info ("'getUserRequests'", userId,userId);
        if (storage.containsKey(userId)) {
            return new ArrayList<>(storage.get(userId).values());
        }
        return null;
    }
}
