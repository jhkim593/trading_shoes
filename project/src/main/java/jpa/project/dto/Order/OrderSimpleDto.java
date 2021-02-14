package jpa.project.dto.Order;

import jpa.project.entity.Order;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderSimpleDto {
    private LocalDateTime orderDateTime;
    private String shoesname;
    private String size;
    private int price;


    public static OrderSimpleDto createOrderSimpleDto(Order order){
        return new OrderSimpleDto(order.getCreatedDate(),order.getRegistedShoes().getShoesInSize().getShoes().getName(),
                order.getRegistedShoes().getShoesInSize().getSize().getUS(),order.getPrice());
    }
}
