package jpa.project.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class RegistedShoes extends BaseTimeEntity{
    @Id
    @GeneratedValue
    private Long id;



//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "shoes_id")
//    private Shoes shoes;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="size_id")
//    private ShoesSize size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shoesInSize_id")
    private ShoesInSize shoesInSize;

    private int price;


    @Enumerated(EnumType.STRING)
    private ShoesStatus shoesStatus;




    public static RegistedShoes createRegistedShoes(Member member,ShoesInSize shoesInSize,int price){
       RegistedShoes registedShoes=new RegistedShoes();
       registedShoes.addMember(member);
       registedShoes.price=price;
       shoesInSize.addStock();
       registedShoes.shoesStatus=ShoesStatus.BID;
       registedShoes.addShoesInSize(shoesInSize);
       return registedShoes;
    }

    public void addShoesInSize(ShoesInSize shoesInSize) {
        this.shoesInSize=shoesInSize;
        shoesInSize.getRegistedShoes().add(this);
        if(shoesInSize.getLowestPrice()==0||shoesInSize.getLowestPrice()>this.getPrice()){
            shoesInSize.changeLowestPrice(this.getPrice());
        }

    }

    public void addMember(Member member){
        this.member=member;
        member.getRegistedShoes().add(this);
    }

    public void order() {
        this.shoesStatus=ShoesStatus.COMP;
        this.shoesInSize.order();
    }
}
