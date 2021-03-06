package jpa.project.model.dto.brand;

import jpa.project.entity.Brand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter

public class BrandSimpleDto {

    private Long id;

    private String name;

    private int stockQuantity;

    public static BrandSimpleDto createBrandSimpleDto(Brand brand){
        return new BrandSimpleDto(brand.getId(), brand.getName(), brand.getStockQuantity());
    }

}
