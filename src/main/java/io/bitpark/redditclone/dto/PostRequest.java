package io.bitpark.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private long postId;
    private String subRedditName;
    private String postName;
    private String url;
    private String description;
}
