package jpa.project.service;

import jpa.project.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Component
public class Init {
    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.Init();

    }


    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitService{
        private final EntityManager em;
        public void Init(){
            ShoesSize shoesSize = ShoesSize.createShoesSize("7");
            ShoesSize shoesSize1 = ShoesSize.createShoesSize("7.5");
            ShoesSize shoesSize2 = ShoesSize.createShoesSize("8");
            ShoesSize shoesSize3 = ShoesSize.createShoesSize("8.5");
            ShoesSize shoesSize4 = ShoesSize.createShoesSize("9");
            ShoesSize shoesSize5 = ShoesSize.createShoesSize("9.5");
            ShoesSize shoesSize6 = ShoesSize.createShoesSize("10");
            ShoesSize shoesSize7 = ShoesSize.createShoesSize("10.5");
            ShoesSize shoesSize8 = ShoesSize.createShoesSize("11");
            ShoesSize shoesSize9 = ShoesSize.createShoesSize("11.5");
            ShoesSize shoesSize10 = ShoesSize.createShoesSize("12");

            em.persist(shoesSize);
            em.persist(shoesSize1);
            em.persist(shoesSize2);
            em.persist(shoesSize3);
            em.persist(shoesSize4);
            em.persist(shoesSize5);
            em.persist(shoesSize6);
            em.persist(shoesSize7);
            em.persist(shoesSize8);
            em.persist(shoesSize9);
            em.persist(shoesSize10);


            Brand asics = Brand.createBrand("asics", "111");
            em.persist(asics);

//


            Shoes shoes = Shoes.createShoes("1", asics, ShoesInSize.createShoesInSize(shoesSize), ShoesInSize.createShoesInSize(shoesSize1), ShoesInSize.createShoesInSize(shoesSize2),
                    ShoesInSize.createShoesInSize(shoesSize3), ShoesInSize.createShoesInSize(shoesSize4), ShoesInSize.createShoesInSize(shoesSize5), ShoesInSize.createShoesInSize(shoesSize6), ShoesInSize.createShoesInSize(shoesSize7), ShoesInSize.createShoesInSize(shoesSize8),
                    ShoesInSize.createShoesInSize(shoesSize9), ShoesInSize.createShoesInSize(shoesSize10));
            em.persist(shoes);


        }

    }
}
