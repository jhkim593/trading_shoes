package jpa.project.Service;

import jpa.project.advide.exception.COrderNotFoundException;
import jpa.project.advide.exception.CResourceNotExistException;
import jpa.project.advide.exception.CUserNotFoundException;
import jpa.project.dto.order.OrderDto;
import jpa.project.dto.order.OrderSimpleDto;
import jpa.project.entity.*;
import jpa.project.repository.MemberRepository;
import jpa.project.repository.Order.OrderRepository;
import jpa.project.repository.RegistedShoesRepository;
import jpa.project.repository.search.ShoesSizeSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final RegistedShoesRepository registedShoesRepository;

    @Transactional
    public OrderDto save(String username,Long registedShoesId){
        Optional<Member> findMember = memberRepository.findByUsername(username);
        Member member = findMember.orElseThrow(CUserNotFoundException::new);
        Optional<RegistedShoes> findRegistedShoes = registedShoesRepository.findById(registedShoesId);
        RegistedShoes registedShoes = findRegistedShoes.orElseThrow(CResourceNotExistException::new);
        Order order = Order.createOrder(member, registedShoes.getMember(), registedShoes);
        orderRepository.save(order);
        return OrderDto.createOrderDto(order);
    }

//    public OrderSimpleDto findOrder(String username){
//        Optional<Member> findMember = memberRepository.findByUsername(username);
//        Member member = findMember.orElseThrow(CUserNotFoundException::new);
//
//        member.
//
//    }
    public OrderDto detail(Long orderId){
        Optional<Order> findOrder = orderRepository.findById(orderId);
        Order order = findOrder.orElseThrow(COrderNotFoundException::new);
        return OrderDto.createOrderDto(order);

    }
    public List<OrderSimpleDto> findPurchasedOrder(Long memberId){
       return orderRepository.findPurChasOrderOrderByCreatedDate(memberId).stream().map(c -> OrderSimpleDto.createOrderSimpleDto(c)).collect(Collectors.toList());

    }
    public  List<OrderSimpleDto>findSoldOrder(Long memberId){

        return orderRepository.findSalesOrderOrderByCreatedDate(memberId).stream().map(c->OrderSimpleDto.createOrderSimpleDto(c)).collect(Collectors.toList());
    }

    /**캐시**/
//    public void deleteOrder(Long orderId){
//        orderRepository.delete(
//    }
    //신발 거래된 주문표시

    public List<OrderSimpleDto>findOrdersByShoesSize(ShoesSizeSearch shoesSizeSearch){
        return orderRepository.findOrdersByShoesSize(shoesSizeSearch);
    }
}
