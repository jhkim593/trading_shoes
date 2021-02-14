package jpa.project.Service;

import jpa.project.advide.exception.CUserNotFoundException;
//import jpa.project.cache.CacheKey;
import jpa.project.cache.CacheKey;
import jpa.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService{



        private final MemberRepository memberRepository;

        //unless = "#result == null" null이 아닌경우에만 캐싱

    @Cacheable(value = CacheKey.Member, key = "#memberPk", unless = "#result == null")
        public UserDetails loadUserByUsername(String memberPk) {
            return memberRepository.findById(Long.valueOf(memberPk)).orElseThrow(CUserNotFoundException::new);
        }

}
