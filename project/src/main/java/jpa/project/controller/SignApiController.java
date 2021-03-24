package jpa.project.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jpa.project.config.security.JwtTokenProvider;
import jpa.project.model.dto.member.MemberLoginRequestDto;
import jpa.project.model.dto.member.MemberLoginResponseDto;
import jpa.project.model.dto.member.MemberRegisterRequestDto;
import jpa.project.model.dto.member.MemberRegisterResponseDto;
import jpa.project.repository.member.MemberRepository;
import jpa.project.response.CommonResult;
import jpa.project.response.SingleResult;
import jpa.project.service.KakaoService;
import jpa.project.service.ResponseService;
import jpa.project.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags={"2.Sign"})
@RestController
@RequiredArgsConstructor
public class SignApiController {
    private final SignService signService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final KakaoService  kakaoService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "로그인",notes = "아이디로 회원 로그인을 한다")
    @PostMapping(value = "/signin")
    public SingleResult<MemberLoginResponseDto> signin(@ModelAttribute MemberLoginRequestDto memberLoginRequestDto)
    {
        return responseService.getSingResult(signService.signin(memberLoginRequestDto));
    }


    @ApiOperation(value = "가입",notes = "회원가입")
    @PostMapping(value = "/signup")
    public CommonResult signup(

              @ModelAttribute MemberRegisterRequestDto memberRegisterRequestDto
    ){
        signService.signup(memberRegisterRequestDto);
        return responseService.getSuccessResult();

    }


    @ApiOperation(value = "소셜 로그인", notes = "소셜 회원 로그인을 한다.")
    @PostMapping(value = "/signin/{provider}")
    public SingleResult<MemberLoginResponseDto> signinByProvider(
            @ApiParam(value = "서비스 제공자 provider", required = true, defaultValue = "kakao") @PathVariable String provider,
            @ApiParam(value = "소셜 access_token", required = true) @RequestParam String accessToken) {

        return responseService.getSingResult(signService.socialLogin(accessToken,provider));
    }



    @ApiOperation(value = "소셜 계정 가입", notes = "소셜 계정 회원가입을 한다.")
    @PostMapping(value = "/signup/{provider}")
    public SingleResult<MemberRegisterResponseDto> signupProvider(@ApiParam(value = "서비스 제공자 provider", required = true, defaultValue = "kakao") @PathVariable String provider,
                                                                  @ApiParam(value = "소셜 access_token", required = true) @RequestParam String accessToken,
                                                                  @ApiParam(value = "이름", required = true) @RequestParam String name) {

        return responseService.getSingResult( signService.socialSignup(accessToken,provider,name));
    }
    @ApiOperation(value = "로그아웃", notes = "로그아웃을 한다")
    @PostMapping(value = "/logout")
    public CommonResult logout(@RequestHeader(value="X-AUTH-TOKEN") String token) {
        signService.logoutUser(token);
        return responseService.getSuccessResult();
    }
}
