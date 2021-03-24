package jpa.project.service;

import jpa.project.advide.exception.CResourceNotExistException;
import jpa.project.advide.exception.CShoesAlreadyExistException;
import jpa.project.entity.Brand;
import jpa.project.entity.Shoes;
import jpa.project.entity.ShoesInSize;
import jpa.project.entity.ShoesSize;
import jpa.project.model.dto.Shoes.*;
import jpa.project.repository.ShoesSize.ShoesSizeRepository;
import jpa.project.repository.brand.BrandRepository;
import jpa.project.repository.member.MemberRepository;
import jpa.project.repository.registedShoes.RegistedShoesRepository;
import jpa.project.repository.search.ShoesSearch;
import jpa.project.repository.shoes.ShoesRepository;
import jpa.project.repository.shoesInSize.ShoesInSizeRepository;
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


    /**
     CASE CADE 설정 해야함**/
    @Transactional
    public ShoesSimpleDto saveShoes(ShoesRegisterRequestDto srrDto){
        Optional<Shoes> findShoes = shoesRepository.findByName(srrDto.getName());
        if(findShoes.isPresent()){
            throw new CShoesAlreadyExistException();
        }
        Brand brand = getBrand(srrDto);
        List<ShoesSize> shoesSizes = shoesSizeRepository.findAll();
        List<ShoesInSize>shoesInSizeList= new ArrayList<>();
        for (ShoesSize shoesSize : shoesSizes) {
            shoesInSizeList.add(ShoesInSize.createShoesInSize(shoesSize));
        }
        Shoes shoes = Shoes.createShoes(srrDto.getName(), brand, shoesInSizeList.get(0), shoesInSizeList.get(1), shoesInSizeList.get(2), shoesInSizeList.get(3), shoesInSizeList.get(4), shoesInSizeList.get(5), shoesInSizeList.get(6)
                , shoesInSizeList.get(7), shoesInSizeList.get(8), shoesInSizeList.get(9), shoesInSizeList.get(10));
        shoesRepository.save(shoes);
        return ShoesSimpleDto.createShoesDto(shoes);
    }

    private Brand getBrand(ShoesRegisterRequestDto srrDto) {
        Optional<Brand> findBrand = brandRepository.findByName(srrDto.getBrand());
        Brand brand = findBrand.orElseThrow(() -> new CResourceNotExistException());
        return brand;
    }

    @Transactional
    public void updateShoes(Long id, ShoesUpdateDto shoesUpdateDto){
        Shoes shoes = getShoes(id);
        shoes.update(shoesUpdateDto.getName());

    }

    private Shoes getShoes(Long id) {
        Optional<Shoes> findShoes = shoesRepository.findById(id);
        Shoes shoes = findShoes.orElseThrow(CResourceNotExistException::new);
        return shoes;
    }


    @Transactional
    public void deleteShoes(Long id){
       shoesRepository.deleteById(id);
   }



   public Page<ShoesSimpleDto>search(ShoesSearch shoesSearch, Pageable pageable){
       return shoesRepository.search(shoesSearch,pageable);
   }
   @Transactional
   public ShoesDto detailSellShoes(Long shoesId){
        getShoes(shoesId).addView();
        return  shoesRepository.detailShoesDto(shoesId);

   }


}
