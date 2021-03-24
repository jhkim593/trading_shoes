package jpa.project.controller;

import io.swagger.annotations.*;
import jpa.project.model.dto.delivery.DeliveryRegisterRequestDto;
import jpa.project.entity.DeliveryStatus;
import jpa.project.repository.search.OrderSearch;
import jpa.project.response.CommonResult;
import jpa.project.service.OrderService;
import jpa.project.service.ResponseService;
import jpa.project.model.dto.order.OrderDto;
import jpa.project.model.dto.order.OrderSimpleDto;
import jpa.project.repository.search.ShoesSizeSearch;
import jpa.project.response.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public SingleResult<Slice<OrderSimpleDto>> salesOrders(@PathVariable("id")Long id
                                                   ,@RequestParam(value = "lastOrdersId",required = false)Long lastOrdersId,
                                                  @RequestParam(value = "limit",defaultValue = "15")int limit){
        return responseService.getSingResult(orderService.findSoldOrder(id,lastOrdersId,limit));

    }
    @ApiOperation(value = "구매 주문내역" ,notes = "회원이 구매한 주문을 조회한다.")
    @GetMapping("/orders/purchase/{id}")
    public SingleResult<Slice<OrderSimpleDto>> purchaseOrders(@PathVariable("id")Long id
                                                      , @RequestParam(value = "lastOrdersId",required = false)Long lastOrdersId,
                                                              @RequestParam(value = "limit",defaultValue = "15")int limit){
        return responseService.getSingResult(orderService.findPurchasedOrder(id,lastOrdersId,limit));

    }
    @ApiOperation(value = "주문 상세정보" ,notes = "주문 상세정보를 조회한다.")
    @GetMapping("/order/{id}")
        public SingleResult<OrderDto>detail(@PathVariable("id")Long id){
        return responseService.getSingResult(orderService.detail(id));
    }
    @ApiOperation(value = "신발 거래 내역" ,notes = "거래된 신발 정보를 표시한다.")
    @GetMapping("/orders/shoes/{shoesId}")
    public SingleResult<Slice<OrderSimpleDto>>findOrderByShoesSizeSearch(@PathVariable("shoesId")Long shoesId, ShoesSizeSearch shoesSizeSearch,
                                                                         @RequestParam(value = "lastOrdersId",required = false)Long lastOrdersId,
                                                                         @RequestParam(value = "limit",defaultValue = "15")int limit){

        return responseService.getSingResult(orderService.findOrdersByShoesSize(shoesId,lastOrdersId,shoesSizeSearch,limit));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value = "판매자 주문 배송정보입력" ,notes = "거래된 신발 주문 배송정보를 입력한다.")
    @PutMapping("/order/delivery/{orderId}")
    public CommonResult addDeliveryInfo(@PathVariable("orderId")Long orderId, @ModelAttribute DeliveryRegisterRequestDto registerRequestDto){
        orderService.addDeliveryInfo(orderId,registerRequestDto);
       return responseService.getSuccessResult();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value = "관리자 주문 배송상태변경" ,notes = "관리자가 주문 배송상태를 변경한다.")
    @PutMapping("/admin/order/delivery/{orderId}")
    public CommonResult updateDelivery(@PathVariable("orderId")Long orderId, @ApiParam DeliveryStatus deliveryStatus){
       orderService.updateOrder(orderId,deliveryStatus);
       return responseService.getSuccessResult();
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access-token",required = true,dataType = "String",paramType = "header")
    })
    @ApiOperation(value = "관리자 모든 주문내역" ,notes = "관리자가 상태에 따른 주문내역을 조회한다.")
    @GetMapping("/admin/orders")
    public SingleResult<Page<OrderDto>> findOrdersByDeliveryStatus(@ModelAttribute OrderSearch orderSearch, Pageable pageable){
       return responseService.getSingResult(orderService.findAllOrder(orderSearch,pageable));

    }



}
