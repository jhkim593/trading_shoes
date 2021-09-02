package jpa.project.entity;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class
Comment extends BaseTimeEntity{
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Lob
    private String content;

    @Enumerated(value = EnumType.STRING)
    private DeleteStatus deleteStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment>child=new ArrayList<>();

    public static Comment createComment(String content,Board board,Member member,Comment parent){
        Comment comment=new Comment();
        comment.content=content;
        comment.board=board;
        comment.deleteStatus=DeleteStatus.NO;
        comment.parent=parent;
        comment.member=member;
        return comment;
    }

    public void deleteComment(DeleteStatus deleteStatus){
        this.deleteStatus=deleteStatus;
    }
    public void updateComment(String content){
        this.content=content;
    }


}
