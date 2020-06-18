package io.bitpark.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private long id;
    private String postName;
    private String url;
    private String description;
    private String username;
    private String subRedditName;
    private int voteCount;
    private int commentCount;
    private String duration;
}
