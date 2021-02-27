package jpa.project.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jpa.project.Service.RegistedShoesService;
import jpa.project.Service.ResponseService;
import jpa.project.dto.RegistedShoes.RegistedShoesDto;
import jpa.project.entity.TradeStatus;
import jpa.project.response.CommonResult;
import jpa.project.response.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = {"6.RegistShoes"})
public class RegistedShoesApiController {
    private final ResponseService responseService;
    private final RegistedShoesService registedShoesService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value ="신발 판매입찰" ,notes = "회원이 신발 판매입찰을 등록한다.")
    @PostMapping("/registerShoes/sell/{id}")
    public SingleResult<RegistedShoesDto> registSellShoes(@PathVariable("id")Long id, int price){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

       return responseService.getSingResult(registedShoesService.registShoes(username,id,price, TradeStatus.SELL));
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value ="신발 구매입찰" ,notes = "회원이 신발 구매입찰을 등록한다.")
    @PostMapping("/registerShoes/buy/{id}")
    public SingleResult<RegistedShoesDto> registBuyShoes(@PathVariable("id")Long id, int price){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return responseService.getSingResult(registedShoesService.registShoes(username,id,price,TradeStatus.BUY));
    }



    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value ="신발 입찰 가격변경" ,notes = "입찰 가격 변경한다.")
    @PutMapping("/registerShoes/{id}")
    public CommonResult updateRegistedShoes(@PathVariable("id")Long id, int price){
        registedShoesService.update(id,price);
       return responseService.getSuccessResult();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value ="신발 입찰 삭제" ,notes = "등록한 입찰정보를 삭제한다.")
    @DeleteMapping("/registerShoes/{id}")
    public CommonResult deleteRegitedShoes(@PathVariable("id")Long id){
        registedShoesService.delete(id);
        return responseService.getSuccessResult();

    }

}
