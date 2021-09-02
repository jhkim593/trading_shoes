package jpa.project.service;

import jpa.project.advide.exception.COrderNotFoundException;
import jpa.project.advide.exception.CResourceNotExistException;
import jpa.project.advide.exception.CUserNotFoundException;
import jpa.project.entity.*;
import jpa.project.model.dto.delivery.DeliveryRegisterRequestDto;
import jpa.project.model.dto.member.MemberRegisterRequestDto;
import jpa.project.model.dto.order.OrderDto;
import jpa.project.model.dto.order.OrderSimpleDto;
import jpa.project.model.dto.registedShoes.RegistedShoesDto;
import jpa.project.repository.member.MemberRepository;
import jpa.project.repository.order.OrderRepository;
import jpa.project.repository.registedShoes.RegistedShoesRepository;
import jpa.project.repository.search.OrderSearch;
import jpa.project.repository.search.ShoesSizeSearch;
import jpa.project.repository.shoesInSize.ShoesInSizeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

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
    @Autowired
    OrderRepository orderRepository;


    @BeforeEach

    public void beforeEach()throws Exception{
        MemberRegisterRequestDto member=new MemberRegisterRequestDto("s","ss","zzz");
        MemberRegisterRequestDto seller=new MemberRegisterRequestDto("s1","ss","zzz");



        signService.signup(member);
        signService.signup(seller);


    }
    @Test
    public void 주문생성()throws Exception{
       //given
        Member member1 = memberRepository.findByUsername("s").orElseThrow(CUserNotFoundException::new);
        Member member2 = memberRepository.findByUsername("s1").orElseThrow(CUserNotFoundException::new);


        Brand asics = Brand.createBrand("asics", "1111");
        em.persist(asics);
        ShoesSize shoesSize = ShoesSize.createShoesSize("7");
        em.persist(shoesSize);
        ShoesInSize shoesInSize = ShoesInSize.createShoesInSize(shoesSize);
        Shoes shoes = Shoes.createShoes("zzz", asics, shoesInSize);
        em.persist(shoes);

        RegistedShoesDto registedShoesDto = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 11, TradeStatus.SELL);
        RegistedShoesDto registedShoesDto1 = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 10, TradeStatus.SELL);

        //when
        OrderDto save = orderService.save(member2.getUsername(),registedShoesDto1.getId());

        em.flush();
        em.clear();
