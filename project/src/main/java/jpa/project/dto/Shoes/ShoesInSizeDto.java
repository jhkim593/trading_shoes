package jpa.project.dto.Shoes;

import jpa.project.dto.RegistedShoes.RegistedShoesDto;
import jpa.project.dto.RegistedShoes.RegistedShoesSimpleDto;
import jpa.project.entity.ShoesInSize;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ShoesInSizeDto {
    private Long id;

    private String US;

    private int stockQuantity;

    private int lowestPrice;

    private List<RegistedShoesSimpleDto>registedShoes;

    public ShoesInSizeDto(ShoesInSize shoesInSize){
        this.id=shoesInSize.getId();
        this.US=shoesInSize.getSize().getUS();
        this.stockQuantity=shoesInSize.getStockQuantity();
        this.lowestPrice=shoesInSize.getLowestPrice();
        this.registedShoes=shoesInSize.getRegistedShoes().stream().map(s->new RegistedShoesSimpleDto(s)).collect(Collectors.toList());


    }
}
