package jpa.project.service;

import jpa.project.cache.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CacheService {


        @Caching(evict = {
                @CacheEvict(value = CacheKey.COMMENTS, key = "#boardId")
        })
        public void deleteCommentCache(Long boardId) {
            log.debug("deleteCommentCache - boardId {}", boardId);

        }


    @Caching(evict = {
            @CacheEvict(value = CacheKey.ORDERS_PURCHASE, key = "#buyerId", allEntries = true),
            @CacheEvict(value = CacheKey.ORDERS_SALES, key = "#sellerId", allEntries = true),
            @CacheEvict(value = CacheKey.ORDERS, key = "#shoesId"),
            @CacheEvict(value=  CacheKey.ORDER,  key = "#orderId")
    })
    public void deleteOrderCache(Long buyerId,Long sellerId,Long shoesId,Long orderId ){
            log.debug("deleteOrderCache -buyerId {},sellerId {} , shoesId{}, orderId{}");
    }


    @Caching(evict = {
            @CacheEvict(value = CacheKey.REGISTEDSHOES_LIST, key = "#memberId", allEntries = true),
            @CacheEvict(value=  CacheKey.REGISTEDSHOES    ,key="#registedShoesId")
    })
    public void deleteRegistedShoesCache(Long memberId ,Long registedShoesId){
            log.debug("deleteRegistedShoesCache -memberId {} ,-registedShoesId{}");
    }


}
