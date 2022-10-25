package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@AllArgsConstructor(onConstructor_ = @Autowired)
@Validated
@Slf4j
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final RequestClient requestClient;


    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody @Valid ItemRequestDtoEntry itemRequestDtoEntry) {
        log.info("addRequest userId {}, itemRequestDtoEntry{}", userId, itemRequestDtoEntry);
        return requestClient.addRequest(userId, itemRequestDtoEntry);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("getUserRequests userId {}", userId);
        return requestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("getRequests userId {}, from{}, size{}", userId, from, size);
        return requestClient.getRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long requestId) {
        log.info("getRequests userId {}, requestId{}", userId, requestId);
        return requestClient.getRequest(userId, requestId);
    }
}
