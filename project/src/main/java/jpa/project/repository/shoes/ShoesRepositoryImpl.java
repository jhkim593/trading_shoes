package jpa.project.repository.shoes;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpa.project.dto.RegistedShoes.RegistedShoesDto;
import jpa.project.dto.RegistedShoes.RegistedShoesSimpleDto;
import jpa.project.dto.Shoes.ShoesDto;
import jpa.project.dto.Shoes.ShoesInSizeDto;
import jpa.project.dto.Shoes.ShoesWithSizeDto;
import jpa.project.entity.*;
import jpa.project.repository.search.ShoesSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

import static jpa.project.entity.QBrand.*;
import static jpa.project.entity.QRegistedShoes.*;
import static jpa.project.entity.QShoes.*;
import static jpa.project.entity.QShoesInSize.*;
import static jpa.project.entity.QShoesSize.*;
import static org.springframework.util.StringUtils.*;


public class ShoesRepositoryImpl implements ShoesRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    private EntityManager em;
    public ShoesRepositoryImpl(EntityManager em){
        this.queryFactory=new JPAQueryFactory(em);
    }

    @Override
    public Page<ShoesDto> search(ShoesSearch shoesSearch, Pageable pageable) {
        List<ShoesDto> shoesDto = queryFactory.select(Projections.bean(ShoesDto.class,
                shoes.id,
                shoes.name,
                brand.name.as("brand"),
                shoes.createdDate
        )).from(shoes)
                .join(shoes.brand, brand)
                .where(
                        shoesnameEq(shoesSearch.getShoesname()),
                        brandnameEq(shoesSearch.getBrandname())
                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Shoes> count = queryFactory.selectFrom(shoes)
                .join(shoes.brand, brand).fetchJoin()
                .where(shoesnameEq(shoesSearch.getShoesname()),
                        brandnameEq(shoesSearch.getBrandname()));
        return PageableExecutionUtils.getPage(shoesDto,pageable,()->count.fetchCount());

    }



    private BooleanExpression shoesnameEq(String shoesname) {
       return  hasText(shoesname)?shoes.name.eq(shoesname):null;
    }


    private BooleanExpression brandnameEq(String brandname) {
        return hasText(brandname)?brand.name.eq(brandname):null;
    }




//    }
//
//    public List<RegistedShoesSimpleDto> findRegistedShoes(Long shoesInSizeId){
//      return  queryFactory.select(Projections.constructor(RegistedShoesSimpleDto.class,
//                registedShoes.id,
//                registedShoes.price)).from(registedShoes).where(registedShoes.shoesStatus.eq(ShoesStatus.BID))
//                .fetch();
//    }
//
//    public List<ShoesInSizeDto>findShoesInSize(Long ShoesId){
//        return queryFactory.select(Projections.constructor(ShoesInSizeDto.class,
//                shoesInSize.id
//                ,shoesSize.US
//                ,shoesInSize.stockQuantity
//                ,shoesInSize.lowestPrice)).from(shoesInSize)
//                .rightJoin(shoesInSize.size, shoesSize ).fetchJoin().fetch();

//    }
    public Shoes detailShoes(Long id){
        Shoes detailShoes = queryFactory.selectFrom(QShoes.shoes).distinct()
                .join(QShoes.shoes.shoesInSizes, shoesInSize).fetchJoin()
                .join(QShoes.shoes.brand, brand).fetchJoin()
                .join(shoesInSize.size, shoesSize).fetchJoin()
                .where(QShoes.shoes.id.eq(id))
                .fetchOne();
        return detailShoes;
    }
    @Override
    public ShoesWithSizeDto detailShoesWithSizeDto(Long id) {
        Shoes detailShoes= detailShoes(id);
        return new ShoesWithSizeDto(detailShoes);
//        QShoes.shoes.stream().map(s->new ShoesWithSizeDto(s)).collect(Collectors.toList());
    }


}
