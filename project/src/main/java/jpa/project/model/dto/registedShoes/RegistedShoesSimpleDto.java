package jpa.project.model.dto.registedShoes;

import jpa.project.entity.RegistedShoes;
import jpa.project.entity.TradeStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RegistedShoesSimpleDto {
    private Long id;
    private int price;
    private TradeStatus status;

    public static RegistedShoesSimpleDto createRegistedSimpleDto(RegistedShoes registedShoes){
        return new RegistedShoesSimpleDto(registedShoes.getId(), registedShoes.getPrice(),registedShoes.getTradeStatus());
    }



}
