package jpa.project.dto.Shoes;

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
public class ShoesDto {

    private Long id;

    private String name;

    private String brand;

    private LocalDateTime createdDate;


   public static ShoesDto createShoesDto(Shoes shoes){
       return new ShoesDto(shoes.getId(),shoes.getName(),shoes.getBrand().getName()
               ,shoes.getCreatedDate());

   }


}
