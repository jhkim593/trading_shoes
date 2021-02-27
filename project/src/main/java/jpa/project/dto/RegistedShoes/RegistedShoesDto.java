package jpa.project.dto.RegistedShoes;

import jpa.project.entity.RegistedShoes;
import jpa.project.entity.TradeStatus;
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
    private TradeStatus tradeStatus;

    public static RegistedShoesDto createRegistedShoesDto(RegistedShoes registedShoes){
        return new RegistedShoesDto(registedShoes.getShoesInSize().getShoes().getName(),registedShoes.getShoesInSize().getSize().getUS(),
                registedShoes.getMember().getUsername(), registedShoes.getPrice(),registedShoes.getTradeStatus());
    }

}
