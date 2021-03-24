package jpa.project.model.dto.Shoes;

import jpa.project.entity.Shoes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoesSimpleDto {

    private Long id;

    private String name;

    private String brand;

    private LocalDateTime createdDate;

    private int price;


   public static ShoesSimpleDto createShoesDto(Shoes shoes){
       return new ShoesSimpleDto(shoes.getId(),shoes.getName(),shoes.getBrand().getName()
               ,shoes.getCreatedDate(),shoes.getPrice());

   }


}
