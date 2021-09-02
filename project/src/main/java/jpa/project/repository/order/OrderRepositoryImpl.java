package jpa.project.repository.order;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpa.project.entity.DeliveryStatus;
import jpa.project.entity.Order;
import jpa.project.entity.OrderStatus;
import jpa.project.entity.QMember;
import jpa.project.model.dto.order.OrderDto;
import jpa.project.model.dto.order.OrderSimpleDto;
import jpa.project.repository.RepositoryHelper;
import jpa.project.repository.search.OrderSearch;
import jpa.project.repository.search.ShoesSizeSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static jpa.project.entity.QDelivery.delivery;
import static jpa.project.entity.QOrder.order;
import static jpa.project.entity.QRegistedShoes.registedShoes;
import static jpa.project.entity.QShoes.shoes;
import static jpa.project.entity.QShoesInSize.shoesInSize;
import static jpa.project.entity.QShoesSize.shoesSize;
import static org.springframework.util.StringUtils.hasText;


public class OrderRepositoryImpl implements OrderRepositoryCustom{


    private final JPAQueryFactory queryFactory;
    @Autowired
    private EntityManager em;

    public OrderRepositoryImpl(EntityManager em){
        this.queryFactory=new JPAQueryFactory(em);
    }

    QMember buyer=new QMember("buyer");
    QMember seller=new QMember("seller");

    @Override
    public Slice<OrderSimpleDto> findOrdersByShoesSize(Long shoesId,Long lastOrderId,ShoesSizeSearch shoesSizeSearch,Pageable pageable) {
        List<OrderSimpleDto> orderSimpleDtos = queryFactory.select(Projections.constructor(OrderSimpleDto.class, order.createdDate, shoes.name,
                shoesSize.US, order.price,order.orderStatus,delivery.deliveryStatus)).from(order)
                .join(order.registedShoes, registedShoes)
                .join(registedShoes.shoesInSize, shoesInSize)
                .join(shoesInSize.size, shoesSize)
                .join(shoesInSize.shoes, shoes)
                .join(order.delivery, delivery)

                .where(
                        shoesSizeSearchEq(shoesSizeSearch.getShoesSize())
                        , shoes.id.eq(shoesId)
                        , order.id.lt(lastOrderId)
                        , order.orderStatus.eq(OrderStatus.Complete).or(order.orderStatus.eq(OrderStatus.Progress))
                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize()+1)
                .fetch();

        return RepositoryHelper.toSlice(orderSimpleDtos, pageable);


    }


    private BooleanExpression shoesSizeSearchEq(String shoesSizeSearch) {
        return hasText(shoesSizeSearch)?shoesSize.US.eq(shoesSizeSearch):null;
    }

    @Override
    public Page<OrderDto> findAllOrder(Pageable pageable ,OrderSearch orderSearch) {

        List<OrderDto> orderDto = queryFactory.select(Projections.constructor(OrderDto.class,
                order.id,
                buyer.username, seller.username
                , order.price, order.createdDate, order.orderStatus, shoes.name
                , shoesSize.US, delivery.address, delivery.deliveryStatus
        )).from(order)
                .join(order.buyer, buyer)
                .join(order.seller, seller)
                .join(order.registedShoes, registedShoes)
                .join(registedShoes.shoesInSize, shoesInSize)
                .join(shoesInSize.shoes, shoes)
                .join(shoesInSize.size, shoesSize)
                .join(order.delivery, delivery)
                .orderBy(order.createdDate.asc())
                .where(
                        buyerNameEq(orderSearch.getBuyerName())
                        ,sellerNameEq(orderSearch.getSellerName())
                        ,orderStatusEq(orderSearch.getOrderStatus())
                        ,deliveryStatusEq(orderSearch.getDeliveryStatus())
                        ,trackingNumberEq(orderSearch.getTrackingNumber())

                ).fetch();

        JPAQuery<Order> count = queryFactory.selectFrom(order).
                where(
                buyerNameEq(orderSearch.getBuyerName())
                , sellerNameEq(orderSearch.getSellerName()),
                orderStatusEq(orderSearch.getOrderStatus()),
                deliveryStatusEq(orderSearch.getDeliveryStatus())
                ,trackingNumberEq(orderSearch.getTrackingNumber()));

        return  PageableExecutionUtils.getPage(orderDto,pageable,()->count.fetchCount());

    }

    private BooleanExpression trackingNumberEq(String trackingNumber) {
      return  hasText(trackingNumber)?delivery.trackingNumber.eq(trackingNumber):null;
    }

    private BooleanExpression deliveryStatusEq(DeliveryStatus deliveryStatus) {
       return !ObjectUtils.isEmpty(deliveryStatus) ?delivery.deliveryStatus.eq(deliveryStatus):null;
    }

    private BooleanExpression  orderStatusEq(OrderStatus orderStatus) {
        return  !ObjectUtils.isEmpty(orderStatus)?order.orderStatus.eq(orderStatus):null;
    }

    private BooleanExpression sellerNameEq(String sellerName) {
        return hasText(sellerName)?buyer.username.eq(sellerName):null;
    }

    private BooleanExpression buyerNameEq(String buyerName) {
        return hasText(buyerName)?buyer.username.eq(buyerName):null;
    }

}
