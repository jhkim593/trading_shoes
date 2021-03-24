package jpa.project.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name="orders")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="buyer_id")
    private Member buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seller_id")
    private Member seller;

    private int price;



    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="registed_shoes_id")
    private RegistedShoes registedShoes;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="delivery_id")
    private Delivery delivery;



    public static Order createOrder(Member buyer,RegistedShoes registedShoes){
        Order order=new Order();
        order.addBuyer(buyer);
        order.price= registedShoes.getPrice();
        order.addSeller(registedShoes.getMember());
        order.registedShoes=registedShoes;
        Delivery delivery = Delivery.createDelivery(buyer.getAddress(), DeliveryStatus.Ordered);
        order.addDelivery(delivery);
        order.orderStatus=OrderStatus.Progress;
        registedShoes.order();
        return order;
    }

    private void addSeller(Member seller) {
        this.seller=seller;
        seller.getSales().add(this);
    }

    private void addBuyer(Member buyer) {
        this.buyer=buyer;
        buyer.getPurchases().add(this);
    }
    public void addDelivery(Delivery delivery){
        this.delivery=delivery;
        delivery.addOrder(this);
    }
    public void addDeliveryInfo(String number,String company){
        this.delivery.update(number,company);
        updateDelivery(DeliveryStatus.ShippingToUs);
    }
    public void updateDelivery(DeliveryStatus deliveryStatus){
        this.delivery.updateDelivery(deliveryStatus);
    }
    public void updateOrderStatus(OrderStatus orderStatus){this.orderStatus=orderStatus;}


    /**
     * order와 registedshoes 연관관계 메소드 필요?**/
}
