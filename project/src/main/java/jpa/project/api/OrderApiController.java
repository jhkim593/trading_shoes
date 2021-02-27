package jpa.project.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jpa.project.service.OrderService;
import jpa.project.service.ResponseService;
import jpa.project.dto.order.OrderDto;
import jpa.project.dto.order.OrderSimpleDto;
import jpa.project.repository.search.ShoesSizeSearch;
import jpa.project.response.ListResult;
import jpa.project.response.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"7.Order"})
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderService orderService;
    private final ResponseService responseService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value = "주문생성" ,notes = "구매자가 신발을 주문한다.")
    @PostMapping("/order/{id}")
    public SingleResult<OrderDto> order(@PathVariable("id")Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return responseService.getSingResult(orderService.save(username,id));

    }

    @ApiOperation(value = "판매 주문내역" ,notes = "회원이 판매한 주문을 조회한다.")
    @GetMapping("/orders/sale/{id}")
    public ListResult<OrderSimpleDto> salesOrders(@PathVariable("id")Long id){
        return responseService.getListResult(orderService.findSoldOrder(id));

    }
    @ApiOperation(value = "구매 주문내역" ,notes = "회원이 구매한 주문을 조회한다.")
    @GetMapping("/orders/purchase/{id}")
    public ListResult<OrderSimpleDto> purchaseOrders(@PathVariable("id")Long id){
        return responseService.getListResult(orderService.findPurchasedOrder(id));

    }
    @ApiOperation(value = "주문 상세정보" ,notes = "주문 상세정보를 조회한다.")
    @GetMapping("/order/{id}")
        public SingleResult<OrderDto>detail(@PathVariable("id")Long id){
        return responseService.getSingResult(orderService.detail(id));
    }
    @ApiOperation(value = "신발 거래 내역" ,notes = "거래된 신발 정보를 표시한다.")
    @GetMapping("/orders/shoes/{shoesId}")
    public ListResult<OrderSimpleDto>findOrderByShoesSizeSearch(@PathVariable("shoesId")Long shoesId, ShoesSizeSearch shoesSizeSearch){

        return responseService.getListResult(orderService.findOrdersByShoesSize(shoesId,shoesSizeSearch));
    }


}
