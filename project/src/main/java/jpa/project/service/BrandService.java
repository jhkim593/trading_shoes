package jpa.project.service;

import jpa.project.advide.exception.CNotOwnerException;
import jpa.project.advide.exception.CResourceNotExistException;
import jpa.project.advide.exception.CUserNotFoundException;
import jpa.project.model.dto.brand.BrandDto;
import jpa.project.model.dto.brand.BrandRegisterDto;
import jpa.project.model.dto.brand.BrandSimpleDto;
import jpa.project.model.dto.brand.BrandUpdateDto;
import jpa.project.entity.Brand;
import jpa.project.entity.Member;
import jpa.project.repository.brand.BrandRepository;
import jpa.project.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Brand brand = getBrand(id);
        Member member = getMember(username);


        if(!member.getRoles().equals("ADMIN")){
            throw  new CNotOwnerException();
        }
        brandRepository.delete(brand);
    }

    private Member getMember(String username) {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        Member member = findMember.orElseThrow(() -> new CUserNotFoundException());
        return member;
    }

    private Brand getBrand(Long id) {
        Optional<Brand> findBrand = brandRepository.findById(id);
        return findBrand.orElseThrow(() -> new CResourceNotExistException());
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
        Brand brand = getBrand(id);
        return BrandDto.createBrandDto(brand);

    }

    @Transactional
    public void updateBrand(Long id,BrandUpdateDto brandUpdateDto){
        Brand brand = getBrand(id);
        brand.update(brandUpdateDto.getName(),brandUpdateDto.getContent());

    }

}
