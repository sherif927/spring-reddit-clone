package io.bitpark.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubRedditDto {
    private long id;
    private String name;
    private String description;
    private int numberOfPosts;
}
