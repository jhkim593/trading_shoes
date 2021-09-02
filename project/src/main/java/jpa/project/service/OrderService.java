package jpa.project.service;

import jpa.project.advide.exception.COrderNotFoundException;
import jpa.project.advide.exception.CResourceNotExistException;
import jpa.project.advide.exception.CUserNotFoundException;
import jpa.project.cache.CacheKey;
import jpa.project.entity.*;
import jpa.project.model.dto.delivery.DeliveryRegisterRequestDto;
import jpa.project.model.dto.order.OrderDto;
import jpa.project.model.dto.order.OrderSimpleDto;
import jpa.project.repository.member.MemberRepository;
import jpa.project.repository.order.OrderRepository;
import jpa.project.repository.registedShoes.RegistedShoesRepository;
import jpa.project.repository.search.OrderSearch;
import jpa.project.repository.search.ShoesSizeSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final RegistedShoesRepository registedShoesRepository;
    private final CacheService cacheService;


    @Transactional
    public OrderDto save(String username,Long registedShoesId){
        Member member = getMember(username);
        RegistedShoes registedShoes = getRegistedShoes(registedShoesId);
        Order order = Order.createOrder(member, registedShoes);
        changeShoesPrice(registedShoes);
        orderRepository.save(order);
        cacheService.deleteOrderCache(order.getBuyer().getId(),order.getSeller().getId(),order.getRegistedShoes().getShoesInSize().getShoes().getId(),order.getId());
        return OrderDto.createOrderDto(order);

    }

    private void changeShoesPrice(RegistedShoes registedShoes) {
        Optional<RegistedShoes>optionalRegistedShoes=registedShoesRepository.findLowestPriceInShoes(registedShoes.getShoesInSize().getShoes().getId());
        if(!optionalRegistedShoes.isEmpty())
        registedShoes.getShoesInSize().getShoes().changePrice(optionalRegistedShoes.orElseThrow(CResourceNotExistException::new).getPrice());
    }

    private RegistedShoes getRegistedShoes(Long registedShoesId) {
        Optional<RegistedShoes> findRegistedShoes = registedShoesRepository.findById(registedShoesId);
        RegistedShoes registedShoes = findRegistedShoes.orElseThrow(CResourceNotExistException::new);
        return registedShoes;
    }

    private Member getMember(String username) {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        Member member = findMember.orElseThrow(CUserNotFoundException::new);
        return member;
    }

    //    public OrderSimpleDto findOrder(String username){
//        Optional<Member> findMember = memberRepository.findByUsername(username);
//        Member member = findMember.orElseThrow(CUserNotFoundException::new);
//
//        member.
//
//    }
    @Cacheable(value = CacheKey.ORDER, key = "#orderId",unless ="#result==null")
    public OrderDto detail(Long orderId){
        Order order = getOrder(orderId);
        return OrderDto.createOrderDto(order);

    }
    //판매자 배송정보 입력
    @Transactional
    public void addDeliveryInfo(Long orderId, DeliveryRegisterRequestDto registerRequestDto){
        Order order = getOrder(orderId);
        cacheService.deleteOrderCache(order.getBuyer().getId(),order.getSeller().getId(),order.getRegistedShoes().getShoesInSize().getShoes().getId(),orderId);
        order.addDeliveryInfo(registerRequestDto.getTrackingNumber(),registerRequestDto.getCompany());

    }
    //관리자 주문상태 변경
    @Transactional
    public void updateOrder(Long orderId,DeliveryStatus deliveryStatus){
        Order order = getOrder(orderId);
        cacheService.deleteOrderCache(order.getBuyer().getId(),order.getSeller().getId(),order.getRegistedShoes().getShoesInSize().getShoes().getId(),orderId);
        order.updateDelivery(deliveryStatus);

    }

    private Order getOrder(Long orderId) {
        Optional<Order> findOrder = orderRepository.findById(orderId);
        Order order = findOrder.orElseThrow(COrderNotFoundException::new);
        return order;
    }
    public Page<OrderDto> findAllOrder(OrderSearch orderSearch, Pageable pageable){
        return orderRepository.findAllOrder(pageable,orderSearch);
    }

    @Cacheable(value = CacheKey.ORDERS_PURCHASE, key = "{#memberId, #limit, #lastOrderId}",unless ="#result==null")
    public Slice<OrderSimpleDto> findPurchasedOrder(Long memberId,Long lastOrderId,int limit){
       return orderRepository.findPurChasOrderOrderByCreatedDate(memberId,lastOrderId!=null?lastOrderId:Long.MAX_VALUE, PageRequest.of(0, limit)).map(c -> OrderSimpleDto.createOrderSimpleDto(c));

    }
    @Cacheable(value = CacheKey.ORDERS_SALES, key = "{#memberId, #limit, #lastOrderId}",unless ="#result==null")
    public  Slice<OrderSimpleDto>findSoldOrder(Long memberId,Long lastOrderId,int limit){

        return orderRepository.findSalesOrderOrderByCreatedDate(memberId,lastOrderId!=null?lastOrderId:Long.MAX_VALUE, PageRequest.of(0, limit)).map(c -> OrderSimpleDto.createOrderSimpleDto(c));
    }

    /**캐시**/
//    public void deleteOrder(Long orderId){
//        orderRepository.delete(
//    }
    //신발 거래된 주문표시

    @Cacheable(value = CacheKey.ORDERS, key = "#shoesId",unless ="#result==null")
    public Slice<OrderSimpleDto>findOrdersByShoesSize(Long shoesId,Long lastOrderId,ShoesSizeSearch shoesSizeSearch,int limit){
        return orderRepository.findOrdersByShoesSize(shoesId, lastOrderId != null ? lastOrderId : Long.MAX_VALUE, shoesSizeSearch, PageRequest.of(0, limit));
    }

    //주문취소
    @Transactional
    public void deleteOrder(Long id) {
        Order order = getOrder(id);
        order.deleteOrder();
        cacheService.deleteOrderCache(order.getBuyer().getId(),order.getSeller().getId(),order.getRegistedShoes().getShoesInSize().getShoes().getId(),order.getId());
        changeShoesPrice(order.getRegistedShoes());
    }
}
