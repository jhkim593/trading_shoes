package jpa.project.dto.Shoes;

import jpa.project.dto.RegistedShoes.RegistedShoesDto;
//import jpa.project.dto.RegistedShoes.RegistedShoesSimpleDto;
import jpa.project.entity.ShoesInSize;
import jpa.project.entity.ShoesStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class SellShoesInSizeDto {
    private Long id;

    private String US;

    private int stockQuantity;

    private int lowestPrice;

//    private List<RegistedShoesSimpleDto>registedShoes;

    public SellShoesInSizeDto(ShoesInSize shoesInSize){
        this.id=shoesInSize.getId();
        this.US=shoesInSize.getSize().getUS();
        this.stockQuantity=shoesInSize.getStockQuantity();
        this.lowestPrice=shoesInSize.getLowestPrice();
//        List<RegistedShoesSimpleDto>registedShoesSimpleDtoList=new ArrayList<>();
//        shoesInSize.getRegistedShoes().stream().forEach(s->{if(s.getShoesStatus().equals(ShoesStatus.BID)) registedShoesSimpleDtoList.add(new RegistedShoesSimpleDto(s));});
//        this.registedShoes=registedShoesSimpleDtoList;


    }
}
