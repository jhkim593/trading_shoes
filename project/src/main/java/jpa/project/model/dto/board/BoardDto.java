package jpa.project.model.dto.board;

import jpa.project.entity.Board;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","writer","boardLikeCount"})
public class BoardDto implements Serializable {

    private Long id;

    private String writer;

    private String title;

    private String content;

    private LocalDateTime createdDate;


    public static BoardDto createBoardDto(Board board){
        return new BoardDto(board.getId(), board.getWriter(), board.getTitle(), board.getContent(), board.getCreatedDate()
        );
    }

}
