package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.PaginationParametersException;
import ru.practicum.shareit.exceptions.RequestNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.RequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoEntry;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Transactional

public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestDto addRequest(long userId, ItemRequestDtoEntry itemRequestDtoEntry) {
        User requestor = checkUser(userId);
        ItemRequest itemRequest = requestRepository.save(RequestMapper.toRequest(requestor, itemRequestDtoEntry));
        log.info("addRequest", itemRequestDtoEntry);
        return RequestMapper.toRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto getRequest(long userId, long requestId) {
        log.info("getRequest");
        checkUser(userId);
        return requestRepository.findById(requestId)
                .map(itemRequest -> RequestMapper.toRequestDto(itemRequest))
                .orElseThrow(() -> new RequestNotFoundException(requestId));
    }

    @Override
    public List<ItemRequestDto> getUserRequests(long userId) {
        log.info("getUserRequests");
        checkUser(userId);
        return requestRepository.findAllByRequestor_IdOrderByCreatedDesc(userId).stream()
                .map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getRequests(long userId, Integer from, Integer size) {
        log.info("getRequests");
        checkUser(userId);
        return requestRepository.findAllByOtherUser(userId, makePageParam(from, size)).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    public static Pageable makePageParam(int from, int size) {
        if (from < 0 || size < 1) {
            throw new PaginationParametersException("wrong");
        }
        int page = from / size;
        Sort sort = Sort.by("created").descending();
        return PageRequest.of(page, size, sort);
    }
}
