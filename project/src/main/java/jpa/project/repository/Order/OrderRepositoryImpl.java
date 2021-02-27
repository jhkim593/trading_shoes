package jpa.project.repository.Order;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpa.project.dto.order.OrderSimpleDto;
import jpa.project.entity.*;
import jpa.project.repository.search.ShoesSizeSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static jpa.project.entity.QOrder.*;
import static jpa.project.entity.QRegistedShoes.*;
import static jpa.project.entity.QShoes.*;
import static jpa.project.entity.QShoesInSize.*;
import static jpa.project.entity.QShoesSize.*;
import static org.springframework.util.StringUtils.*;


public class OrderRepositoryImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Autowired
    private EntityManager em;

    public OrderRepositoryImpl(EntityManager em){
        this.queryFactory=new JPAQueryFactory(em);
    }

    @Override
    public List<OrderSimpleDto> findOrdersByShoesSize(Long shoesId,ShoesSizeSearch shoesSizeSearch) {
        return queryFactory.select(Projections.constructor(OrderSimpleDto.class, order.createdDate, shoes.name,
                shoesSize.US, order.price)).from(order)
                .join(order.registedShoes, registedShoes)
                .join(registedShoes.shoesInSize, shoesInSize)
                .join(shoesInSize.size, shoesSize)
                .join(shoesInSize.shoes, shoes)
                .where(
                        shoesSizeSearchEq(shoesSizeSearch.getShoesSize())
                        , shoes.id.eq(shoesId)
                ).fetch();
    }

    private BooleanExpression shoesSizeSearchEq(String shoesSizeSearch) {
        return hasText(shoesSizeSearch)?shoesSize.US.eq(shoesSizeSearch):null;
    }
}
