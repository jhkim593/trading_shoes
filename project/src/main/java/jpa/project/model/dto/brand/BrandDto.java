package jpa.project.model.dto.brand;

import jpa.project.model.dto.shoes.ShoesSimpleDto;
import jpa.project.entity.Brand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BrandDto {

    private Long id;

    private String name;

    private String content;

    private int stockQuantity;

    private List<ShoesSimpleDto> shoesSimpleDtos;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;


    public static BrandDto createBrandDto(Brand brand){
        return new BrandDto(brand.getId(),brand.getName(),brand.getContent(), brand.getStockQuantity()
                          ,brand.getShoes().stream().map(b-> ShoesSimpleDto.createShoesDto(b)).collect(Collectors.toList()), brand.getCreatedDate(),brand.getLastModifiedDate());
    }
}
