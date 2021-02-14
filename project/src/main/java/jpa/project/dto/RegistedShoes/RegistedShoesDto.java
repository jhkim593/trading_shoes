package jpa.project.dto.RegistedShoes;

import jpa.project.entity.RegistedShoes;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegistedShoesDto {

    private String shoesname;
    private String size;
    private String username;
    private int price;

    public static RegistedShoesDto createRegistedShoesDto(String shoesname, String username,String size,int price){
        return new RegistedShoesDto(shoesname,size,username,price);
    }

}
