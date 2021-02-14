package jpa.project.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jpa.project.Service.ResponseService;
import jpa.project.Service.ShoesService;
import jpa.project.dto.RegistedShoes.RegistedShoesDto;
import jpa.project.dto.Shoes.ShoesDto;
import jpa.project.dto.Shoes.ShoesRegisterRequestDto;
import jpa.project.dto.Shoes.ShoesUpdateDto;
import jpa.project.dto.Shoes.ShoesWithSizeDto;
import jpa.project.repository.search.ShoesSearch;
import jpa.project.response.CommonResult;
import jpa.project.response.ListResult;
import jpa.project.response.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Api(tags = {"5.Shoes"})
public class ShoesApiController {
    private final ShoesService shoesService;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value ="신발등록" ,notes = "관리자가 신발을 등록한다.")
    @PostMapping("/admin/shoes")
    public SingleResult<ShoesDto> save(@Valid @ModelAttribute ShoesRegisterRequestDto srrDto){
        return responseService.getSingResult(shoesService.saveShoes(srrDto));

    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value ="신발 수정" ,notes = "관리자가 신발을 수정한다.")
    @PutMapping("/admin/shoes/{id}")
    public CommonResult update(@PathVariable("id")Long id, @ModelAttribute ShoesUpdateDto shoesUpdateDto){
        shoesService.updateShoes(id,shoesUpdateDto);
        return responseService.getSuccessResult();
    }
    @ApiOperation(value = "신발 조회",notes="신발을 조회한다")
    @GetMapping("/shoes")
    public SingleResult<Page<ShoesDto>>search(ShoesSearch shoesSearch,Pageable pageable){
        return responseService.getSingResult(shoesService.search(shoesSearch,pageable));
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value ="판매 신발 등록" ,notes = "회원이 판매 할 신발을 등록한다")
    @PostMapping("/shoes/{id}")
    public SingleResult<RegistedShoesDto>registShoes(@PathVariable("id")Long id,int price){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return responseService.getSingResult(shoesService.registShoes(username,id,price));
    }
    @ApiOperation(value ="신발 단건 조회" , notes = "신발 사이즈내 등록상품 단건 조회한다")
    @GetMapping("/shoes/{id}")
   public SingleResult<ShoesWithSizeDto>detail(@PathVariable("id")Long id){
        return responseService.getSingResult(shoesService.detailShoes(id));
    }



}
