package io.bitpark.redditclone.dto;

import io.bitpark.redditclone.models.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto {
    private VoteType voteType;
    private long postId;
}
