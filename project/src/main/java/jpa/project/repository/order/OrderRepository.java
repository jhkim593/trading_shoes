package jpa.project.repository.order;

import jpa.project.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order,Long>,OrderRepositoryCustom {

    @Query("select o from Order o join fetch o.buyer b join fetch o.seller s join fetch o.registedShoes r " +
            "join fetch r.shoesInSize ss join fetch ss.size join fetch ss.shoes where b.id=:memberId and o.id<:lastOrderId order by o.createdDate desc")
    Slice<Order> findPurChasOrderOrderByCreatedDate(@Param("memberId")Long id,@Param("lastOrderId")Long lastOrderId, Pageable pageable);

    @Query("select o from Order o join fetch o.buyer b join fetch o.seller s join fetch o.registedShoes r  " +
            "join fetch r.shoesInSize ss  join fetch ss.size join fetch ss.shoes where s.id=:memberId and o.id<:lastOrderId order by o.createdDate desc")
    Slice<Order> findSalesOrderOrderByCreatedDate(@Param("memberId")Long id, @Param("lastOrderId")Long lastOrderId, Pageable pageable);


}
