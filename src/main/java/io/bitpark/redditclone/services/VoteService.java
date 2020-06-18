package io.bitpark.redditclone.services;

import io.bitpark.redditclone.dto.VoteDto;
import io.bitpark.redditclone.exceptions.PostNotFoundException;
import io.bitpark.redditclone.exceptions.SpringRedditException;
import io.bitpark.redditclone.models.Post;
import io.bitpark.redditclone.models.User;
import io.bitpark.redditclone.models.Vote;
import io.bitpark.redditclone.repositories.PostRepository;
import io.bitpark.redditclone.repositories.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static io.bitpark.redditclone.models.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    public void performVote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));

        User currentUser = authService.getCurrentUser();

        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,currentUser );
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already "
                    + voteDto.getVoteType() + "'d for this post");
        }

        post.setVoteCount((UPVOTE.equals(voteDto.getVoteType())) ?
                post.getVoteCount() + 1 :
                post.getVoteCount() - 1);


        voteRepository.save(mapToVote(voteDto, post,currentUser));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post,User user) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(user)
                .build();
    }
}
