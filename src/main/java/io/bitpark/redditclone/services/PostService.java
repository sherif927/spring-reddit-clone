package io.bitpark.redditclone.services;

import io.bitpark.redditclone.dto.PostRequest;
import io.bitpark.redditclone.dto.PostResponse;
import io.bitpark.redditclone.exceptions.PostNotFoundException;
import io.bitpark.redditclone.exceptions.SubRedditNotFoundException;
import io.bitpark.redditclone.mappers.PostMapper;
import io.bitpark.redditclone.models.Post;
import io.bitpark.redditclone.models.SubReddit;
import io.bitpark.redditclone.models.User;
import io.bitpark.redditclone.repositories.PostRepository;
import io.bitpark.redditclone.repositories.SubRedditRepository;
import io.bitpark.redditclone.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final SubRedditRepository subRedditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;


    public PostResponse createPost(PostRequest postRequest) {
        SubReddit subReddit = subRedditRepository.findByName(postRequest.getSubRedditName())
                .orElseThrow(() -> new SubRedditNotFoundException("SubReddit Not Found!"));
        User user = authService.getCurrentUser();
        Post post = postMapper.map(postRequest,subReddit,user);
        post.setUser(user);
        post.setSubReddit(subReddit);
        post.setVoteCount(0);
        postRepository.save(post);
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(p -> postMapper.mapToDto(p))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(long id) {
        return postRepository.findById(id)
                .map(postMapper::mapToDto)
                .orElseThrow(() -> new PostNotFoundException("Post Not Found"));
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getUserPosts(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SubRedditNotFoundException("User Not Found"));
        List<Post> userPosts = postRepository.findByUser(user);
        return userPosts.stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getSubRedditPosts(long subRedditId) {
        SubReddit subReddit = subRedditRepository.findById(subRedditId)
                .orElseThrow(() -> new SubRedditNotFoundException("Not Found"));
        List<Post> posts = postRepository.findAllBySubReddit(subReddit);
        return posts.stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }
}

