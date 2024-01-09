package com.youcode.taskflow.web.rest;

import com.youcode.taskflow.dto.TagDto;
import com.youcode.taskflow.service.ITagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/tag")
@RequiredArgsConstructor
public class TagRest {
    private final ITagService tagService;

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<TagDto> tags = tagService.findAll();
        return ResponseEntity.ok(tags);
    }
}
