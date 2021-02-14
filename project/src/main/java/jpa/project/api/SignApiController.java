package jpa.project.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jpa.project.Service.MemberService;
import jpa.project.Service.ResponseService;
import jpa.project.advide.exception.CUsernameSigninFailedException;
import jpa.project.config.security.JwtTokenProvider;
import jpa.project.dto.member.MemberDto;
import jpa.project.response.CommonResult;
import jpa.project.response.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags={"2.Sign"})
@RestController
@RequiredArgsConstructor
public class SignApiController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인",notes = "아이디로 회원 로그인을 한다")
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(@ApiParam(value = "회원ID:이메일",required = true)@RequestParam String username,
                                       @ApiParam(value = "비밀번호",required = true)@RequestParam String password){



        MemberDto memberDto = memberService.findByUsername(username);


        if(!passwordEncoder.matches(password,memberDto.getPassword())){
            throw new CUsernameSigninFailedException();
        }
        return responseService.getSingResult(jwtTokenProvider.createToken(String.valueOf(memberDto.getId()),memberDto.getRoles()));
    }


    @ApiOperation(value = "가입",notes = "회원가입")
    @PostMapping(value = "/signup")
    public CommonResult signup(
//            @RequestBody MemberRegisterRequestDto mrrDto

            @ApiParam(value = "회원 ID:username",required = true)@RequestParam String username ,
                               @ApiParam(value = "비밀번호",required = true)@RequestParam String password,
                               @ApiParam(value = "이메일",required = true)@RequestParam String email){
        memberService.save(username,password,email);
        return responseService.getSuccessResult();

    }
//    @ApiOperation(value = "로그아웃",notes = "로그 아웃을 한다")
//    @PostMapping("/logout")
//    public CommonResult logout(@RequestHeader(value = "X-AUTH-TOKEN")String token){
//        s
//    }
}
