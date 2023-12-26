package com.youcode.taskflow.service;

import com.youcode.taskflow.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {
    UserDto findOne(Long id);
}
