package jpa.project.api;

import io.swagger.annotations.*;
import jpa.project.Service.MemberService;
import jpa.project.Service.ResponseService;
import jpa.project.dto.member.MemberDto;
import jpa.project.dto.member.MemberRegisterRequestDto;
import jpa.project.response.CommonResult;
import jpa.project.response.ListResult;
import jpa.project.response.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @GetMapping("/members")
    public ListResult<MemberDto> findAll(){
        return  responseService.getListResult(memberService.findAll());

    }
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "0access-token",required = true,dataType = "String",paramType = "header")
//    })
//    @ApiOperation(value="회원입력", notes = "회원을 입력한다.")
//    @PostMapping("/member")
//    public SingelResult<MemberDto> save(@RequestBody @Valid MemberRegisterRequestDto mrrDto){
//       return responseService.getSingResult(memberService.join(mrrDto));
//
//    }
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
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
