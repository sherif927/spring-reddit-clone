package io.bitpark.redditclone.controllers;

import io.bitpark.redditclone.dto.SubRedditDto;
import io.bitpark.redditclone.services.SubRedditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subReddit")
@AllArgsConstructor
@Slf4j
public class SubRedditController {

    private final SubRedditService subRedditService;

    @PostMapping
    public ResponseEntity<SubRedditDto> createSubReddit(@RequestBody SubRedditDto subRedditDto){
        return new ResponseEntity<>(subRedditService.createSubReddit(subRedditDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SubRedditDto>> getAllSubReddits(){
        List<SubRedditDto> subReddits = subRedditService.getAllSubReddits();
        return ResponseEntity.ok(subReddits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubRedditDto> getSubRedditById(@PathVariable("id") long id){
        SubRedditDto subReddit = subRedditService.getById(id);
        return ResponseEntity.ok(subReddit);
    }

}
