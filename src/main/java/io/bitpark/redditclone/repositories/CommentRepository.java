package io.bitpark.redditclone.repositories;

import io.bitpark.redditclone.models.Comment;
import io.bitpark.redditclone.models.Post;
import io.bitpark.redditclone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    List<Comment> findAllByUser(User user);
}