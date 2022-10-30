package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoShort;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    ItemDtoShort addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                         @RequestBody ItemDtoShort item) {
        return itemService.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    ItemDtoShort updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                            @RequestBody ItemDtoShort item) {
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    ItemDto getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                    @PathVariable(name = "itemId") long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    List<ItemDto> getOwnerItems(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(name = "from") Integer from,
                                @RequestParam(name = "size") Integer size) {
        return itemService.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    List<ItemDtoShort> findItemsByName(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(name = "text") String text,
                                       @RequestParam(name = "from") Integer from, @RequestParam(name = "size") Integer size) {
        return text.isBlank() ? Collections.emptyList() : itemService.findItemByName(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long authorId, @PathVariable(name = "itemId") Long itemId,
                          @RequestBody CommentDto commentDto) {
        return itemService.addComment(authorId, itemId, commentDto);
    }
}