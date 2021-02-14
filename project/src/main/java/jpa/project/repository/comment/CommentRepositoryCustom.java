package jpa.project.repository.comment;

import jpa.project.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepositoryCustom {
   List<Comment>findCommentsByBoardId(Long boardId);
}
