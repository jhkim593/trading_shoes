package jpa.project.repository.shoes;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpa.project.entity.Shoes;
import jpa.project.model.dto.shoes.ShoesDto;
import jpa.project.model.dto.shoes.ShoesSimpleDto;
import jpa.project.repository.search.ShoesSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static jpa.project.entity.QBrand.brand;
import static jpa.project.entity.QShoes.shoes;
import static jpa.project.entity.QShoesInSize.shoesInSize;
import static jpa.project.entity.QShoesSize.shoesSize;
import static org.springframework.util.StringUtils.hasText;



public class ShoesRepositoryImpl implements ShoesRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    private EntityManager em;
    public ShoesRepositoryImpl(EntityManager em){
        this.queryFactory=new JPAQueryFactory(em);
    }

    @Override
    public Page<ShoesSimpleDto> search(ShoesSearch shoesSearch, Pageable pageable) {
        List<ShoesSimpleDto> shoesSimpleDto = queryFactory.select(Projections.bean(ShoesSimpleDto.class,
                shoes.id,
                shoes.name,
                brand.name.as("brand"),
                shoes.createdDate,
                shoes.price
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
        return PageableExecutionUtils.getPage(shoesSimpleDto,pageable,()->count.fetchCount());

    }




    private BooleanExpression shoesnameEq(String shoesname) {
       return  hasText(shoesname)?shoes.name.eq(shoesname):null;
    }


    private BooleanExpression brandnameEq(String brandname) {
        return hasText(brandname)?brand.name.eq(brandname):null;
    }




//
    public Shoes detailShoes(Long id){
        Shoes detailShoes = queryFactory.selectFrom(shoes).distinct()
                .join(shoes.shoesInSizes, shoesInSize).fetchJoin()
                .join(shoes.brand, brand).fetchJoin()
                .join(shoesInSize.size, shoesSize).fetchJoin()
                .where(shoes.id.eq(id))
                .fetchOne();
        return detailShoes;
    }
    @Override
    public ShoesDto detailShoesDto(Long shoesId) {
        Shoes detailShoes= detailShoes(shoesId);
        return new ShoesDto(detailShoes);

    }




}
