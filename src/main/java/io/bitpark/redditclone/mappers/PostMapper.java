package io.bitpark.redditclone.mappers;

import io.bitpark.redditclone.dto.PostRequest;
import io.bitpark.redditclone.dto.PostResponse;
import io.bitpark.redditclone.models.*;
import io.bitpark.redditclone.repositories.CommentRepository;
import io.bitpark.redditclone.repositories.VoteRepository;
import io.bitpark.redditclone.services.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static io.bitpark.redditclone.models.VoteType.DOWNVOTE;
import static io.bitpark.redditclone.models.VoteType.UPVOTE;


@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subReddit", source = "subReddit")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    public abstract  Post map(PostRequest postRequest, SubReddit subReddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subRedditName", source = "subReddit.name")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract  PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return post.getCreatedDate().toString();
    }

    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                            authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }
}
