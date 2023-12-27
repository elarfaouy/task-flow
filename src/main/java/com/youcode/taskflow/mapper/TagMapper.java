package com.youcode.taskflow.mapper;

import com.youcode.taskflow.domain.entity.Tag;
import com.youcode.taskflow.dto.TagDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    Tag tagDtoToTag(TagDto tagDto);

    TagDto tagToTagDto(Tag tag);
}
