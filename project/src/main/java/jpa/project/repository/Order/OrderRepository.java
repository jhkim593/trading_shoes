package jpa.project.repository.Order;

import jpa.project.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("select o from Order o join fetch o.buyer b join fetch o.seller s join fetch o.registedShoes r join fetch r.shoesInSize ss join fetch ss.size join fetch ss.shoes where b.id=:memberId order by o.createdDate")
    List<Order> findPurChasOrderOrderByCreatedDate(@Param("memberId")Long id);

    @Query("select o from Order o join fetch o.buyer b join fetch o.seller s join fetch o.registedShoes r  join fetch r.shoesInSize ss  join fetch ss.size join fetch ss.shoes where s.id=:memberId order by o.createdDate")
    List<Order> findSalesOrderOrderByCreatedDate(@Param("memberId")Long id);
}
