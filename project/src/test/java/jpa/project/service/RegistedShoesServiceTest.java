package jpa.project.service;

import jpa.project.advide.exception.CUserNotFoundException;
import jpa.project.entity.*;
import jpa.project.model.dto.member.MemberRegisterRequestDto;
import jpa.project.repository.member.MemberRepository;
import jpa.project.repository.registedShoes.RegistedShoesRepository;
import jpa.project.repository.shoesInSize.ShoesInSizeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class RegistedShoesServiceTest {
    @Autowired
    RegistedShoesService registedShoesService;
    @Autowired
    ShoesInSizeRepository shoesInSizeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    SignService signService;
    @Autowired
    RegistedShoesRepository registedShoesRepository;

    @Autowired
    EntityManager em;

    @Test

    public void 입찰생성()throws Exception{
       //given

        MemberRegisterRequestDto member=new MemberRegisterRequestDto();
        member.setUsername("s");member.setEmail("ss");member.setPassword("zzz");
        signService.signup(member);
        Member member1 = memberRepository.findByUsername("s").orElseThrow(CUserNotFoundException::new);

        Brand asics = Brand.createBrand("asics", "1111");
        em.persist(asics);
        ShoesSize shoesSize = ShoesSize.createShoesSize("7");
        em.persist(shoesSize);
        ShoesInSize shoesInSize = ShoesInSize.createShoesInSize(shoesSize);
        Shoes zzz = Shoes.createShoes("zzz", asics, shoesInSize);
        em.persist(zzz);
        RegistedShoes registedShoes = RegistedShoes.createRegistedShoes(member1, shoesInSize, 11, TradeStatus.SELL);
        RegistedShoes registedShoes1 = RegistedShoes.createRegistedShoes(member1, shoesInSize, 12, TradeStatus.SELL);
        em.persist(registedShoes);
        em.persist(registedShoes1);




//        //when
       int r= registedShoesRepository.findLowestPriceInShoes(registedShoes.getShoesInSize().getShoes().getId());


        //then
        Assertions.assertThat(r).isEqualTo(11);
    }


}