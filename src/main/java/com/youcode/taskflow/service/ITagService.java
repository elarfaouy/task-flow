package com.youcode.taskflow.service;

import com.youcode.taskflow.dto.TagDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ITagService {
    List<TagDto> findAll();
}
