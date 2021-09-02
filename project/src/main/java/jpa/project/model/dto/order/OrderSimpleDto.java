package jpa.project.model.dto.order;

import jpa.project.entity.DeliveryStatus;
import jpa.project.entity.Order;
import jpa.project.entity.OrderStatus;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderSimpleDto implements Serializable {
    private LocalDateTime orderDateTime;
    private String shoesname;
    private String size;
    private int price;
    private OrderStatus orderStatus;
    private DeliveryStatus deliveryStatus;


    public static OrderSimpleDto createOrderSimpleDto(Order order){
        return new OrderSimpleDto(order.getCreatedDate(),order.getRegistedShoes().getShoesInSize().getShoes().getName(),
                order.getRegistedShoes().getShoesInSize().getSize().getUS(),order.getPrice(),order.getOrderStatus(),order.getDelivery().getDeliveryStatus());
    }
}
