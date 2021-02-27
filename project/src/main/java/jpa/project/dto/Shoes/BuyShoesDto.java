package jpa.project.dto.Shoes;


import jpa.project.entity.Shoes;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BuyShoesDto {
    private Long id;

    private String name;

    private String brand;

    private LocalDateTime createdDate;

    private List<BuyShoesInSizeDto> shoesInSize;

    public BuyShoesDto(Shoes shoes){
        this.id=shoes.getId();
        this.name=shoes.getName();
        this.brand=shoes.getBrand().getName();
        this.createdDate=shoes.getCreatedDate();
        this.shoesInSize=shoes.getShoesInSizes().stream().map(s->new BuyShoesInSizeDto(s)).collect(Collectors.toList());


    }
}
