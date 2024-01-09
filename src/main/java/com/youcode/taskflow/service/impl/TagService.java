package com.youcode.taskflow.service.impl;

import com.youcode.taskflow.dto.TagDto;
import com.youcode.taskflow.mapper.TagMapper;
import com.youcode.taskflow.repository.TagRepository;
import com.youcode.taskflow.service.ITagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TagService implements ITagService {
    private final TagRepository tagRepository;

    @Override
    public List<TagDto> findAll() {
        return tagRepository.findAll()
                .stream()
                .map(TagMapper.INSTANCE::tagToTagDto)
                .collect(Collectors.toList());
    }
}
