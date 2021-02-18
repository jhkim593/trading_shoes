package jpa.project.entity;

import jpa.project.dto.board.BoardCreateRequestDto;
import jpa.project.dto.board.BoardDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 100)
    private String writer;
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board")
    private List<Board_liked> board_likedList = new ArrayList<>();

    @OneToMany

    public void addMember(Member member) {
        this.member = member;
        member.getBoards().add(this);
    }



    public static Board addBoard(Member member, BoardCreateRequestDto bcrDto){
       Board board =new Board();
       board.update(bcrDto,member);
       board.addMember(member);
       return board;
    }



    public void update(BoardCreateRequestDto bcrDto) {

        if(bcrDto.getTitle()!=null){
            this.title = bcrDto.getTitle();
        }
        if(bcrDto.getContent()!=null) {
            this.content = bcrDto.getContent();
        }

    }
    public void update(BoardCreateRequestDto bcrDto,Member member) {

        this.writer=member.getUsername();
        this.content = bcrDto.getContent();
        this.title = bcrDto.getTitle();

    }
}
