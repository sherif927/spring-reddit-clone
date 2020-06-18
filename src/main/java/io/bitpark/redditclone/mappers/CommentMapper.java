package io.bitpark.redditclone.mappers;

import io.bitpark.redditclone.dto.CommentDto;
import io.bitpark.redditclone.models.Comment;
import io.bitpark.redditclone.models.Post;
import io.bitpark.redditclone.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Comment map (CommentDto commentDto, Post post, User user);

    @Mapping(target = "postId", expression =  "java(comment.getPost().getPostId())")
    @Mapping(target = "username", expression = "java(comment.getUser().getUsername())")
    CommentDto mapToDto(Comment comment);
}
