package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.UserMapper.convertToDto;
import static ru.practicum.shareit.user.UserMapper.toUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;


    @Override
    public UserDto addUser(UserDto userDto) {
        log.info("'addUser' ", userDto);
        if (userStorage.emailExistingCheck(userDto.getEmail())) {
            throw new EmailAlreadyExistException(String.format("user with this email %s had been registered earlier",
                    userDto.getEmail()));
        }
        final User user = userStorage.addUser(toUser(userDto));
        return convertToDto(user);
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        log.info("'updateUser' ", userId, userDto);
        if (userStorage.emailExistingCheck(userDto.getEmail())) {
            throw new EmailAlreadyExistException(String.format("user with this email %s had been registered earlier",
                    userDto.getEmail()));
        }
        userDto.setId(userId);
        return userStorage.updateUser(toUser(userDto)).map(UserMapper::convertToDto)
                .orElseThrow(() -> new UserNotFoundException(userDto.getId()));
    }

    @Override
    public void deleteUserById(long userId) {
        log.info("'deleteUserById' ", userId);
        userStorage.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userStorage.deleteUserById(userId);
    }

    @Override
    public UserDto getUserById(long userId) {
        log.info("'getUserById' ", userId);
        return userStorage.getUserById(userId).map(UserMapper::convertToDto)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public List<UserDto> getUsers() {
        log.info("'getUsers' ");
        return userStorage.getUsers().stream().map(UserMapper::convertToDto).collect(Collectors.toList());
    }

}
