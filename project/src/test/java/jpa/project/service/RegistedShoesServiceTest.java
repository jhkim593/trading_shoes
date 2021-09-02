package jpa.project.service;

import jpa.project.advide.exception.CResourceNotExistException;
import jpa.project.advide.exception.CUserNotFoundException;
import jpa.project.entity.*;
import jpa.project.model.dto.member.MemberRegisterRequestDto;
import jpa.project.model.dto.registedShoes.RegistedShoesDto;
import jpa.project.repository.member.MemberRepository;
import jpa.project.repository.registedShoes.RegistedShoesRepository;
import jpa.project.repository.shoesInSize.ShoesInSizeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    public void 입찰가격변경()throws Exception{
       //given

        MemberRegisterRequestDto member=new MemberRegisterRequestDto("s","ss","zzz");

        signService.signup(member);
        Member member1 = memberRepository.findByUsername("s").orElseThrow(CUserNotFoundException::new);

        Brand asics = Brand.createBrand("asics", "1111");
        em.persist(asics);
        ShoesSize shoesSize = ShoesSize.createShoesSize("7");
        em.persist(shoesSize);
        ShoesInSize shoesInSize = ShoesInSize.createShoesInSize(shoesSize);
        Shoes zzz = Shoes.createShoes("zzz", asics, shoesInSize);
        em.persist(zzz);

        RegistedShoesDto registedShoesDto = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 9, TradeStatus.SELL);
        RegistedShoesDto registedShoesDto1 = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 11, TradeStatus.SELL);
        RegistedShoesDto registedShoesDto2 = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 10, TradeStatus.SELL);

        em.flush();
        em.clear();
        RegistedShoes registedShoes = registedShoesRepository.findById(registedShoesDto.getId()).orElseThrow(CResourceNotExistException::new);
        

//        //when

        registedShoesService.update(registedShoes.getId(),member1.getUsername(),5);


        //then
        assertThat(registedShoes.getPrice()).isEqualTo(5);
        assertThat(registedShoes.getShoesInSize().getShoes().getPrice()).isEqualTo(5);
//     
    }
    @Test
    public void 입찰정보조회()throws Exception{
        MemberRegisterRequestDto member=new MemberRegisterRequestDto("s","ss","zzz");

        signService.signup(member);
        Member member1 = memberRepository.findByUsername("s").orElseThrow(CUserNotFoundException::new);

        Brand asics = Brand.createBrand("asics", "1111");
        em.persist(asics);
        ShoesSize shoesSize = ShoesSize.createShoesSize("7");
        em.persist(shoesSize);
        ShoesInSize shoesInSize = ShoesInSize.createShoesInSize(shoesSize);
        Shoes zzz = Shoes.createShoes("zzz", asics, shoesInSize);
        em.persist(zzz);

        RegistedShoesDto registedShoesDto = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 9, TradeStatus.SELL);
        RegistedShoesDto registedShoesDto1 = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 11, TradeStatus.SELL);
        RegistedShoesDto registedShoesDto2 = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 10, TradeStatus.BUY);

        em.flush();
        em.clear();
        //when
        Slice<RegistedShoesDto> registedShoesByTradeStatus = registedShoesService.findRegistedShoesByTradeStatus(member1.getId(), null, TradeStatus.BUY, 1);
        Slice<RegistedShoesDto> registedShoesByTradeStatusS = registedShoesService.findRegistedShoesByTradeStatus(member1.getId(), null, TradeStatus.SELL, 1);

        Long last = registedShoesByTradeStatusS.getContent().get(0).getId();
        Slice<RegistedShoesDto> registedShoesByTradeStatus1 = registedShoesService.findRegistedShoesByTradeStatus(member1.getId(), last, TradeStatus.SELL, 1);



        //then
        assertThat(registedShoesByTradeStatus.hasNext()).isFalse();
        assertThat(registedShoesByTradeStatus.getSize()).isEqualTo(1);
        assertThat(registedShoesByTradeStatusS.hasNext()).isTrue() ;

        assertThat(registedShoesByTradeStatusS.getSize()).isEqualTo(1);
        assertThat(registedShoesByTradeStatus1.getSize()).isEqualTo(1);
        assertThat(registedShoesByTradeStatus1.hasNext()).isFalse();


    }

    @Test
    public void 입찰삭제()throws Exception{
        MemberRegisterRequestDto member=new MemberRegisterRequestDto("s","ss","zzz");

        signService.signup(member);
        Member member1 = memberRepository.findByUsername("s").orElseThrow(CUserNotFoundException::new);

        Brand asics = Brand.createBrand("asics", "1111");
        em.persist(asics);
        ShoesSize shoesSize = ShoesSize.createShoesSize("7");
        em.persist(shoesSize);
        ShoesInSize shoesInSize = ShoesInSize.createShoesInSize(shoesSize);
        Shoes zzz = Shoes.createShoes("zzz", asics, shoesInSize);
        em.persist(zzz);

        RegistedShoesDto registedShoesDto = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 9, TradeStatus.SELL);
        RegistedShoesDto registedShoesDto1 = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 11, TradeStatus.SELL);
        RegistedShoesDto registedShoesDto2 = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 10, TradeStatus.SELL);


       //when
        registedShoesService.delete(registedShoesDto.getId(),member.getUsername());
        RegistedShoes registedShoes = registedShoesRepository.findById(registedShoesDto1.getId()).orElseThrow(CResourceNotExistException::new);


        //then
        assertThat(registedShoes.getShoesInSize().getShoes().getPrice()).isEqualTo(10);

        assertThatThrownBy(() -> registedShoesRepository.findById(registedShoesDto.getId()).orElseThrow(CResourceNotExistException::new))
                .isInstanceOf(CResourceNotExistException.class);
    }

}