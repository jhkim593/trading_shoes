package jpa.project.repository.search;

import jpa.project.entity.DeliveryStatus;
import jpa.project.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {
    private String buyerName;
    private String sellerName;
    private String trackingNumber;
    private DeliveryStatus deliveryStatus;
    private OrderStatus orderStatus;

}
