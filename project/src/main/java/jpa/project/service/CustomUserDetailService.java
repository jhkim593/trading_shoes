package jpa.project.service;

import jpa.project.advide.exception.CUserNotFoundException;
//import jpa.project.cache.CacheKey;
import jpa.project.cache.CacheKey;
import jpa.project.entity.CustomUserDetails;
import jpa.project.entity.Member;
import jpa.project.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService{



        private final MemberRepository memberRepository;

        //unless = "#result == null" null이 아닌경우에만 캐싱

    @Cacheable(value = CacheKey.MEMBER, key = "#memberPk", unless = "#result == null")
        public UserDetails loadUserByUsername(String memberPk) {
        Member member = memberRepository.findById(Long.valueOf(memberPk)).orElseThrow(CUserNotFoundException::new);
        return new CustomUserDetails(
                member.getUsername(),
                member.getPassword(),
                member.getRoles().stream().map(r -> new SimpleGrantedAuthority(r)).collect(Collectors.toList()));
    }


}

