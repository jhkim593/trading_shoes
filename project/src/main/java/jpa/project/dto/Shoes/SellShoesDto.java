package jpa.project.dto.Shoes;

import jpa.project.entity.Shoes;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellShoesDto {

    private Long id;

    private String name;

    private String brand;

    private LocalDateTime createdDate;

    private List<SellShoesInSizeDto> shoesInSize;

    public SellShoesDto(Shoes shoes){
        this.id=shoes.getId();
        this.name=shoes.getName();
        this.brand=shoes.getBrand().getName();
        this.createdDate=shoes.getCreatedDate();
        this.shoesInSize=shoes.getShoesInSizes().stream().map(s->new SellShoesInSizeDto(s)).collect(Collectors.toList());


    }

}
