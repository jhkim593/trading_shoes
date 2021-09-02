package jpa.project.repository.comment;

import jpa.project.entity.Board;
import jpa.project.entity.Comment;
import jpa.project.entity.Member;
import jpa.project.repository.board.BoardRepository;
import jpa.project.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

@DataJpaTest

class CommentRepositoryTest {
    @Autowired CommentRepository commentRepository;
    @Autowired BoardRepository boardRepository;
    @Autowired EntityManager em;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    public void beforeEach(){
        Member member = memberRepository.save(Member.builder().email("ss").username("aaa").password("1111").build());
        Board board=Board.createBoard(member,"test","test","ttt");
        em.persist(board);

    }

    @Test
    public void addComment()throws Exception{
       //given
        Member fim = em.createQuery("select m from Member m where m.username=:username", Member.class)
                .setParameter("username", "aaa").getSingleResult();
        Board fib = em.createQuery("select b from Board b where b.title=:title", Board.class)
                .setParameter("title", "test").getSingleResult();
        Comment comment= Comment.createComment("aaa",fib,fim,null);
        em.persist(comment);
        em.flush();
        em.clear();

       //when


       //then
    }
}