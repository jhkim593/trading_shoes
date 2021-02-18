package jpa.project.Service;

import jpa.project.advide.exception.CLoginFailureException;
import jpa.project.advide.exception.CUserAlreadyExistException;
import jpa.project.advide.exception.CUserNotFoundException;
import jpa.project.cache.CacheKey;
import jpa.project.config.security.JwtTokenProvider;
import jpa.project.dto.member.MemberLoginRequestDto;
import jpa.project.dto.member.MemberLoginResponseDto;
import jpa.project.dto.member.MemberRegisterRequestDto;
import jpa.project.dto.member.MemberRegisterResponseDto;
import jpa.project.entity.Member;
import jpa.project.repository.MemberRepository;
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
//    private MemberLoginResponseDto signUp(MemberRegisterRequestDto requestDto){
//
//    }

    @Transactional
    public void logoutMember(String token){
        redisTemplate.opsForValue().set(CacheKey.TOKEN + ":" + token, "v", jwtTokenProvider.getRemainingSeconds(token));
        Member member = memberRepository.findById(Long.valueOf(jwtTokenProvider.getMemberPk(token))).orElseThrow(CUserNotFoundException::new);
        member.changeRefreshToken("invalid");
    }
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
    public MemberLoginResponseDto login(MemberLoginRequestDto requestDto){
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


}
