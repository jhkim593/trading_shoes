package jpa.project.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class ShoesSize {

   @Id
   @GeneratedValue
   private Long id;

   private String US;

    @OneToMany(mappedBy = "size")
    private List<ShoesInSize> shoesInSizes=new ArrayList<>();

    public static ShoesSize createShoesSize(String US){
     ShoesSize shoesInSize=new ShoesSize();
      shoesInSize.US=US;
      return shoesInSize;
   }

}
