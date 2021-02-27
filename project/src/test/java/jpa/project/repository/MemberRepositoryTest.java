package jpa.project.repository;

import jpa.project.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager em;

    @Test
    public void 회원찾기()throws Exception{
//       //given
//        MemberDto memberDto=new MemberDto();
//        memberDto.setName("dd");
//        Member member=new Member(memberDto);
//        System.out.println("=============");
//        System.out.println(member.getName());
//
//       //when
//        Member save = memberRepository.save(member);

        //then
//        List<Member> member1 = memberRepository.findByName(member.getName());
//
//        Assertions.assertThat("dd").isEqualTo(member1.get(0).getName());
    }
}