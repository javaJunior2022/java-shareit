package ru.practicum.shareit.item.ItemStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, Set<Long>> usersItems = new HashMap<>();
    private final Map<Long, Item> itemsStorage = new HashMap<>();

    @Override
    public Item addItem(long userId, Item item) {
        final Set<Long> items = usersItems.computeIfAbsent(userId, k -> new HashSet<>());
        items.add(item.getId());
        itemsStorage.put(item.getId(), item);
        log.info("Item " + item + " has been created");
        return item;
    }

    @Override
    public Optional<Item> getItemById(long userId, long itemId) {
        if (!itemsStorage.containsKey(itemId)) {
            log.info("could not find the item " + itemId);
            return Optional.empty();
        }
        return Optional.of(itemsStorage.get(itemId));
    }

    @Override
    public Optional<Item> updateItem(long userId, Item item) {
        if (!usersItems.containsKey(userId) || !usersItems.get(userId).contains(item.getId())) {
            return Optional.empty();
        }
        final Item updatedItem = itemsStorage.get(item.getId());

        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }

        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }

        if (item.getItemRequest() != null) {
            updatedItem.setItemRequest(item.getItemRequest());
        }

        return Optional.of(updatedItem);
    }

    @Override
    public List<Item> getUserItems(long userId) {
        if (!usersItems.containsKey(userId)) {
            return null;
        }
        return usersItems.get(userId).stream().map(itemsStorage::get).collect(Collectors.toList());
    }

    @Override
    public List<Item> findItemByName(String text) {
        return itemsStorage.values().stream().filter(Item::getAvailable)
                .filter(items -> items.getName().toLowerCase().contains(text.toLowerCase()) ||
                        items.getDescription().toLowerCase().contains(text.toLowerCase())).collect(Collectors.toList());
    }
}
