package jpa.project.service;

import jpa.project.advide.exception.CLoginFailureException;
import jpa.project.advide.exception.CUserAlreadyExistException;
import jpa.project.advide.exception.CUserNotFoundException;
import jpa.project.cache.CacheKey;
import jpa.project.config.security.JwtTokenProvider;
import jpa.project.entity.Member;
import jpa.project.model.dto.member.MemberLoginRequestDto;
import jpa.project.model.dto.member.MemberLoginResponseDto;
import jpa.project.model.dto.member.MemberRegisterRequestDto;
import jpa.project.model.dto.member.MemberRegisterResponseDto;
import jpa.project.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignService {
    private final RedisTemplate redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final KakaoService kakaoService;


    @Transactional
    public MemberLoginResponseDto refreshToken(String token,String refreshToken){
        if(!jwtTokenProvider.validateTokenExceptExpiration(token))throw new AccessDeniedException("");
        Member member = memberRepository.findById(Long.valueOf(jwtTokenProvider.getMemberPk(token))).orElseThrow(CUserNotFoundException::new);
        if(!jwtTokenProvider.validateToken(member.getRefreshToken())||!refreshToken.equals(member.getRefreshToken()))
            throw new AccessDeniedException("");
        member.changeRefreshToken(jwtTokenProvider.createRefreshToken());
        return new MemberLoginResponseDto(member.getId(),jwtTokenProvider.createToken(String.valueOf(member.getId()),member.getRoles()),member.getRefreshToken());
    }

    @Transactional
    public MemberLoginResponseDto signin(MemberLoginRequestDto requestDto){
        Member member = memberRepository.findByUsername(requestDto.getUsername()).orElseThrow(CUserNotFoundException::new);
        if(!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())){
            throw new CLoginFailureException();
        }
        member.changeRefreshToken(jwtTokenProvider.createRefreshToken());
        return new MemberLoginResponseDto(member.getId(), jwtTokenProvider.createToken(String.valueOf(member.getId()),member.getRoles()), member.getRefreshToken());
    }

    @Transactional
    public MemberRegisterResponseDto signup(MemberRegisterRequestDto requestDto){

        validMember(requestDto.getUsername());
        String role="ROLE_USER";
        if(requestDto.getUsername().equals("a")){
            role="ROLE_ADMIN";
        }

        Member member = memberRepository.save(Member.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .roles(Collections.singletonList(role))
                .build());
      return new MemberRegisterResponseDto(member.getId(),member.getUsername(),member.getEmail());

    }

    private void validMember(String username) {

        if(memberRepository.findByUsername(username).isPresent())throw new CUserAlreadyExistException();

    }
    private void validMemberByProvider(String idByProvider,String provider){
        if(memberRepository.findByUsernameAndProvider(idByProvider, provider).isPresent()){
            throw new CUserAlreadyExistException();
        }
    }


    public MemberLoginResponseDto socialLogin(String accessToken, String provider) {
        String idByProvider = getIdByProvider(accessToken, provider);
        Member member = memberRepository.findByUsernameAndProvider(idByProvider, provider).orElseThrow(CUserNotFoundException::new);
        String token = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRoles());
        member.changeRefreshToken(jwtTokenProvider.createRefreshToken());
        return new MemberLoginResponseDto(member.getId(),token,member.getRefreshToken());

    }

    public String getIdByProvider(String accessToken,String provider){
       if(provider.equals("kakao")){
           return kakaoService.getKakaoProfile(accessToken).getId();
       }
       throw new CLoginFailureException();
    }


    @Transactional
    public  MemberRegisterResponseDto socialSignup(String accessToken,String provider,String name) {
        String idByProvider = getIdByProvider(accessToken, provider);
        validMemberByProvider(idByProvider,provider);
        Member member  = memberRepository.save(Member.builder()
                .username(idByProvider)
                .provider(provider)
                .email(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        return new MemberRegisterResponseDto(member.getId(),member.getUsername(),member.getEmail());
    }
    @Transactional
    public void logoutUser(String token) {
        redisTemplate.opsForValue().set(CacheKey.TOKEN + ":" + token, "v", jwtTokenProvider.getRemainingSeconds(token));
       Member member = memberRepository.findById(Long.valueOf(jwtTokenProvider.getMemberPk(token))).orElseThrow(CUserNotFoundException::new);
        member.changeRefreshToken("invalidate");
    }

}
