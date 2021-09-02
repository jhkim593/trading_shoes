package jpa.project.entity;

import jpa.project.advide.exception.CNotEnoughStockException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class RegistedShoes extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shoesInSize_id")
    private ShoesInSize shoesInSize;

    private int price;


    @Enumerated(EnumType.STRING)
    private ShoesStatus shoesStatus;


    @Enumerated(EnumType.STRING)
    private TradeStatus tradeStatus;


    public static RegistedShoes createRegistedShoes(Member member, ShoesInSize shoesInSize, int price, TradeStatus tradeStatus) {
        RegistedShoes registedShoes = new RegistedShoes();
        registedShoes.addMember(member);
        registedShoes.price = price;
        registedShoes.tradeStatus = tradeStatus;
        if (tradeStatus.equals(TradeStatus.SELL)) {
            shoesInSize.addStock();
        }
        registedShoes.addShoesInSize(shoesInSize);
        registedShoes.shoesStatus = ShoesStatus.BID;


        return registedShoes;
    }


    public void addShoesInSize(ShoesInSize shoesInSize) {
        this.shoesInSize = shoesInSize;
        shoesInSize.getRegistedShoes().add(this);

    }


    public void addMember(Member member) {
        this.member = member;
        member.getRegistedShoes().add(this);
    }

    public void deleteRegistedShoes() {
        if (this.getShoesStatus().equals(ShoesStatus.COMP)) {
            throw new CNotEnoughStockException();
        }
        this.shoesStatus = ShoesStatus.COMP;
        this.shoesInSize.deleteStockQuantity();
    }
    public void changePrice(int price){
        this.price=price;
    }

    public void updateShoesStatus(ShoesStatus shoesStatus){
        this.shoesStatus=shoesStatus;
    }


}
