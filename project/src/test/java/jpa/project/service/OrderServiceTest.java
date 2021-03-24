package jpa.project.service;

import jpa.project.advide.exception.CUserNotFoundException;
import jpa.project.entity.*;
import jpa.project.model.dto.member.MemberRegisterRequestDto;
import jpa.project.repository.member.MemberRepository;
import jpa.project.repository.registedShoes.RegistedShoesRepository;
import jpa.project.repository.search.ShoesSizeSearch;
import jpa.project.repository.shoesInSize.ShoesInSizeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class OrderServiceTest {
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
    @Autowired
    OrderService orderService;

    @Test
    public void 신발사이즈별주문내역()throws Exception{
       //given
        MemberRegisterRequestDto member=new MemberRegisterRequestDto();
        MemberRegisterRequestDto seller=new MemberRegisterRequestDto();

        member.setUsername("s");member.setEmail("ss");member.setPassword("zzz");
        seller.setUsername("s1");seller.setEmail("ss");seller.setPassword("zzz");

        signService.signup(member);
        signService.signup(seller);

        Member member1 = memberRepository.findByUsername("s").orElseThrow(CUserNotFoundException::new);
        Member member2 = memberRepository.findByUsername("s1").orElseThrow(CUserNotFoundException::new);

        Brand asics = Brand.createBrand("asics", "1111");
        em.persist(asics);
        ShoesSize shoesSize = ShoesSize.createShoesSize("7");
        em.persist(shoesSize);
        ShoesInSize shoesInSize = ShoesInSize.createShoesInSize(shoesSize);
        Shoes shoes = Shoes.createShoes("zzz", asics, shoesInSize);
        em.persist(shoes);
        RegistedShoes registedShoes1 = RegistedShoes.createRegistedShoes(member1, shoesInSize, 11, TradeStatus.SELL);
        RegistedShoes registedShoes2= RegistedShoes.createRegistedShoes(member1, shoesInSize, 11, TradeStatus.SELL);
        RegistedShoes registedShoes3 = RegistedShoes.createRegistedShoes(member1, shoesInSize, 11, TradeStatus.SELL);
        RegistedShoes registedShoes4 = RegistedShoes.createRegistedShoes(member1, shoesInSize, 11, TradeStatus.SELL);
        RegistedShoes registedShoes5 = RegistedShoes.createRegistedShoes(member1, shoesInSize, 12, TradeStatus.SELL);
        RegistedShoes registedShoes6 = RegistedShoes.createRegistedShoes(member1, shoesInSize, 11, TradeStatus.SELL);

        em.persist(registedShoes1);
        em.persist(registedShoes2);
        em.persist(registedShoes3);
        em.persist(registedShoes4);
        em.persist(registedShoes5);
        em.persist(registedShoes6);
        Order order1 = Order.createOrder(member2, registedShoes1); order1.updateOrderStatus(OrderStatus.Complete);
        Order order2 = Order.createOrder(member2, registedShoes2);order2.updateOrderStatus(OrderStatus.Complete);
        Order order3 = Order.createOrder(member2, registedShoes3);order3.updateOrderStatus(OrderStatus.Complete);
        Order order4 = Order.createOrder(member2, registedShoes4);order4.updateOrderStatus(OrderStatus.Complete);
        Order order5 = Order.createOrder(member2, registedShoes5);order5.updateOrderStatus(OrderStatus.Complete);
        Order order6 = Order.createOrder(member2, registedShoes6);order6.updateOrderStatus(OrderStatus.Complete);


        em.persist(order1);
        em.persist(order2);
        em.persist(order3);
        em.persist(order4);
        em.persist(order5);
        em.persist(order6);



        //when

        ShoesSizeSearch shoesSizeSearch=new ShoesSizeSearch();
        shoesSizeSearch.setShoesSize("7");
       PageRequest pageable= PageRequest.of(0,3);
//        Slice<OrderSimpleDto> ordersByShoesSize = orderService.findOrdersByShoesSize(shoes.getId(), shoesSizeSearch, pageable);

        //then
//
//        assertThat(ordersByShoesSize.hasNext()).isTrue();
//        assertThat(ordersByShoesSize.getSize()).isEqualTo(3);

    }


}