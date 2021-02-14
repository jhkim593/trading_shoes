package jpa.project.Service;

import jpa.project.advide.exception.CResourceNotExistException;
import jpa.project.advide.exception.CShoesAlreadyExistException;
import jpa.project.advide.exception.CUserNotFoundException;
import jpa.project.dto.RegistedShoes.RegistedShoesDto;
import jpa.project.dto.Shoes.ShoesDto;
import jpa.project.dto.Shoes.ShoesRegisterRequestDto;
import jpa.project.dto.Shoes.ShoesUpdateDto;
import jpa.project.dto.Shoes.ShoesWithSizeDto;
import jpa.project.entity.*;
import jpa.project.repository.*;
import jpa.project.repository.search.ShoesSearch;
import jpa.project.repository.shoes.ShoesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoesService {
    private final ShoesRepository shoesRepository;
    private final ShoesSizeRepository shoesSizeRepository;
    private final BrandRepository brandRepository;
    private final MemberRepository memberRepository;
    private final ShoesInSizeRepository shoesInSizeRepository;
    private final RegistedShoesRepository registedShoesRepository;


//   @Transactional
//    public ShoesDto saveShoes(ShoesRegisterRequestDto srrDto){
//       Optional<Brand> findBrand = brandRepository.findByName(srrDto.getName());
//       Brand brand = findBrand.orElseThrow(() -> new CResourceNotExistException());
//       Shoes shoes = Shoes.createShoes(srrDto.getName(), brand);
//       return ShoesDto.createShoesDto(shoes);
//   }

    /**
     CASE CADE 설정 해야함**/
    @Transactional
    public ShoesDto saveShoes(ShoesRegisterRequestDto srrDto){
        Optional<Shoes> findShoes = shoesRepository.findByName(srrDto.getName());
        if(findShoes.isPresent()){
            throw new CShoesAlreadyExistException();
        }
        Optional<Brand> findBrand = brandRepository.findByName(srrDto.getBrand());
        Brand brand = findBrand.orElseThrow(() -> new CResourceNotExistException());
        List<ShoesSize> shoesSizes = shoesSizeRepository.findAll();
        List<ShoesInSize>shoesInSizeList= new ArrayList<>();
        for (ShoesSize shoesSize : shoesSizes) {
            shoesInSizeList.add(ShoesInSize.createShoesInSize(shoesSize));
        }
        Shoes shoes = Shoes.createShoes(srrDto.getName(), brand, shoesInSizeList.get(0), shoesInSizeList.get(1), shoesInSizeList.get(2), shoesInSizeList.get(3), shoesInSizeList.get(4), shoesInSizeList.get(5), shoesInSizeList.get(6)
                , shoesInSizeList.get(7), shoesInSizeList.get(8), shoesInSizeList.get(9), shoesInSizeList.get(10));
        shoesRepository.save(shoes);
        return ShoesDto.createShoesDto(shoes);
    }

    @Transactional
    public void updateShoes(Long id, ShoesUpdateDto shoesUpdateDto){
        Optional<Shoes> findShoes = shoesRepository.findById(id);
        Shoes shoes = findShoes.orElseThrow(CResourceNotExistException::new);
        shoes.update(shoesUpdateDto.getName());

    }


    @Transactional
    public void deleteShoes(Long id){
       shoesRepository.deleteById(id);
   }

   @Transactional
    public RegistedShoesDto registShoes(String username, Long shoesInSizeId, int price){
       Optional<Member> findMember = memberRepository.findByUsername(username);
       Member member = findMember.orElseThrow(CUserNotFoundException::new);


       Optional<ShoesInSize> findShoesInSize = shoesInSizeRepository.findById(shoesInSizeId);
       ShoesInSize shoesInSize = findShoesInSize.orElseThrow(CResourceNotExistException::new);

       RegistedShoes registedShoes = RegistedShoes.createRegistedShoes(member, shoesInSize, price);
       registedShoesRepository.save(registedShoes);
       /**최적화**/
       /****/
       /****/
       /****/
       return RegistedShoesDto.createRegistedShoesDto(shoesInSize.getShoes().getName(),username,shoesInSize.getSize().getUS(),price);

   }

   public Page<ShoesDto>search(ShoesSearch shoesSearch, Pageable pageable){
       return shoesRepository.search(shoesSearch,pageable);
   }
   public ShoesWithSizeDto detailShoes(Long shoesId){

      return  shoesRepository.detailShoesWithSizeDto(shoesId);

   }

}
