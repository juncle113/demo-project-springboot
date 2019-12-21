package com.cpto.dapp.manager.api.controller;

import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.pojo.vo.AccountAssetsVO;
import com.cpto.dapp.pojo.vo.AccountLogVO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;

/**
 * 账户Controller
 *
 * @author sunli
 * @date 2019/04/25
 */
@Api(tags = "账户")
@RequestMapping("/users/{userId}")
@RestController
@Validated
public class AccountController extends BaseController {

    @Autowired
    private AccountService accountService;

    @ApiOperation(value = "查询用户的账目明细列表", notes = "根据用户id查询用户账户记录列表。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "path", defaultValue = "1"),
            @ApiImplicitParam(name = "searchTime", value = "查询时间", dataType = "java.sql.Timestamp", paramType = "query", example = "2019-01-01 10:20:30"),
            @ApiImplicitParam(name = "page", value = "当前页数", dataType = "int", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "transferTypeGroup", value = "转账类型分类（1：支付，2：收益，3：充值，4：提币）", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "fromCreatedTime", value = "创建开始时间", paramType = "query", example = "2019-01-01"),
            @ApiImplicitParam(name = "toCreatedTime", value = "创建结束时间", paramType = "query", example = "2019-01-31")
    })
    @GetMapping("/accounts/logs")
    @ManagerAuth
    public ResponseEntity<PageVO<AccountLogVO>> findAccountLogList(@PathVariable @NotNull(message = "用户id不能为空") Long userId,
                                                                   @RequestParam(required = false) Timestamp searchTime,
                                                                   @RequestParam @NotNull(message = "当前页数不能为空") @PositiveOrZero Integer page,
                                                                   @RequestParam @NotNull(message = "每页条数不能为空") @Positive Integer pageSize,
                                                                   @RequestParam(required = false) Integer transferTypeGroup,
                                                                   @RequestParam(required = false) String fromCreatedTime,
                                                                   @RequestParam(required = false) String toCreatedTime) {
        return SimpleResponseEntity.get(accountService.searchAccountLog(searchTime, page, pageSize, userId, transferTypeGroup, fromCreatedTime, toCreatedTime));
    }

    @ApiOperation(value = "取得账户资产信息", notes = "根据用户id查询账户总资产、可用资产、锁定资产和累计收益。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "path", defaultValue = "1")
    })
    @GetMapping("/assets")
    @ManagerAuth
    public ResponseEntity<AccountAssetsVO> findAssets(@PathVariable @NotNull(message = "用户id不能为空") Long userId) {
        return SimpleResponseEntity.get(accountService.findAssets(userId));
    }
}