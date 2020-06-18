package io.bitpark.redditclone.services;

import io.bitpark.redditclone.dto.CommentDto;
import io.bitpark.redditclone.exceptions.PostNotFoundException;
import io.bitpark.redditclone.exceptions.SpringRedditException;
import io.bitpark.redditclone.mappers.CommentMapper;
import io.bitpark.redditclone.models.Comment;
import io.bitpark.redditclone.models.NotificationEmail;
import io.bitpark.redditclone.models.Post;
import io.bitpark.redditclone.models.User;
import io.bitpark.redditclone.repositories.CommentRepository;
import io.bitpark.redditclone.repositories.PostRepository;
import io.bitpark.redditclone.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final AuthService authService;
    private final MailService mailService;
    private final MailContentBuilder builder;


    public CommentDto createComment(CommentDto commentDto) {
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found"));
        User user = authService.getCurrentUser();
        Comment newComment = commentMapper.map(commentDto, post, user);
        newComment.setUser(user);
        newComment.setPost(post);
        commentRepository.save(newComment);
        sendCommentEmail(post,user);
        return commentMapper.mapToDto(newComment);
    }

    private void sendCommentEmail(Post post, User commenter) {
        String message = builder.build(commenter.getUsername() + " commented on your post. " + post.getUrl());
        mailService.sendMail(new NotificationEmail(
                "New Comment no your post",
                post.getUser().getUsername(),
                message
        ));

    }

    public List<CommentDto> getPostComments(long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post Not Found"));

        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getUserComments(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SpringRedditException("User Not Found!"));

        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }


}
