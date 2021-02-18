package jpa.project.dto.board;

import jpa.project.entity.Board;
import lombok.*;

import javax.jdo.annotations.Serialized;
import javax.validation.constraints.NotEmpty;;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","writer","boardLikeCount"})
public class BoardDto implements Serializable {

    private Long id;

    private String writer;

    private String title;

    private String content;

    private LocalDateTime createdDate;

//    private int boardLikeCount=0;

//    public BoardDto(Board board) {
//        this.id=board.getId();
//        this.writer = board.getWriter();
//        this.title = board.getTitle();
//        this.content = board.getContent();
//        this.createdDate=board.getCreatedDate();
//
//    }
//    public BoardDto(Long id,String writer, String title, String content,LocalDateTime createdDate,Long boardLikeCount) {
//        this.id=id;
//        this.writer = writer;
//        this.title = title;
//        this.content = content;
//        this.createdDate=createdDate;
//        if(boardLikeCount!=null) {
//            this.boardLikeCount = boardLikeCount.intValue();
//        }
//    }

//    public BoardDto(){
//
//    }
    public static BoardDto createBoardDto(Board board){
        return new BoardDto(board.getId(), board.getWriter(), board.getTitle(), board.getContent(), board.getCreatedDate()
        );
    }

}
