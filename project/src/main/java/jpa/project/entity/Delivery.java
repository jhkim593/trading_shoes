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


    private String trackingNumber;

    @OneToOne(mappedBy = "delivery",fetch = LAZY)
    private Order order;

    private String company;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Embedded
    private Address address;

    public static Delivery createDelivery(Address address,DeliveryStatus deliveryStatus){
       Delivery delivery=new Delivery();
       delivery.deliveryStatus=deliveryStatus;
       delivery.address=address;
       return delivery;
    }
    public void addOrder(Order order){
        this.order=order;

    }

    public void update(String number, String company) {
        this.trackingNumber=number;
        this.company=company;
        this.deliveryStatus=DeliveryStatus.ShippingToUs;
    }

    public void updateDelivery(DeliveryStatus deliveryStatus) {
        this.deliveryStatus=deliveryStatus;
    }
}
