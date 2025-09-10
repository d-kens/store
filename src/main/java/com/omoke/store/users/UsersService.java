package com.omoke.store.users;


import com.omoke.store.dtos.ChangePasswordRequest;
import com.omoke.store.dtos.RegisterUserRequest;
import com.omoke.store.dtos.UpdateUserRequest;
import com.omoke.store.entities.Role;
import com.omoke.store.entities.User;
import com.omoke.store.exceptions.EmailAlreadyExistException;
import com.omoke.store.exceptions.NotFoundException;
import com.omoke.store.exceptions.PasswordMismatchException;
import com.omoke.store.mappers.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UsersService {
    private UserMapper userMapper;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;


    public List<UserDto> findAll(String sort) {
        if (!Set.of("name", "email").contains(sort))
            sort = "name";

        return usersRepository.findAll(Sort.by(sort))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto findUserById(Long userId) {
        User user = usersRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with " + userId + " not found")
        );
        return userMapper.toDto(user);
    }

    public UserDto createUser(RegisterUserRequest request) {
        if (usersRepository.existsByEmail(request.getEmail()))
            throw new EmailAlreadyExistException();

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        usersRepository.save(user);

        return userMapper.toDto(user);
    }

    public UserDto updateUser(Long userId, UpdateUserRequest request) {
        var user = usersRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with ID " + userId + " not found.")
        );

        userMapper.update(request, user);
        usersRepository.save(user);

        return userMapper.toDto(user);
    }

    public void deleteUser(Long userId) {
        var user = usersRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with ID " + userId + " not found")
        );

        usersRepository.delete(user);
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {
        var user = usersRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with ID " + userId + " not found")
        );

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
            throw new PasswordMismatchException();

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usersRepository.save(user);
    }
}