package jpa.project.service;

import jpa.project.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    private MemberRepository memberRepository;


//    @Test
//    public void save()throws Exception{
//        MemberDto memberDto=new MemberDto();
//        memberDto.setPassword("s");
//        memberDto.setEmail("s");
//        memberDto.setUsername("ss");
//
//      Member member=new Member(memberDto);
//
//        Member save = memberRepository.save(member);
//
//        assertThat(save.getId()).isEqualTo(member.getId());
//
//
//    }

}