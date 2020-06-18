package io.bitpark.redditclone.repositories;


import io.bitpark.redditclone.models.Post;
import io.bitpark.redditclone.models.User;
import io.bitpark.redditclone.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
