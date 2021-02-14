package jpa.project.repository.shoes;

import jpa.project.dto.Shoes.ShoesDto;
import jpa.project.dto.Shoes.ShoesWithSizeDto;
import jpa.project.repository.search.ShoesSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoesRepositoryCustom {
    Page<ShoesDto> search(ShoesSearch shoesSearch, Pageable pageable);
    ShoesWithSizeDto detailShoesWithSizeDto(Long ShoesId);
}
