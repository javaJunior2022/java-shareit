package ru.practicum.shareit.item.ItemStorage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item addItem(long userId, Item item);

    Optional<Item> getItemById(long userId, long itemId);

    Optional<Item> updateItem(long userId, Item item);

    List<Item> getUserItems(long userId);

    List<Item> findItemByNameAndDescription(String text);
}
