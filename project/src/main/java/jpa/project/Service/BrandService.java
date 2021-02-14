package jpa.project.Service;

import jpa.project.advide.exception.CNotOwnerException;
import jpa.project.advide.exception.CResourceNotExistException;
import jpa.project.advide.exception.CUserNotFoundException;
import jpa.project.dto.brand.BrandDto;
import jpa.project.dto.brand.BrandRegisterDto;
import jpa.project.dto.brand.BrandSimpleDto;
import jpa.project.dto.brand.BrandUpdateDto;
import jpa.project.entity.Board;
import jpa.project.entity.Brand;
import jpa.project.entity.Member;
import jpa.project.repository.BrandRepository;
import jpa.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandService {
    private final BrandRepository brandRepository;
    private final MemberRepository memberRepository;



    @Transactional
    public BrandDto save(BrandRegisterDto brandRegisterDto){
        Brand brand = Brand.createBrand(brandRegisterDto.getName(), brandRegisterDto.getContent());
        brandRepository.save(brand);
        return BrandDto.createBrandDto(brand);

    }
    @Transactional
    public void delete(Long id, String username){
        Optional<Brand> findBrand =brandRepository.findById(id);
        Optional<Member> findMember = memberRepository.findByUsername(username);
        Member member = findMember.orElseThrow(() -> new CUserNotFoundException());

        Brand brand = findBrand.orElseThrow(() -> new CResourceNotExistException());
        if(!member.getRoles().equals("ADMIN")){
            throw  new CNotOwnerException();
        }
        brandRepository.delete(brand);
    }

    public List<BrandSimpleDto> findAll(){
        List<Brand> brands = brandRepository.findAll();
//        List<BrandSimpleDto> brandSimpleDto=new ArrayList<>();
//        for (Brand brand : brands) {
//            BrandSimpleDto brandSimpleDto1 = BrandSimpleDto.createBrandSimpleDto(brand);
//            brandSimpleDto.add(brandSimpleDto1);
//        }
        List<BrandSimpleDto> brandSimpleDtos = brands.stream().map(b -> BrandSimpleDto.createBrandSimpleDto(b)).collect(Collectors.toList());

        return brandSimpleDtos;

    }

    public BrandDto findById(Long id){
        Optional<Brand> findBrand = brandRepository.findById(id);
        Brand brand = findBrand.orElseThrow(() -> new CResourceNotExistException());
        return BrandDto.createBrandDto(brand);

    }

    @Transactional
    public void updateBrand(Long id,BrandUpdateDto brandUpdateDto){
        Optional<Brand> findBrand = brandRepository.findById(id);
        Brand brand = findBrand.orElseThrow(() -> new CResourceNotExistException());
        brand.update(brandUpdateDto.getName(),brandUpdateDto.getContent());

    }

}
