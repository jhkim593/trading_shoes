package jpa.project.repository.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jpa.project.entity.Comment;
import jpa.project.entity.QComment;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

import static jpa.project.entity.QComment.*;


public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Autowired
    private EntityManager em;

    public CommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Comment> findCommentsByBoardId(Long boardId) {
      return queryFactory.selectFrom(comment).leftJoin(comment.parent)
               .fetchJoin()
               .where(comment.board.id.eq(boardId))
               .orderBy(
                       comment.parent.id.asc().nullsFirst(),
                       comment.createdDate.asc()
               ).fetch();

    }
}
