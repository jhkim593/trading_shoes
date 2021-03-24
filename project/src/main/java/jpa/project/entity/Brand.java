package jpa.project.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand extends BaseTimeEntity{
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Lob
    private String content;

    @OneToMany(mappedBy ="brand")
    private List<Shoes> shoes=new ArrayList<>();

    @Column(nullable = false)
    private int stockQuantity;



    public static Brand createBrand(String name,String content){
        Brand brand=new Brand();
        brand.name=name;
        brand.content=content;
        brand.stockQuantity=0;
        return brand;
    }
    public void update(String name,String content){
        this.name=name;
        this.content=content;
    }
}
