package jpa.project.repository.shoes;

import jpa.project.model.dto.Shoes.ShoesDto;
import jpa.project.model.dto.Shoes.ShoesSimpleDto;
import jpa.project.repository.search.ShoesSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoesRepositoryCustom {
    Page<ShoesSimpleDto> search(ShoesSearch shoesSearch, Pageable pageable);
    ShoesDto detailShoesDto(Long ShoesId);


}
