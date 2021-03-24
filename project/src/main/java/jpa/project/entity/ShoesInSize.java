package jpa.project.entity;

import jpa.project.advide.exception.CNotEnoughStockException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ShoesInSize {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shoes_id")
    private Shoes shoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="size_id")
    private ShoesSize size;

    @OneToMany(mappedBy ="shoesInSize")
    private List<RegistedShoes>registedShoes=new ArrayList<>();

    private int stockQuantity;



    public static ShoesInSize createShoesInSize(ShoesSize shoesSize){
        ShoesInSize shoesInSize=new ShoesInSize();
        shoesInSize.stockQuantity=0;
        shoesInSize.addShoesSize(shoesSize);

        return shoesInSize;

    }

    public void addShoesSize(ShoesSize shoesSize){
        this.size=shoesSize;
        shoesSize.getShoesInSizes().add(this);

    }
    public void addShoes(Shoes shoes){
        this.shoes=shoes;
        shoes.getShoesInSizes().add(this);

   }

    public void addStock() {
        this.stockQuantity+=1;
        this.getShoes().addStock();
    }

    public void deleteStockQuantity() {
        int restStock=this.stockQuantity-1;
        if(restStock<0){
            throw new CNotEnoughStockException();
        }
        this.stockQuantity-=1;
        this.getShoes().deleteStockQuantity();
    }





}
