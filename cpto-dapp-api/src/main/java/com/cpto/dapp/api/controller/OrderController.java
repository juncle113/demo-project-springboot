package com.cpto.dapp.api.controller;

import com.cpto.dapp.auth.annotation.Auth;
import com.cpto.dapp.auth.annotation.CurrentUser;
import com.cpto.dapp.auth.annotation.PayAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.User;
import com.cpto.dapp.pojo.dto.OrderDTO;
import com.cpto.dapp.pojo.vo.OrderVO;
import com.cpto.dapp.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 订单Controller
 *
 * @author sunli
 * @date 2019/01/16
 */
@Api(tags = "订单")
@RequestMapping("/users/self/orders")
@RestController
@Validated
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "查询订单列表", notes = "根据用户id查询全部订单列表。")
    @GetMapping
    @Auth
    public ResponseEntity<List<OrderVO>> findOrderList(@ApiIgnore @CurrentUser User user) {
        return SimpleResponseEntity.get(orderService.findOrderListByUser(user));
    }

    @ApiOperation(value = "创建订单", notes = "选择募集的项目进行投资，然后生成相应订单。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderDTO", value = "创建订单信息", paramType = "body", dataType = "OrderDTO", required = true)
    })
    @PostMapping
    @Auth
    @PayAuth
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity addOrder(@ApiIgnore @CurrentUser User user,
                                   @RequestBody @Validated OrderDTO orderDTO) {
        orderService.addOrder(user, orderDTO);
        return SimpleResponseEntity.post();
    }

    @ApiOperation(value = "退出投资", notes = "根据订单id撤销当前用户的订单。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "long", paramType = "path", defaultValue = "11", required = true)
    })
    @DeleteMapping("/{orderId}")
    @Auth
    @PayAuth
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity cancelOrder(@ApiIgnore @CurrentUser User user,
                                      @PathVariable @NotNull(message = "订单id不能为空") Long orderId) {
        orderService.cancelOrder(user, orderId);
        return SimpleResponseEntity.delete();
    }
}