package jpa.project.dto.order;

import jpa.project.entity.Address;
import jpa.project.entity.Order;
import jpa.project.entity.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDto {
    private Long id;
    private String buyername;
    private String sellername;
    private int price;
    private LocalDateTime orderDateTime;
    private OrderStatus orderStatus;
    private String shoesname;
    private String size;
    private Address address;

    public static OrderDto createOrderDto(Order order){
        return new OrderDto(order.getId(),order.getBuyer().getUsername(),order.getSeller().getUsername()
                , order.getPrice(), order.getCreatedDate(),order.getOrderStatus(),order.getRegistedShoes().getShoesInSize().getShoes().getName()
                ,order.getRegistedShoes().getShoesInSize().getSize().getUS(),order.getBuyer().getAddress());

    }
}
