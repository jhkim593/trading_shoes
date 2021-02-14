package jpa.project.repository.comment;

import jpa.project.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> ,CommentRepositoryCustom{
}
