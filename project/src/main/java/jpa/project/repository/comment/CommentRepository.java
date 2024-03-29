package jpa.project.repository.comment;

import jpa.project.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> ,CommentRepositoryCustom{
    @Query("select c from Comment c left join fetch c.parent where c.id=:id")
    Optional<Comment>findCommentWithParent(@Param("id")Long id);
}
