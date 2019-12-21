package com.cpto.dapp.manager.api.controller;

import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.pojo.vo.OrderVO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;

/**
 * 订单Controller
 *
 * @author sunli
 * @date 2019/01/29
 */
@Api(tags = "订单")
@RequestMapping("/orders")
@RestController
@Validated
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "查询订单", notes = "查询满足条件的订单。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchTime", value = "查询时间", dataType = "java.sql.Timestamp", paramType = "query", example = "2019-01-01 10:20:30"),
            @ApiImplicitParam(name = "page", value = "当前页数", dataType = "int", paramType = "query", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int", paramType = "query", defaultValue = "10", required = true),
            @ApiImplicitParam(name = "id", value = "订单id", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "用户名", paramType = "query"),
            @ApiImplicitParam(name = "projectNo", value = "项目编号", paramType = "query"),
            @ApiImplicitParam(name = "projectName", value = "项目名称", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态（1：已支付，2：已取消，3：已完成，4：已失效）", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "备注", paramType = "query"),
            @ApiImplicitParam(name = "fromCreatedTime", value = "创建开始时间", paramType = "query"),
            @ApiImplicitParam(name = "toCreatedTime", value = "创建结束时间", paramType = "query")
    })
    @GetMapping
    @ManagerAuth
    public ResponseEntity<PageVO<OrderVO>> searchOrder(@RequestParam(required = false) Timestamp searchTime,
                                                       @RequestParam @NotNull(message = "当前页数不能为空") @PositiveOrZero Integer page,
                                                       @RequestParam @NotNull(message = "每页条数不能为空") @Positive Integer pageSize,
                                                       @RequestParam(required = false) Long id,
                                                       @RequestParam(required = false) Long userId,
                                                       @RequestParam(required = false) String userName,
                                                       @RequestParam(required = false) String projectNo,
                                                       @RequestParam(required = false) String projectName,
                                                       @RequestParam(required = false) Integer status,
                                                       @RequestParam(required = false) String remark,
                                                       @RequestParam(required = false) String fromCreatedTime,
                                                       @RequestParam(required = false) String toCreatedTime) {
        return SimpleResponseEntity.get(orderService.searchOrder(searchTime, page, pageSize, id, userId, userName, projectNo, projectName, status, remark, fromCreatedTime, toCreatedTime));
    }
}