//        Order order = orderRepository.findById(save.getId()).orElseThrow(COrderNotFoundException::new);
        RegistedShoes registedShoes = registedShoesRepository.findById(registedShoesDto1.getId()).orElseThrow(CResourceNotExistException::new);

        assertThat(registedShoes.getShoesInSize().getShoes().getPrice()).isEqualTo(11);

        //then


    }

    @Test
    public void 신발사이즈별주문내역()throws Exception{
       //given

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

        Slice<OrderSimpleDto> ordersByShoesSize = orderService.findOrdersByShoesSize(shoes.getId(),null,shoesSizeSearch,3);
        Slice<OrderSimpleDto> purchasedOrder = orderService.findPurchasedOrder(member2.getId(), null, 3);
        Slice<OrderSimpleDto> soldOrder = orderService.findSoldOrder(member1.getId(), null, 3);

        //then

        assertThat(ordersByShoesSize.hasNext()).isTrue();
        assertThat(ordersByShoesSize.getSize()).isEqualTo(3);
        assertThat(purchasedOrder.hasNext()).isTrue();
        assertThat(purchasedOrder.getSize()).isEqualTo(3);
        assertThat(soldOrder.hasNext()).isTrue();
        assertThat(soldOrder.getSize()).isEqualTo(3);




    }
    @Test
    public void 배송정보입력()throws Exception{
       //given
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
        em.persist(registedShoes1);

        //when
        Order order1 = Order.createOrder(member2, registedShoes1); order1.updateOrderStatus(OrderStatus.Complete);
        em.persist(order1);

        DeliveryRegisterRequestDto deliveryRegisterRequestDto=new DeliveryRegisterRequestDto();
        deliveryRegisterRequestDto.setCompany("cj");
        deliveryRegisterRequestDto.setTrackingNumber("11");
        orderService.addDeliveryInfo(order1.getId(),deliveryRegisterRequestDto);
        em.flush();
        em.clear();
        Order order = orderRepository.findById(order1.getId()).orElseThrow(COrderNotFoundException::new);



        //then
        assertThat(order.getDelivery().getTrackingNumber()).isEqualTo("11");
        assertThat(order.getDelivery().getCompany()).isEqualTo("cj");

    }

    @Test
    public void 모든주문내역()throws Exception{
       //given
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

        //when
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



        DeliveryRegisterRequestDto deliveryRegisterRequestDto=new DeliveryRegisterRequestDto();
        deliveryRegisterRequestDto.setCompany("cj");
        deliveryRegisterRequestDto.setTrackingNumber("11");
        orderService.addDeliveryInfo(order2.getId(),deliveryRegisterRequestDto);

        em.flush();
        em.clear();
        OrderSearch orderSearch=new OrderSearch();
        orderSearch.setTrackingNumber("11");
        PageRequest page=PageRequest.of(0,2);
        Page<OrderDto> allOrder = orderService.findAllOrder(orderSearch, page);

        //then

        Order order = orderRepository.findById(order2.getId()).orElseThrow(COrderNotFoundException::new);
        assertThat(allOrder.getNumberOfElements()).isEqualTo(1);


    }
    @Test
    public void 주문상태변경()throws Exception{
       //given
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

        RegistedShoesDto registedShoesDto = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 11, TradeStatus.SELL);
       
       //when
        OrderDto save = orderService.save(member2.getUsername(),registedShoesDto.getId());
        orderService.updateOrder(save.getId(), DeliveryStatus.Completed);

        em.flush();
        em.clear();
        Order order = orderRepository.findById(save.getId()).orElseThrow(COrderNotFoundException::new);

        //then
        assertThat(order.getDelivery().getDeliveryStatus()).isEqualTo(DeliveryStatus.Completed);


    }
    @Test
    public void 거래내역조회()throws Exception{
       //given
        Member member1 = memberRepository.findByUsername("s").orElseThrow(CUserNotFoundException::new);
        Member member2 = memberRepository.findByUsername("s1").orElseThrow(CUserNotFoundException::new);


        Brand asics = Brand.createBrand("asics", "1111");
        em.persist(asics);
        ShoesSize shoesSize = ShoesSize.createShoesSize("7");
        em.persist(shoesSize);
        ShoesInSize shoesInSize = ShoesInSize.createShoesInSize(shoesSize);
        Shoes shoes = Shoes.createShoes("zzz", asics, shoesInSize);
        em.persist(shoes);

        RegistedShoesDto registedShoesDto = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 11, TradeStatus.SELL);
        RegistedShoesDto registedShoesDto1 = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 10, TradeStatus.SELL);
        RegistedShoesDto registedShoesDto2 = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 10, TradeStatus.SELL);
        RegistedShoesDto registedShoesDto3 = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 10, TradeStatus.SELL);
        RegistedShoesDto registedShoesDto4 = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 10, TradeStatus.SELL);
        RegistedShoesDto registedShoesDto5 = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 10, TradeStatus.SELL);


        //when
        OrderDto save = orderService.save(member2.getUsername(),registedShoesDto.getId());
        OrderDto save1 = orderService.save(member2.getUsername(),registedShoesDto1.getId());
        OrderDto save2 = orderService.save(member2.getUsername(),registedShoesDto2.getId());
        OrderDto save3 = orderService.save(member2.getUsername(),registedShoesDto3.getId());
        OrderDto save4 = orderService.save(member2.getUsername(),registedShoesDto4.getId());
        OrderDto save5 = orderService.save(member2.getUsername(),registedShoesDto5.getId());


        em.flush();
        em.clear();
        Slice<OrderSimpleDto> soldOrder = orderService.findSoldOrder(member1.getId(), null, 6);
        Slice<OrderSimpleDto> purchasedOrder = orderService.findPurchasedOrder(member2.getId(), null, 6);


        //then
        assertThat(soldOrder.getSize()).isEqualTo(6);
        assertThat(purchasedOrder.getSize()).isEqualTo(6);
        assertThat(soldOrder.hasNext()).isFalse();
        assertThat(purchasedOrder.hasNext()).isFalse();

    }
    @Test
    public void 주문삭제()throws Exception{
        Member member1 = memberRepository.findByUsername("s").orElseThrow(CUserNotFoundException::new);
        Member member2 = memberRepository.findByUsername("s1").orElseThrow(CUserNotFoundException::new);


        Brand asics = Brand.createBrand("asics", "1111");
        em.persist(asics);
        ShoesSize shoesSize = ShoesSize.createShoesSize("7");
        em.persist(shoesSize);
        ShoesInSize shoesInSize = ShoesInSize.createShoesInSize(shoesSize);
        Shoes shoes = Shoes.createShoes("zzz", asics, shoesInSize);
        em.persist(shoes);

        RegistedShoesDto registedShoesDto = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 11, TradeStatus.SELL);
        RegistedShoesDto registedShoesDto1 = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 10, TradeStatus.SELL);
        RegistedShoesDto registedShoesDto2 = registedShoesService.registShoes(member1.getUsername(), shoesInSize.getId(), 9, TradeStatus.SELL);

        //when
        OrderDto save = orderService.save(member2.getUsername(),registedShoesDto.getId());
        OrderDto save1 = orderService.save(member2.getUsername(),registedShoesDto1.getId());
        OrderDto save2 = orderService.save(member2.getUsername(),registedShoesDto2.getId());

        orderService.deleteOrder(save1.getId());
        Order order = orderRepository.findById(save.getId()).orElseThrow(COrderNotFoundException::new);
        Order order1 = orderRepository.findById(save1.getId()).orElseThrow(COrderNotFoundException::new);


        //then
        assertThat(order.getRegistedShoes().getShoesInSize().getStockQuantity()).isEqualTo(1);
        assertThat(order.getRegistedShoes().getShoesInSize().getShoes().getPrice()).isEqualTo(10);
        assertThat(order1.getRegistedShoes().getShoesStatus()).isEqualTo(ShoesStatus.BID);
    }


}