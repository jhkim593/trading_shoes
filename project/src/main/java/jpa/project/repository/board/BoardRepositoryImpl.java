package jpa.project.repository.board;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpa.project.dto.board.BoardDto;
import jpa.project.entity.Board;
import jpa.project.repository.search.BoardSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static jpa.project.entity.QBoard.*;
import static jpa.project.entity.QBoard_liked.*;
import static jpa.project.entity.QMember.*;
import static org.springframework.util.StringUtils.*;

public class BoardRepositoryImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    @Autowired
    private EntityManager em;

    public BoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardDto> search(BoardSearch boardSearch, Pageable pageable) {

        List<BoardDto> fetch = queryFactory.select(Projections.bean(BoardDto.class,
                 board.id
                , board.writer
                , board.title
                , board.content
                , board.createdDate
                , member.username
                 ,board_liked.count().as("boardLikeCount")
//
        )).from(board)
                .join(board.member, member)
                .join(board.board_likedList, board_liked)
                .where(
                        contentEq(boardSearch.getContent())
                        , titleEq(boardSearch.getTitle())
                ).
                        offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Board> count = queryFactory.selectFrom(board)
                .join(board.member, member).fetchJoin()
                .where(
                        contentEq(boardSearch.getContent())
                        , titleEq(boardSearch.getTitle())
                );

        return PageableExecutionUtils.getPage(fetch,pageable,()->count.fetchCount());

    }


//    @Override
//    public List<BoardDto> searchTest(BoardSearch boardSearch){
//        return  em.createQuery("select new jpa.project.dto.BoardDto(b.id,b.writer,b.title,b.content,b.createdDate,count(l)) " +
//               "from Board b join b.board_likedList l",BoardDto.class).getResultList();
//
//
//    }



    private BooleanExpression contentEq(String content) {
        return hasText(content)? board.content.eq(content):null;

    }


    private BooleanExpression titleEq(String title) {
        return hasText(title)? board.title.eq(title):null;
    }


}
