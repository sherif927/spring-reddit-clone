package io.bitpark.redditclone.controllers;

import io.bitpark.redditclone.dto.CommentDto;
import io.bitpark.redditclone.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto){
        CommentDto createdComment = commentService.createComment(commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentDto>> getCommentsByPost(@PathVariable("postId") long postId){
        List<CommentDto> comments = commentService.getPostComments(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<CommentDto>> getCommentsByUser(@PathVariable("userId") long userId){
        List<CommentDto> comments = commentService.getUserComments(userId);
        return ResponseEntity.ok(comments);
    }
}
