package jpa.project.entity;

import jpa.project.advide.exception.CNotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Shoes extends BaseTimeEntity{
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    //사이즈중 가장 최저가
    private int price;

    //모든 사이즈 총 재고
    private int stockQuantity;


     //조회수
     private int view;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="brand_id")
    private Brand brand;

//    @OneToMany(mappedBy = "shoes")
//    private List<RegistedShoes>registedShoes=new ArrayList<>();
    @OneToMany(mappedBy = "shoes",cascade = CascadeType.ALL)
    private List<ShoesInSize>shoesInSizes=new ArrayList<>();








public static Shoes createShoes(String name,Brand brand,ShoesInSize...shoesInSizes){

        Shoes shoes=new Shoes();
        shoes.name=name;
        shoes.stockQuantity=0;
        shoes.view=0;
        shoes.addBrand(brand);
    for (ShoesInSize shoesInSize : shoesInSizes) {
        shoesInSize.addShoes(shoes);
    }

        return shoes;
    }

    public void addBrand(Brand brand){
        this.brand=brand;
        brand.getShoes().add(this);
    }
//    public void addShoesInSize(ShoesInSize shoesInSize){
//       this.getShoesInSizes().add(shoesInSize);
//
//
//    }
    public void update(String name){
    this.name=name;
    }

    public void addStock() {
     this.stockQuantity+=1;
    }

    public void addView(){
    this.view+=1;
    }

    public void deleteStockQuantity() {
        int restStock=this.stockQuantity-1;
        if(restStock<0){
            throw new CNotEnoughStockException();
        }
        this.stockQuantity-=1;
    }

    public void changePrice(int price) {
    if(this.getPrice()==0||this.getPrice()>price){
        this.price=price;
    }

    }

}

