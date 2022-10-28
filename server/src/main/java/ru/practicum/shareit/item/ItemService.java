package ru.practicum.shareit.item;


import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoShort;

import java.util.List;

public interface ItemService {

    ItemDtoShort addItem(Long userId, ItemDtoShort itemDtoShort);

    ItemDto getItemById(Long userId, long itemId);

    ItemDtoShort updateItem(long userId, long itemId, ItemDtoShort itemDtoShort);

    List<ItemDto> getUserItems(long userId, int from, int size);

    List<ItemDtoShort> findItemByName(String text, int from, int size);

    CommentDto addComment(long authorId, long itemId, CommentDto commentDto);
}
