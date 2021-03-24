package jpa.project.repository.order;



import jpa.project.model.dto.order.OrderDto;
import jpa.project.model.dto.order.OrderSimpleDto;
import jpa.project.repository.search.OrderSearch;
import jpa.project.repository.search.ShoesSizeSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepositoryCustom {
    Slice<OrderSimpleDto> findOrdersByShoesSize(Long shoesId, Long lastOrderId,ShoesSizeSearch shoesSize,Pageable pageable);
    Page<OrderDto> findAllOrder(Pageable pageable,OrderSearch orderSearch);
}
