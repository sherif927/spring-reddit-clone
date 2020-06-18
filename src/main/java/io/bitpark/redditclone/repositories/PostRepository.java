package io.bitpark.redditclone.repositories;

import io.bitpark.redditclone.models.Post;
import io.bitpark.redditclone.models.SubReddit;
import io.bitpark.redditclone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubReddit(SubReddit subreddit);
    List<Post> findByUser(User user);
}
