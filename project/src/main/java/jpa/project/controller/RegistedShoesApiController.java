package jpa.project.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jpa.project.entity.TradeStatus;
import jpa.project.model.dto.registedShoes.RegistedShoesDto;
import jpa.project.response.CommonResult;
import jpa.project.response.SingleResult;
import jpa.project.service.RegistedShoesService;
import jpa.project.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @ApiOperation(value ="신발 입찰 단건조회" ,notes = "신발 입찰 단건조회")
    @GetMapping("/registerShoes/{id}")
    public SingleResult<RegistedShoesDto> findRegistedShoes(@PathVariable("id")Long id){
        return responseService.getSingResult(registedShoesService.find(id));
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        registedShoesService.update(id,username,price);
       return responseService.getSuccessResult();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value ="신발 입찰 삭제" ,notes = "등록한 입찰정보를 삭제한다.")
    @DeleteMapping("/registerShoes/{id}")
    public CommonResult deleteRegitedShoes(@PathVariable("id")Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        registedShoesService.delete(id,username);
        return responseService.getSuccessResult();

    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value ="신발 입찰 조회" ,notes = "등록한 신발 입찰정보 조회")
    @GetMapping("/registerShoes/member/{memberId}")
    public SingleResult<Slice<RegistedShoesDto>>findRegistedShoesByTradeStatus(@PathVariable("memberId")Long memberId,TradeStatus tradeStatus,
                                                                               @RequestParam(value = "lastRegistedShoesId",required = false)Long lastRegistedShoesId,
                                                                               @RequestParam(value = "limit",defaultValue = "15")int limit){
       return responseService.getSingResult( registedShoesService.findRegistedShoesByTradeStatus(memberId,lastRegistedShoesId,tradeStatus,limit));
    }


}
