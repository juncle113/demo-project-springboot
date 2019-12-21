package com.cpto.dapp.manager.api.controller;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.auth.annotation.CurrentAdmin;
import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.CheckApplicationDTO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.pojo.vo.WithdrawalApplicationVO;
import com.cpto.dapp.service.WalletService;
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
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;

/**
 * 钱包Controller
 *
 * @author sunli
 * @date 2019/04/14
 */
@Api(tags = "钱包")
@RequestMapping("/users/wallets")
@RestController
@Validated
public class WalletController extends BaseController {


    @Autowired
    private WalletService walletService;

    @ApiOperation(value = "取得提币申请详情", notes = "根据提币申请id，取得提币申请详情。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "withdrawalApplicationId", value = "提币申请id", paramType = "path", defaultValue = "1571231245", required = true)
    })
    @GetMapping("/withdrawal-applications/{withdrawalApplicationId}")
    @ManagerAuth
    public ResponseEntity<WithdrawalApplicationVO> findWithdrawalApplication(@PathVariable @NotNull(message = "提币申请id不能为空") Long withdrawalApplicationId) {
        return SimpleResponseEntity.get(walletService.findWithdrawalApplication(withdrawalApplicationId));
    }

    @ApiOperation(value = "查询钱包提币信息列表", notes = "查询所有用户的提币申请。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchTime", value = "查询时间", dataType = "java.sql.Timestamp", paramType = "query", defaultValue = "2019-01-01 10:20:30"),
            @ApiImplicitParam(name = "page", value = "当前页数", dataType = "int", paramType = "query", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int", paramType = "query", defaultValue = "10", required = true),
            @ApiImplicitParam(name = "id", value = "反馈id", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "用户名", paramType = "query"),
            @ApiImplicitParam(name = "coinKind", value = "提币币种", paramType = "query"),
            @ApiImplicitParam(name = "chainAddress", value = "提币地址", paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "备注", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态（1：未处理，2：成功，3：失败）", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "fromCreatedTime", value = "申请开始时间", paramType = "query"),
            @ApiImplicitParam(name = "toCreatedTime", value = "申请结束时间", paramType = "query")
    })
    @GetMapping("/withdrawal-applications")
    @ManagerAuth
    public ResponseEntity<PageVO<WithdrawalApplicationVO>> searchWithdrawalApplication(@RequestParam(required = false) Timestamp searchTime,
                                                                                       @RequestParam @NotNull(message = "当前页数不能为空") @PositiveOrZero Integer page,
                                                                                       @RequestParam @NotNull(message = "每页条数不能为空") @Positive Integer pageSize,
                                                                                       @RequestParam(required = false) Long id,
                                                                                       @RequestParam(required = false) Long userId,
                                                                                       @RequestParam(required = false) String userName,
                                                                                       @RequestParam(required = false) String coinKind,
                                                                                       @RequestParam(required = false) String chainAddress,
                                                                                       @RequestParam(required = false) String remark,
                                                                                       @RequestParam(required = false) Integer status,
                                                                                       @RequestParam(required = false) String fromCreatedTime,
                                                                                       @RequestParam(required = false) String toCreatedTime) {
        return SimpleResponseEntity.get(walletService.searchWithdrawalApplication(searchTime, page, pageSize, id, userId, userName, coinKind, chainAddress, remark, status, fromCreatedTime, toCreatedTime));
    }

    @ApiOperation(value = "审核提币申请", notes = "审核提币申请成功或者失败。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "withdrawalApplicationId", value = "提币申请id", paramType = "path", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "checkApplicationDTO", value = "审核申请信息", paramType = "body", dataType = "CheckApplicationDTO", required = true)
    })
    @PutMapping("/withdrawal-applications/{withdrawalApplicationId}")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity checkWithdrawalApplication(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                                     @PathVariable @NotNull(message = "提币申请id不能为空") Long withdrawalApplicationId,
                                                     @RequestBody @Validated CheckApplicationDTO checkApplicationDTO) {
        walletService.checkWithdrawalApplication(admin, withdrawalApplicationId, checkApplicationDTO);
        return SimpleResponseEntity.put();
    }
}