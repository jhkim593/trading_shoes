package jpa.project.Service;

import jpa.project.advide.exception.CResourceNotExistException;
import jpa.project.advide.exception.CUserNotFoundException;
import jpa.project.dto.RegistedShoes.RegistedShoesDto;
import jpa.project.entity.Member;
import jpa.project.entity.RegistedShoes;
import jpa.project.entity.ShoesInSize;
import jpa.project.entity.TradeStatus;
import jpa.project.repository.member.MemberRepository;
import jpa.project.repository.registedShoes.RegistedShoesRepository;
import jpa.project.repository.shoesInSize.ShoesInSizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegistedShoesService {
    private final RegistedShoesRepository registedShoesRepository;
    private final MemberRepository memberRepository;
    private final ShoesInSizeRepository shoesInSizeRepository;

    //판매입찰
    @Transactional
    public RegistedShoesDto registShoes(String username, Long shoesInSizeId, int price,TradeStatus tradeStatus){
        Member member = getMember(username);


        ShoesInSize shoesInSize = getShoesInSize(shoesInSizeId);

        RegistedShoes registedShoes = RegistedShoes.createRegistedShoes(member, shoesInSize, price, tradeStatus);
        registedShoesRepository.save(registedShoes);


        /**최적화**/
        /****/
        /****/
        /****/
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
    public void update(Long registedShoesId,int price){
        RegistedShoes registedShoes = getRegistedShoes(registedShoesId);
        registedShoes.changePrice(price,registedShoes.getTradeStatus(),registedShoes.getShoesInSize());
    }

    private RegistedShoes getRegistedShoes(Long registedShoesId) {
        Optional<RegistedShoes> findRegistedShoes = registedShoesRepository.findById(registedShoesId);
        return findRegistedShoes.orElseThrow(CResourceNotExistException::new);
    }

    @Transactional
    public void delete(Long registedShoesId){
        RegistedShoes registedShoes = getRegistedShoes(registedShoesId);
        registedShoesRepository.delete(registedShoes);

        if(registedShoes.getTradeStatus().equals(TradeStatus.SELL)) {
            if (registedShoesRepository.findSellOrderByPrice() == registedShoes.getPrice()) {
                registedShoes.getShoesInSize().justChangeLowestPrice(registedShoesRepository.findSellOrderByPrice());
            }
        }
        else{
            if(registedShoesRepository.findBuyOrderByPrice()==registedShoes.getPrice()){
                registedShoes.getShoesInSize().changeHighestPrice(registedShoesRepository.findBuyOrderByPrice());
            }
        }


    }

    public List<RegistedShoesDto> findByUsername(String username){
        List<RegistedShoes> findRegistedShoesList = registedShoesRepository.findByUsername(username);
       return findRegistedShoesList.stream().map(r -> RegistedShoesDto.createRegistedShoesDto(r)).collect(Collectors.toList());
    }

}
