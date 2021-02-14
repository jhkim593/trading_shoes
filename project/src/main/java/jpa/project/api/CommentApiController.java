package jpa.project.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jpa.project.Service.CommentService;
import jpa.project.Service.ResponseService;
import jpa.project.dto.comment.CommentCreateRequestDto;
import jpa.project.dto.comment.CommentDto;
import jpa.project.dto.member.MemberDto;
import jpa.project.entity.Comment;
import jpa.project.repository.comment.CommentRepository;
import jpa.project.response.ListResult;
import jpa.project.response.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"7.Comment"})
@RestController
@RequiredArgsConstructor
public class CommentApiController {
    private final CommentService commentService;
    private final ResponseService responseService;

    @ApiOperation(value = "게시판 댓글조회", notes = "게시판의 댓글들을 조회한다")
    @GetMapping("/comment/{id}")
    public ListResult<CommentDto>findCommentByBoard(@PathVariable("id")Long id){
        return responseService.getListResult(commentService.findCommentByBoardId(id));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "댓글 작성", notes = "회원이 댓글을 작성한다")
    @PostMapping("/comment")
    public SingleResult<CommentDto>saveComment(@ModelAttribute CommentCreateRequestDto ccrDto){
       return responseService.getSingResult(commentService.saveComment(ccrDto));
    }

}
