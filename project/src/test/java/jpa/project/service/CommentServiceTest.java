package jpa.project.service;

import jpa.project.advide.exception.CResourceNotExistException;
import jpa.project.entity.Board;
import jpa.project.entity.Comment;
import jpa.project.entity.DeleteStatus;
import jpa.project.entity.Member;
import jpa.project.model.dto.comment.CommentCreateRequestDto;
import jpa.project.model.dto.comment.CommentDto;
import jpa.project.repository.board.BoardRepository;
import jpa.project.repository.comment.CommentRepository;
import jpa.project.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Transactional
class CommentServiceTest {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    @Autowired CommentService commentService;

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



        //when
        CommentDto dsd = commentService.saveComment(new CommentCreateRequestDto("dsd", fib.getId(), fim.getId(), null));


        //then
        Comment comment1 = commentRepository.findById(dsd.getId()).orElseThrow(CResourceNotExistException::new);

        assertThat(dsd.getId()).isEqualTo(comment1.getId());
        assertThat(dsd.getContent()).isEqualTo(comment1.getContent());
        assertThat(DeleteStatus.NO).isEqualTo(comment1.getDeleteStatus());



    }
    @Test
    public void commentReply()throws Exception{
       //given
        Member fim = em.createQuery("select m from Member m where m.username=:username", Member.class)
                .setParameter("username", "aaa").getSingleResult();
        Board fib = em.createQuery("select b from Board b where b.title=:title", Board.class)
                .setParameter("title", "test").getSingleResult();
       //when
        CommentDto pa = commentService.saveComment(new CommentCreateRequestDto("dsd", fib.getId(), fim.getId(), null));
        CommentDto chi = commentService.saveComment(new CommentCreateRequestDto("dsd", fib.getId(), fim.getId(), pa.getId()));
        em.flush();
        em.clear();

       //then
        Comment parent = commentRepository.findById(pa.getId()).orElseThrow(CResourceNotExistException::new);
        Comment child = commentRepository.findById(chi.getId()).orElseThrow(CResourceNotExistException::new);
        assertThat(child.getParent().getId()).isEqualTo(parent.getId());
        assertThat(parent.getChild().size()).isEqualTo(1);

    }
    @Test
    public void deleteComment()throws Exception{
       //given
        Member fim = em.createQuery("select m from Member m where m.username=:username", Member.class)
                .setParameter("username", "aaa").getSingleResult();
        Board fib = em.createQuery("select b from Board b where b.title=:title", Board.class)
                .setParameter("title", "test").getSingleResult();
       
       //when
        CommentDto pa = commentService.saveComment(new CommentCreateRequestDto("dsd", fib.getId(), fim.getId(), null));

        commentService.deleteComment(pa.getId());


        //then
        assertThatThrownBy(()->commentRepository.findById(pa.getId()).orElseThrow(CResourceNotExistException::new))
                .isInstanceOf(CResourceNotExistException.class);
    }
    
    @Test
    public void testStruct()throws Exception{
       //given
        Member fim = em.createQuery("select m from Member m where m.username=:username", Member.class)
                .setParameter("username", "aaa").getSingleResult();
        Board fib = em.createQuery("select b from Board b where b.title=:title", Board.class)
                .setParameter("title", "test").getSingleResult();
       
       //when
        CommentDto comment1 = commentService.saveComment(new CommentCreateRequestDto("dsd", fib.getId(), fim.getId(), null));
        CommentDto comment2 = commentService.saveComment(new CommentCreateRequestDto("dsd", fib.getId(), fim.getId(), comment1.getId()));
        CommentDto comment3 = commentService.saveComment(new CommentCreateRequestDto("dsd", fib.getId(), fim.getId(), comment2.getId()));
        CommentDto comment4 = commentService.saveComment(new CommentCreateRequestDto("dsd", fib.getId(), fim.getId(), comment1.getId()));
        CommentDto comment5 = commentService.saveComment(new CommentCreateRequestDto("dsd", fib.getId(), fim.getId(), null));

        em.flush();
        em.clear();

        Board board = boardRepository.findById(fib.getId()).orElseThrow(CResourceNotExistException::new);

        List<CommentDto> commentByBoardId = commentService.findCommentByBoardId(board.getId());
        //then
        assertThat(commentByBoardId.size()).isEqualTo(2);
        assertThat(commentByBoardId.get(0).getChildren().size()).isEqualTo(2);
        assertThat(commentByBoardId.get(0).getChildren().get(0).getChildren().size()).isEqualTo(1);



    }
    
    @Test
    public void deleteStruct()throws Exception{
       //given
        Member fim = em.createQuery("select m from Member m where m.username=:username", Member.class)
                .setParameter("username", "aaa").getSingleResult();
        Board fib = em.createQuery("select b from Board b where b.title=:title", Board.class)
                .setParameter("title", "test").getSingleResult();


        //when
        CommentDto comment1 = commentService.saveComment(new CommentCreateRequestDto("dsd", fib.getId(), fim.getId(), null));
        CommentDto comment2 = commentService.saveComment(new CommentCreateRequestDto("dsd", fib.getId(), fim.getId(), comment1.getId()));
        CommentDto comment3 = commentService.saveComment(new CommentCreateRequestDto("dsd", fib.getId(), fim.getId(), comment2.getId()));
        CommentDto comment4 = commentService.saveComment(new CommentCreateRequestDto("dsd", fib.getId(), fim.getId(), comment1.getId()));
        CommentDto comment5 = commentService.saveComment(new CommentCreateRequestDto("dsd", fib.getId(), fim.getId(), null));

        em.flush();
        em.clear();
        commentService.deleteComment(comment1.getId());

        Comment tcomment1 = commentRepository.findById(comment1.getId()).orElseThrow(CResourceNotExistException::new);
        assertThat(tcomment1.getDeleteStatus()).isEqualTo(DeleteStatus.YES);

        //then
//        assertThat(commentByBoardId.get(0).get)


    }
}