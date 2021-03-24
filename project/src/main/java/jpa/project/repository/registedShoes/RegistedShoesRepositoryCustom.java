package jpa.project.repository.registedShoes;

import jpa.project.entity.TradeStatus;
import jpa.project.model.dto.registedShoes.RegistedShoesDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistedShoesRepositoryCustom {

    int findLowestPriceInShoes(Long id);
    Slice<RegistedShoesDto>findRegistedShoesByTradeStatus(Long memberId ,Long lastRegistedShoesId ,TradeStatus tradeStatus, Pageable pageable);


}
