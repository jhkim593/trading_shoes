package jpa.project.repository.Order;



import jpa.project.dto.order.OrderSimpleDto;
import jpa.project.repository.search.ShoesSizeSearch;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepositoryCustom {
    List<OrderSimpleDto> findOrdersByShoesSize(Long shoesId,ShoesSizeSearch shoesSize);
}
