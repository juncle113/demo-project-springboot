package com.cpto.dapp.api.controller;

import com.cpto.dapp.auth.annotation.Auth;
import com.cpto.dapp.auth.annotation.CurrentUser;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.User;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;

/**
 * 账户Controller
 *
 * @author sunli
 * @date 2019/01/27
 */
@Api(tags = "账户")
@RequestMapping("/users/self/accounts")
@RestController
@Validated
public class AccountController extends BaseController {

    @Autowired
    private AccountService accountService;

    @ApiOperation(value = "查询账目明细列表", notes = "根据用户id查询账户记录列表。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchTime", value = "查询时间", dataType = "java.sql.Timestamp", paramType = "query", example = "2019-01-01 10:20:30"),
            @ApiImplicitParam(name = "page", value = "当前页数", dataType = "int", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "transferTypeGroup", value = "转账类型分类（1：支付，2：收益，3：充值，4：提币）", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "fromCreatedTime", value = "创建开始时间", paramType = "query", example = "2019-01-01"),
            @ApiImplicitParam(name = "toCreatedTime", value = "创建结束时间", paramType = "query", example = "2019-01-31")
    })
    @GetMapping("/logs")
    @Auth
    public ResponseEntity<PageVO<AccountLogVO>> findAccountLogList(@RequestParam(required = false) Timestamp searchTime,
                                                                   @RequestParam @NotNull(message = "当前页数不能为空") @PositiveOrZero Integer page,
                                                                   @RequestParam @NotNull(message = "每页条数不能为空") @Positive Integer pageSize,
                                                                   @ApiIgnore @CurrentUser User user,
                                                                   @RequestParam(required = false) Integer transferTypeGroup,
                                                                   @RequestParam(required = false) String fromCreatedTime,
                                                                   @RequestParam(required = false) String toCreatedTime) {
        return SimpleResponseEntity.get(accountService.searchAccountLog(searchTime, page, pageSize, user.getId(), transferTypeGroup, fromCreatedTime, toCreatedTime));
    }

    @ApiOperation(value = "取得账户资产信息", notes = "根据用户id查询账户总资产、可用资产、锁定资产和累计收益。")
    @GetMapping("/assets")
    @Auth
    public ResponseEntity<AccountAssetsVO> findAssets(@ApiIgnore @CurrentUser User user) {
        return SimpleResponseEntity.get(accountService.findAssets(user.getId()));
    }
}