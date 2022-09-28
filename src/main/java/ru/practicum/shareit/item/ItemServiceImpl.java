package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.ItemStorage.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.*;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        log.info("'addItem'", userId, itemDto);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item item = itemRepository.save(toItem(user, itemDto, null));
        return toItemDto(item);

    }

    @Override
    public ItemDto getItemById(long userId, long itemId) {
        log.info("'getItemById'", userId, itemId);
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        return toItemDto(item);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        log.info("'updateItem'", userId, itemId, itemDto);

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item item = itemRepository.findByOwner_IdAndId(userId, itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        itemRepository.save(updateItemFromDto(item, itemDto));
        return toItemDto(item);
    }

    @Override
    public List<ItemDto> getUserItems(long userId) {
        log.info("'getUserItems'", userId);

        return itemRepository.findByOwner_Id(userId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItemByNameAndDescription(String text) {
        log.info("'findItemByName'", text);

        return itemRepository.search(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
