package io.bitpark.redditclone.controllers;

import io.bitpark.redditclone.dto.PostRequest;
import io.bitpark.redditclone.dto.PostResponse;
import io.bitpark.redditclone.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest){
        postService.createPost(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable("id") long id){
        PostResponse post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<PostResponse>> getUserPosts(@PathVariable("userId") long userId){
        List<PostResponse> posts = postService.getUserPosts(userId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/by-subReddit/{subRedditId}")
    public ResponseEntity<List<PostResponse>> getSubRedditPosts(@PathVariable("subRedditId") long subRedditId){
        List<PostResponse> posts = postService.getSubRedditPosts(subRedditId);
        return ResponseEntity.ok(posts);
    }
}
