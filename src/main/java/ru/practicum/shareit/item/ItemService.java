package ru.practicum.shareit.item;

import ru.practicum.shareit.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDtoShort addItem(Long userId, ItemDtoShort itemDtoShort);

    ItemDto getItemById(Long userId, long itemId);

    ItemDtoShort updateItem(long userId, long itemId, ItemDtoShort itemDtoShort);

    List<ItemDto> getUserItems(long userId);

    List<ItemDtoShort> findItemByName(String text);

    CommentDto addComment(long authorId, long itemId, CommentDto commentDto);
}
