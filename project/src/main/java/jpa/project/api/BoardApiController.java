package jpa.project.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jpa.project.Service.ResponseService;
import jpa.project.dto.board.BoardCreateRequestDto;
import jpa.project.dto.board.BoardDto;
import jpa.project.Service.BoardService;
import jpa.project.repository.search.BoardSearch;
import jpa.project.response.CommonResult;
import jpa.project.response.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags={"3.Board"})
@RestController
@RequiredArgsConstructor

public class BoardApiController {
    private  final BoardService boardService;

    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value = "게시판 작성", notes = "게시판을 작성한다")
    @PostMapping("/board")
    public SingleResult<BoardDto> write(@Valid @ModelAttribute BoardCreateRequestDto bcrDto ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
       return  responseService.getSingResult(boardService.save(uid,bcrDto));
    }
    @ApiOperation(value = "게시판 글 상세",notes = "게시판 상세정보 조회")
    @GetMapping("/board/{id}")
    public SingleResult<BoardDto>detail(@PathVariable("id")Long id){
        return responseService.getSingResult(boardService.find(id));
    }

    @ApiOperation(value = "게시판 조회", notes = "게시판 목록을 조회한다.")
    @GetMapping("/boards")
    public SingleResult<Page<BoardDto>> search(BoardSearch search, Pageable pageable){
        return responseService.getSingResult(boardService.search(search,pageable));

    }
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value = "게시판 수정", notes = "게시판을 수정한다")
    @PutMapping("/boards/{id}")
    public SingleResult<BoardDto> update(@PathVariable("id")Long id, @Valid @ModelAttribute BoardCreateRequestDto bcrDto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return responseService.getSingResult(boardService.update(id,username,bcrDto));
    }
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value = "게시판 삭제", notes = "게시판을 삭제한다")
    @DeleteMapping("/boards/{id}")
    public CommonResult delete(@PathVariable("id")Long id ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boardService.delete(id,username);
        return responseService.getSuccessResult();
    }

}
