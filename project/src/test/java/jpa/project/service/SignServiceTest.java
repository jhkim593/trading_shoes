package jpa.project.service;

import jpa.project.advide.exception.CUserAlreadyExistException;
import jpa.project.advide.exception.CUserNotFoundException;
import jpa.project.config.security.JwtTokenProvider;
import jpa.project.entity.Member;
import jpa.project.model.dto.member.MemberLoginRequestDto;
import jpa.project.model.dto.member.MemberLoginResponseDto;
import jpa.project.model.dto.member.MemberRegisterRequestDto;
import jpa.project.model.dto.member.MemberRegisterResponseDto;
import jpa.project.repository.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class SignServiceTest {
    @Autowired
    SignService signService;
    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    public void 회원가입()throws Exception{
       //given
        MemberRegisterRequestDto member=new MemberRegisterRequestDto("s","ss","zzz");
       //when
        MemberRegisterResponseDto signup = signService.signup(member);
        em.flush();
        em.clear();
        Member member1 = memberRepository.findById(signup.getId()).orElseThrow(CUserNotFoundException::new);
        //then
        assertThat(member1.getId()).isEqualTo(signup.getId());

    }
    @Test
    public void 로그인()throws Exception{
       //given

        MemberRegisterRequestDto member=new MemberRegisterRequestDto("s","ss","zzz");

        MemberLoginRequestDto login=new MemberLoginRequestDto("s","ss");

        MemberRegisterResponseDto signup = signService.signup(member);

        //when
        MemberLoginResponseDto signin = signService.signin(login);
        em.flush();
        em.clear();
        Member member1 = memberRepository.findByUsername("s").orElseThrow(CUserNotFoundException::new);



        //then
        assertThat(jwtTokenProvider.validateToken(signin.getToken())).isTrue();
        assertThat(jwtTokenProvider.validateToken(member1.getRefreshToken())).isTrue() ;
        assertThat(jwtTokenProvider.validateToken(signin.getRefreshToken())).isTrue();
    }

    @Test
    public void 중복회원검사()throws Exception{
       //given
        MemberRegisterRequestDto member=new MemberRegisterRequestDto("s","ss","zzz");
        MemberRegisterRequestDto member2=new MemberRegisterRequestDto("s","sss","zzzz");

        //when
        MemberRegisterResponseDto signup = signService.signup(member);



       //then
        Assertions.assertThatThrownBy(()->  signService.signup(member2)).isInstanceOf(CUserAlreadyExistException.class);
    }

    @Test
    public void refreshToken()throws Exception{
       //given
        jwtTokenProvider.setTokenValidMillisecond(-1L);
        MemberRegisterRequestDto member=new MemberRegisterRequestDto("s","ss","zzz");

        MemberLoginRequestDto login=new MemberLoginRequestDto("s","ss");

        //when
        MemberRegisterResponseDto signup = signService.signup(member);
        MemberLoginResponseDto signin = signService.signin(login);

        jwtTokenProvider.setTokenValidMillisecond(3600L);
        MemberLoginResponseDto memberLoginResponseDto = signService.refreshToken(signin.getToken(), signin.getRefreshToken());

        //then
        Member member1 = memberRepository.findByUsername("s").orElseThrow(CUserNotFoundException::new);
        assertThat(jwtTokenProvider.validateToken(member1.getRefreshToken())).isTrue();
        assertThat(jwtTokenProvider.validateToken(memberLoginResponseDto.getToken())).isTrue();
    }
    @Test
    public void logout()throws Exception{
       //given
        MemberRegisterRequestDto member=new MemberRegisterRequestDto("s","ss","zzz");

        MemberLoginRequestDto login=new MemberLoginRequestDto("s","ss");

        MemberRegisterResponseDto signup = signService.signup(member);
        //when
        MemberLoginResponseDto signin = signService.signin(login);
       signService.logoutMember(signin.getToken());


       //then
        Member member1 = memberRepository.findByUsername(member.getUsername()).orElseThrow(CUserNotFoundException::new);
        assertThat(jwtTokenProvider.validateToken(member1.getRefreshToken())).isFalse();
        assertThat(jwtTokenProvider.validateToken(signin.getToken())).isFalse();
      assertThat(jwtTokenProvider.isLoggedOut(signin.getToken())).isTrue();
    }
    @Test
    public void refreshTokenWithLogout()throws Exception{
        MemberRegisterRequestDto member=new MemberRegisterRequestDto("s","ss","zzz");

        MemberLoginRequestDto login=new MemberLoginRequestDto("s","ss");

        MemberRegisterResponseDto signup = signService.signup(member);
        //when
        MemberLoginResponseDto signin = signService.signin(login);
        signService.logoutMember(signin.getToken());
       //then
        assertThatThrownBy(()->signService.refreshToken(signin.getToken(),signin.getRefreshToken())).isInstanceOf(AccessDeniedException.class);
    }
    @Test
    public void refreshTokenInvalidateExpiration()throws Exception{
        //given
        jwtTokenProvider.setRefreshTokenValidMillisecond(-1L);
        MemberRegisterRequestDto member=new MemberRegisterRequestDto("s","ss","zzz");

        MemberLoginRequestDto login=new MemberLoginRequestDto("s","ss");

        MemberRegisterResponseDto signup = signService.signup(member);
        //when
        MemberLoginResponseDto signin = signService.signin(login);
       
       //then
        assertThatThrownBy(()->signService.refreshToken(signin.getToken(),signin.getRefreshToken())).isInstanceOf(AccessDeniedException.class);
    }
    @Test
    public void refreshTokenWithValidToken()throws Exception{
        //given
        jwtTokenProvider.setRefreshTokenValidMillisecond(-1L);
        MemberRegisterRequestDto member=new MemberRegisterRequestDto("s","ss","zzz");

        MemberLoginRequestDto login=new MemberLoginRequestDto("s","ss");

        MemberRegisterResponseDto signup = signService.signup(member);
        //when
        MemberLoginResponseDto signin = signService.signin(login);

       
       //then
        assertThatThrownBy(()->signService.refreshToken(signin.getToken(),signin.getRefreshToken())).isInstanceOf(AccessDeniedException.class);
    }
    @Test
    public void refreshTokenWithInValidToken()throws Exception{

        MemberRegisterRequestDto member=new MemberRegisterRequestDto("s","ss","zzz");

        MemberLoginRequestDto login=new MemberLoginRequestDto("s","ss");

        MemberRegisterResponseDto signup = signService.signup(member);
        //when
        MemberLoginResponseDto signin = signService.signin(login);
       //then
        assertThatThrownBy(()->signService.refreshToken("sss",signin.getRefreshToken())).isInstanceOf(AccessDeniedException.class);

    }
    @Test
    public void refreshTokenInvalidRefreshToekn()throws Exception{
        //given
        jwtTokenProvider.setRefreshTokenValidMillisecond(-1L);
        MemberRegisterRequestDto member=new MemberRegisterRequestDto("s","ss","zzz");

        MemberLoginRequestDto login=new MemberLoginRequestDto("s","ss");

        MemberRegisterResponseDto signup = signService.signup(member);
        //when
        MemberLoginResponseDto signin = signService.signin(login);


        //then
        assertThatThrownBy(()->signService.refreshToken(signin.getToken(),"sss")).isInstanceOf(AccessDeniedException.class);


    }

}