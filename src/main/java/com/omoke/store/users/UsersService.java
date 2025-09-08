package com.omoke.store.users;


import com.omoke.store.entities.User;
import com.omoke.store.exceptions.NotFoundException;
import com.omoke.store.mappers.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsersService {
    private UserMapper userMapper;
    private final UsersRepository usersRepository;

    public UserDto findUserById(Long userId) {
        User user = usersRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with " + userId + " not found"));
        return userMapper.toDto(user);
    }
}
