package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.Valid;


@Controller
@AllArgsConstructor(onConstructor_ = @Autowired)
@Validated
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemClient itemClient;


    @PostMapping
    ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @RequestBody @Validated(Create.class) ItemDtoShort item) {
        log.info("addItem userId {}, item{}", userId, item);
        return itemClient.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                                      @RequestBody @Validated(Update.class) ItemDtoShort item) {
        log.info("updateItem userId {}, itemId {}, item {}", userId, itemId, item);
        return itemClient.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable(name = "itemId") long itemId) {
        log.info("getItem userId {}, itemId {}", userId, itemId);
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    ResponseEntity<Object> getOwnerItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("getOwnerItems userId {}, from {}, size{}", userId, from, size);
        return itemClient.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    ResponseEntity<Object> findItemsByName(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestParam(name = "text") String text,
                                           @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("findItemsByName userId {}, text {}, from {}, size{}", userId, text, from, size);
        return itemClient.findItemByName(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long authorId, @PathVariable(name = "itemId") Long itemId,
                                      @RequestBody @Valid CommentDto commentDto) {
        log.info("addComment authorId {}, itemId {}, commentDto {}", authorId, itemId, commentDto);
        return itemClient.addComment(authorId, itemId, commentDto);
    }
}
