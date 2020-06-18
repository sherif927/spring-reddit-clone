package io.bitpark.redditclone.controllers;

import io.bitpark.redditclone.dto.VoteDto;
import io.bitpark.redditclone.services.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> performVote(@RequestBody VoteDto voteDto){
        voteService.performVote(voteDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
