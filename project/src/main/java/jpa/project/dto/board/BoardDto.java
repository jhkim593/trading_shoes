package jpa.project.dto.board;

import jpa.project.entity.Board;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(of = {"id","writer","boardLikeCount"})
public class BoardDto {

    private Long id;

    private String writer;

    private String title;

    private String content;

    private LocalDateTime createdDate;

    private int boardLikeCount=0;

    public BoardDto(Board board) {
        this.id=board.getId();
        this.writer = board.getWriter();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdDate=board.getCreatedDate();

    }
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

    public BoardDto(){

    }

}
