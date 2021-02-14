package jpa.project.Service;

import jpa.project.advide.exception.CResourceNotExistException;
import jpa.project.advide.exception.CUserNotFoundException;
import jpa.project.dto.comment.CommentCreateRequestDto;
import jpa.project.dto.comment.CommentDto;
import jpa.project.entity.Board;
import jpa.project.entity.Comment;
import jpa.project.entity.Member;
import jpa.project.repository.MemberRepository;
import jpa.project.repository.comment.CommentRepository;
import jpa.project.repository.custom.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public CommentDto saveComment(CommentCreateRequestDto requestDto){
        Optional<Member> findMember = memberRepository.findById(requestDto.getMemberId());
        Member member = findMember.orElseThrow(CUserNotFoundException::new);
        Optional<Board> findBoard = boardRepository.findById(requestDto.getBoardId());
        Board board = findBoard.orElseThrow(CResourceNotExistException::new);

        Comment comment = commentRepository.save(Comment.createComment(requestDto.getContent(), board, member,
                requestDto.getParentId() == null ? null : commentRepository.findById(requestDto.getParentId()).orElseThrow(CResourceNotExistException::new)));

        return CommentDto.createCommentDto(comment);

    }
    public List<CommentDto> findCommentByBoardId(Long boardId){
        Optional<Board> findBoard = boardRepository.findById(boardId);
        findBoard.orElseThrow(CResourceNotExistException::new);
        List<Comment> findComments = commentRepository.findCommentsByBoardId(boardId);
        List<CommentDto>commentDtos=new ArrayList<>();
        Map<Long,CommentDto> commentDtoMap=new HashMap<>();
        findComments.stream().forEach(c->{
            CommentDto commentdto=CommentDto.createCommentDto(c);
            commentDtoMap.put(commentdto.getId(),commentdto);
            if(c.getParent()!=null) commentDtoMap.get(c.getParent().getId()).getChildren().add(commentdto);
            else commentDtos.add(commentdto);

        });

        return commentDtos;

    }
    //캐쉬
//    @Transactional
//    public void deleteComment(Long commentId){
//
//    }

}
