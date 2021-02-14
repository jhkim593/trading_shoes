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



    //양방향 불필요
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="registed_shoes_id")
    private RegistedShoes registedShoes;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;



    public static Order createOrder(Member buyer,Member seller,RegistedShoes registedShoes){
        Order order=new Order();
        order.addBuyer(buyer);
        order.price= registedShoes.getPrice();
        order.addSeller(seller);
        order.registedShoes=registedShoes;
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

    /**
     * order와 registedshoes 연관관계 메소드 필요?**/
}
