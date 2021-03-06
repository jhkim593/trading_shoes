package jpa.project.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jpa.project.service.CommentService;
import jpa.project.service.ResponseService;
import jpa.project.model.dto.comment.CommentCreateRequestDto;
import jpa.project.model.dto.comment.CommentDto;
import jpa.project.response.CommonResult;
import jpa.project.response.ListResult;
import jpa.project.response.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"8.Comment"})
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

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "댓글 삭제", notes = "회원이 댓글을 삭제한다")
    @DeleteMapping("/comment/{id}")
    public CommonResult deleteComment(@PathVariable("id")Long id){
        commentService.deleteComment(id);
        return responseService.getSuccessResult();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "댓글 수정", notes = "회원이 댓글을 수정한다")
    @PutMapping("/comment/{id}")
    public CommonResult updateComment(@PathVariable("id")Long id,String content){
        commentService.updateComment(id,content);
        return responseService.getSuccessResult();
    }

}
