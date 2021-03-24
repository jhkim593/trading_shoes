package jpa.project.entity;

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

    @Column(length = 100,nullable = false)
    private String writer;

    @Column(nullable = false)
    @Lob
    private String content;

    private int view;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board")
    private List<Board_liked> board_likedList = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<Comment> comments=new ArrayList<>();

    public void addMember(Member member) {
        this.member = member;
        member.getBoards().add(this);
    }



    public static Board createBoard(Member member, String title,String writer,String content){
       Board board =new Board();
       board.title=title;
       board.writer=writer;
       board.content= content;
       board.addMember(member);
       board.view=0;
       return board;
    }



    public void update(String title,String content) {

        if(title!=null){
            this.title = title;
        }
        if(content!=null) {
            this.content = content;
        }

    }
    public void addView(){
        this.view++;
    }

}
