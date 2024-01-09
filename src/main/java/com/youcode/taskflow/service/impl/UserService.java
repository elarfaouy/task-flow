package com.youcode.taskflow.service.impl;

import com.youcode.taskflow.domain.entity.User;
import com.youcode.taskflow.dto.UserDto;
import com.youcode.taskflow.mapper.UserMapper;
import com.youcode.taskflow.repository.UserRepository;
import com.youcode.taskflow.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper.INSTANCE::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findOne(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
        return UserMapper.INSTANCE.userToUserDto(user);
    }
}
