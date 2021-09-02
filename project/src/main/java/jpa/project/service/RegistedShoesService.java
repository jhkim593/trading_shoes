package jpa.project.service;

import jpa.project.advide.exception.CNotOwnerException;
import jpa.project.advide.exception.CResourceNotExistException;
import jpa.project.advide.exception.CUserNotFoundException;
import jpa.project.cache.CacheKey;
import jpa.project.entity.*;
import jpa.project.model.dto.registedShoes.RegistedShoesDto;
import jpa.project.repository.member.MemberRepository;
import jpa.project.repository.registedShoes.RegistedShoesRepository;
import jpa.project.repository.shoesInSize.ShoesInSizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegistedShoesService {
    private final RegistedShoesRepository registedShoesRepository;
    private final MemberRepository memberRepository;
    private final ShoesInSizeRepository shoesInSizeRepository;
    private final CacheService cacheService;

   //입찰
    @Transactional
    public RegistedShoesDto registShoes(String username, Long shoesInSizeId, int price,TradeStatus tradeStatus){
        Member member = getMember(username);
        ShoesInSize shoesInSize = getShoesInSize(shoesInSizeId);
        RegistedShoes registedShoes = RegistedShoes.createRegistedShoes(member, shoesInSize, price, tradeStatus);

        registedShoesRepository.save(registedShoes);
        cacheService.deleteRegistedShoesCache(member.getId(), registedShoes.getId());
        if(registedShoes.getTradeStatus().equals(TradeStatus.SELL))
        changeShoesPrice(registedShoes.getShoesInSize().getShoes());

        return RegistedShoesDto.createRegistedShoesDto(registedShoes);

    }

    private ShoesInSize getShoesInSize(Long shoesInSizeId) {
        Optional<ShoesInSize> findShoesInSize = shoesInSizeRepository.findById(shoesInSizeId);
        ShoesInSize shoesInSize = findShoesInSize.orElseThrow(CResourceNotExistException::new);
        return shoesInSize;
    }

    private Member getMember(String username) {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        Member member = findMember.orElseThrow(CUserNotFoundException::new);
        return member;
    }


    @Transactional
    public void update(Long registedShoesId,String username,int price){
        RegistedShoes registedShoes = getRegistedShoes(registedShoesId);
        if (!username.equals(registedShoes.getMember().getUsername())) {
            throw new CNotOwnerException();
        }
        cacheService.deleteRegistedShoesCache(registedShoes.getMember().getId(),registedShoesId);
        registedShoes.changePrice(price);
        changeShoesPrice(registedShoes.getShoesInSize().getShoes());

    }
    @Cacheable(value = CacheKey.REGISTEDSHOES, key = "#registedShoesId",unless ="#result==null")
    public RegistedShoesDto find(Long registedShoesId){
        RegistedShoes registedShoes = getRegistedShoes(registedShoesId);
        return RegistedShoesDto.createRegistedShoesDto(registedShoes);
    }

    private void changeShoesPrice(Shoes shoes) {
        Optional<RegistedShoes> optionalRegistedShoes = registedShoesRepository.findLowestPriceInShoes(shoes.getId());
        if(!optionalRegistedShoes.isEmpty())
       shoes.changePrice(optionalRegistedShoes.orElseThrow(CResourceNotExistException::new).getPrice());
    }

    private RegistedShoes getRegistedShoes(Long registedShoesId) {
        Optional<RegistedShoes> findRegistedShoes = registedShoesRepository.findById(registedShoesId);
        return findRegistedShoes.orElseThrow(CResourceNotExistException::new);
    }

    @Transactional
    public void delete(Long registedShoesId,String username){
        RegistedShoes registedShoes = getRegistedShoes(registedShoesId);
        if (!username.equals(registedShoes.getMember().getUsername())) {
            throw new CNotOwnerException();
        }
        cacheService.deleteRegistedShoesCache(registedShoes.getMember().getId(),registedShoesId);
        Shoes shoes = registedShoes.getShoesInSize().getShoes();
        registedShoesRepository.delete(registedShoes);
        changeShoesPrice(shoes);

    }

    @Cacheable(value = CacheKey.REGISTEDSHOES_LIST, key = "{#memberId,#tradeStatus,#lastRegistedShoesId,#limit}",unless ="#result==null")
    public Slice<RegistedShoesDto> findRegistedShoesByTradeStatus(Long memberId,Long lastRegistedShoesId,TradeStatus tradeStatus, int limit){
       return registedShoesRepository.findRegistedShoesByTradeStatus(memberId,lastRegistedShoesId != null ? lastRegistedShoesId : Long.MAX_VALUE ,tradeStatus, PageRequest.of(0,limit));
    }



}
