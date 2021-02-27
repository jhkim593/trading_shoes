package jpa.project.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Delivery extends BaseTimeEntity{
    @Id @GeneratedValue
    private Long id;

    private Long number;

    @OneToOne(mappedBy = "delivery",fetch = LAZY)
    private Order order;

    private String company;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Embedded
    private Address address;

    public static Delivery createDelivery(Long number,String company,Order order,DeliveryStatus deliveryStatus){
       Delivery delivery=new Delivery();
       delivery.number=number;
       delivery.company=company;
       delivery.addOrder(order);
       delivery.deliveryStatus=deliveryStatus;
       delivery.address=order.getBuyer().getAddress();
       return delivery;
    }
    public void addOrder(Order order){
        this.order=order;
        order.addDelivery(this);
    }
}
