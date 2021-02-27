package jpa.project.repository.registedShoes;

import jpa.project.entity.RegistedShoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistedShoesRepository extends JpaRepository<RegistedShoes,Long> {
    @Query("select r from RegistedShoes r where r.id=:id and r.shoesStatus=jpa.project.entity.ShoesStatus.BID")
    Optional<RegistedShoes> findByIdAndShoesStatus();

    @Query("select r from RegistedShoes r join fetch r.member m join fetch r.shoesInSize sis join fetch sis.size join fetch sis.shoes where m.username=:username")
    List<RegistedShoes> findByUsername(@Param("username")String username);

    @Query("select r.price from RegistedShoes r where r.shoesStatus=jpa.project.entity.ShoesStatus.BID and " +
            "r.tradeStatus=jpa.project.entity.TradeStatus.SELL order by r.price asc")
    int findSellOrderByPrice();

    @Query("select r.price from RegistedShoes r where r.shoesStatus=jpa.project.entity.ShoesStatus.BID and " +
            "r.tradeStatus=jpa.project.entity.TradeStatus.BUY  order by r.price desc")
    int findBuyOrderByPrice();


}
