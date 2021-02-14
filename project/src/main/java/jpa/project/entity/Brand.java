package jpa.project.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand extends BaseTimeEntity{
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String content;

    @OneToMany(mappedBy ="brand")
    private List<Shoes> shoes=new ArrayList<>();

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
