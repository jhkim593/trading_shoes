package jpa.project.model.dto.comment;

import jpa.project.entity.Comment;
import jpa.project.entity.DeleteStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private Long memberId;
    private String username;
    private List<CommentDto>children=new ArrayList<>();

    public CommentDto(Long id, String content, Long memberId, String username) {
        this.id = id;
        this.content = content;
        this.memberId = memberId;
        this.username = username;
    }
    public static CommentDto createCommentDto(Comment comment){
        return comment.getDeleteStatus()== DeleteStatus.YES?
                new CommentDto(comment.getId(),"삭제되었습니다",null,null):
                new CommentDto(comment.getId(), comment.getContent(),comment.getMember().getId(),comment.getMember().getUsername());
    }
}
