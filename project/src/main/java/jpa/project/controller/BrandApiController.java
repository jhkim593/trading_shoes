package jpa.project.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jpa.project.service.BrandService;
import jpa.project.service.ResponseService;
import jpa.project.model.dto.brand.BrandDto;
import jpa.project.model.dto.brand.BrandRegisterDto;
import jpa.project.model.dto.brand.BrandUpdateDto;
import jpa.project.response.CommonResult;
import jpa.project.response.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Api(tags = {"4.brand"})
public class BrandApiController {

    private final BrandService brandService;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value ="브랜드 등록" ,notes = "브랜드를 등록한다")
    @PostMapping("/admin/brand")
    public SingleResult<BrandDto> save(@Valid @ModelAttribute BrandRegisterDto brandRegisterDto){
        return responseService.getSingResult(brandService.save(brandRegisterDto));
    }

    @ApiOperation(value="브랜드 단건조회",notes="브랜드를 단건 조회한다")
    @GetMapping("/brand")
    public SingleResult<BrandDto>findById(@PathVariable("id")Long id){
        return responseService.getSingResult(brandService.findById(id));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value="브랜드 삭제",notes="브랜드를 삭제한다")
    @DeleteMapping("/admin/brand/{id}")
    public CommonResult delete(@PathVariable("id")Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
       brandService.delete(id,username);
       return responseService.getSuccessResult();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value="브랜드 수정",notes="브랜드를 수정한다")
    @PutMapping("/admin/brand")
    public CommonResult update(@PathVariable("id")Long id, @ModelAttribute BrandUpdateDto brandUpdateDto) {
        brandService.updateBrand(id, brandUpdateDto);
        return responseService.getSuccessResult();

    }
}
