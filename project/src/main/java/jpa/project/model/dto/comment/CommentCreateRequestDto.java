package jpa.project.model.dto.comment;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentCreateRequestDto {
    private String content;
    private Long boardId;
    private Long memberId;
    private Long parentId;



}
