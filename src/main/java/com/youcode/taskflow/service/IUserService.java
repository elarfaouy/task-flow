package com.youcode.taskflow.service;

import com.youcode.taskflow.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUserService {
    List<UserDto> findAll();
    UserDto findOne(Long id);
}
