package jpa.project.controller;

import io.swagger.annotations.*;
import jpa.project.model.dto.member.MemberDto;
import jpa.project.model.dto.member.MemberRegisterRequestDto;
import jpa.project.response.CommonResult;
import jpa.project.response.ListResult;
import jpa.project.response.SingleResult;
import jpa.project.service.MemberService;
import jpa.project.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags={"1.Member"})
@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;
    private final ResponseService responseService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value = "전체회원조회", notes = "모든 회원을 조회한다.")
    @GetMapping("/admin/members")
    public ListResult<MemberDto> findAll(){
        return  responseService.getListResult(memberService.findAll());

    }

@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
})
    @ApiOperation(value = "회원 수정" ,notes = "회원을 수정한다")
    @PutMapping("/member/{id}")
    public SingleResult<MemberDto> update(@RequestBody MemberRegisterRequestDto mrrDto, @PathVariable("id")Long id){
        return responseService.getSingResult(memberService.update(mrrDto,id));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value = "회원단건조회" ,notes = "한명의 회원을 조회한다")
    @GetMapping("/member/{id}")
    public SingleResult<MemberDto> findById(@ApiParam(value = "회원번호",required = true)@PathVariable("id")Long id
                                              , @ApiParam(value = "언어",defaultValue = "ko")@RequestParam String lang){

        return responseService.getSingResult(memberService.find(id));
    }
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })

    @ApiOperation(value = "회원삭제",notes = "회원을 삭제한다")
    @DeleteMapping("/member/{id}")
    public CommonResult delete(@ApiParam(value ="회원번호",required = true)@PathVariable("id")Long id){
        memberService.delete(id);
        return responseService.getSuccessResult();
    }


}
