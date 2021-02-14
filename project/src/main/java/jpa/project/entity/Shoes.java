package jpa.project.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Shoes extends BaseTimeEntity{
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    //사이즈중 가장 최저가
    private String price;

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
}

