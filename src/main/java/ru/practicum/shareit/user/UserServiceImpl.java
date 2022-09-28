package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        log.info("'addUser' ", userDto);

        User user;
        try {
            user = userRepository.save(UserMapper.toEntity(userDto));
        } catch (RuntimeException e) {
            throw new EmailAlreadyExistException(userDto.getEmail());
        }

        return UserMapper.convertToDto(user);
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        log.info("'updateUser' ", userId, userDto);

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistException(userDto.getEmail());
        }

        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException(userId);
        }

        User user = UserMapper.convertFromDto(userRepository.findById(userId).get(), userDto);
        userDto.setId(userId);
        userRepository.save(user);

        return UserMapper.convertToDto(user);
    }

    @Override
    public void deleteUserById(long userId) {
        log.info("'deleteUserById' ", userId);
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.delete(userRepository.findById(userId).get());
    }

    @Override
    public UserDto getUserById(long userId) {
        log.info("'getUserById' ", userId);
        return userRepository.findById(userId).map(UserMapper::convertToDto).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public List<UserDto> getUsers() {
        log.info("'getUsers' ");
        return userRepository.findAll().stream().map(UserMapper::convertToDto).collect(Collectors.toList());
    }
}
