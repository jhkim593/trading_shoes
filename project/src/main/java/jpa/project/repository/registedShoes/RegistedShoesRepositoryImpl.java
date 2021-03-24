package jpa.project.repository.registedShoes;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpa.project.entity.*;
import jpa.project.model.dto.registedShoes.RegistedShoesDto;
import jpa.project.repository.RepositoryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import javax.persistence.EntityManager;

import java.util.List;

import static jpa.project.entity.QMember.*;
import static jpa.project.entity.QRegistedShoes.*;
import static jpa.project.entity.QShoes.*;
import static jpa.project.entity.QShoesInSize.*;
import static jpa.project.entity.QShoesSize.*;

public class RegistedShoesRepositoryImpl implements RegistedShoesRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Autowired
    private EntityManager em;

    public RegistedShoesRepositoryImpl(EntityManager em) {

        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public int findLowestPriceInShoes(Long id) {
        RegistedShoes registedShoes = em.createQuery("select r from RegistedShoes r join fetch r.shoesInSize sis " +
                "join fetch sis.shoes sh where sh.id=:id and r.shoesStatus=jpa.project.entity.ShoesStatus.BID and r.tradeStatus=jpa.project.entity.TradeStatus.SELL order by r.price asc", RegistedShoes.class).setParameter("id", id).setMaxResults(1).getSingleResult();
        return registedShoes.getPrice();
    }

    @Override
    public Slice<RegistedShoesDto> findRegistedShoesByTradeStatus(Long memberId,Long lastRegistedShoesId,TradeStatus tradeStatus,Pageable pageable) {
        List<RegistedShoesDto> registedShoesDtos = queryFactory.select(Projections.constructor(RegistedShoesDto.class,
                shoes.name, shoesSize.US,
                member.username, registedShoes.price
                , registedShoes.tradeStatus)).from(registedShoes)
                .join(registedShoes.shoesInSize, shoesInSize)
                .join(shoesInSize.shoes, shoes)
                .join(shoesInSize.size, shoesSize)
                .join(registedShoes.member, member)
                .where(member.id.eq(memberId),
                        registedShoes.tradeStatus.eq(tradeStatus)
                        ,registedShoes.id.lt(lastRegistedShoesId)
                        , registedShoes.shoesStatus.eq(ShoesStatus.BID))
                .fetch();
        return RepositoryHelper.toSlice(registedShoesDtos,pageable);
    }


}
