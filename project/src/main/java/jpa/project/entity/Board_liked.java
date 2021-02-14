package jpa.project.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board_liked {
    @Id
    @GeneratedValue

    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    private Board board;

    public Board_liked(Board board,Member member){
        addMember(member);
        addBoard(board);

    }
    public void addMember(Member member){
        this.member=member;
        member.getBoard_likeds().add(this);
    }
    public void addBoard(Board board){
        this.board=board;
        board.getBoard_likedList().add(this);
    }

}
