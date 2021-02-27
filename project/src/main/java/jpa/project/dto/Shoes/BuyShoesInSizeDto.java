package jpa.project.dto.Shoes;

import jpa.project.entity.ShoesInSize;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyShoesInSizeDto {
    private Long id;

    private String US;

    private int highestPrice;

    public BuyShoesInSizeDto(ShoesInSize shoesInSize) {
        this.id = shoesInSize.getId();
        this.US = shoesInSize.getSize().getUS();
        this.highestPrice = shoesInSize.getHighestPrice();
    }
}
