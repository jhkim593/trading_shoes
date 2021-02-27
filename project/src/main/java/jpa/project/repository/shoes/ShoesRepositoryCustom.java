package jpa.project.repository.shoes;

import jpa.project.dto.Shoes.BuyShoesDto;
import jpa.project.dto.Shoes.ShoesDto;
import jpa.project.dto.Shoes.SellShoesDto;
import jpa.project.repository.search.ShoesSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoesRepositoryCustom {
    Page<ShoesDto> search(ShoesSearch shoesSearch, Pageable pageable);
    SellShoesDto detailSellShoesDto(Long ShoesId);
    BuyShoesDto  detailBuyShoesDto(Long ShoesId);

}
