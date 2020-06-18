package io.bitpark.redditclone.mappers;

import io.bitpark.redditclone.dto.SubRedditDto;
import io.bitpark.redditclone.models.Post;
import io.bitpark.redditclone.models.SubReddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubRedditMapper {
    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subReddit.getPosts()))")
    SubRedditDto mapSubRedditToDto(SubReddit subReddit);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    SubReddit mapDtoToSubReddit(SubRedditDto subRedditDto);
}
