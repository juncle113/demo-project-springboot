package com.cpto.dapp.api.controller;

import com.cpto.dapp.auth.annotation.Auth;
import com.cpto.dapp.auth.annotation.CurrentUser;
import com.cpto.dapp.auth.annotation.PayAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.User;
import com.cpto.dapp.pojo.dto.WalletDTO;
import com.cpto.dapp.pojo.dto.WithdrawalApplicationDTO;
import com.cpto.dapp.pojo.vo.WalletVO;
import com.cpto.dapp.pojo.vo.WithdrawalApplicationVO;
import com.cpto.dapp.service.WalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * 钱包Controller
 *
 * @author sunli
 * @date 2019/02/14
 */
@Api(tags = "钱包")
@RequestMapping("/users/self/wallets")
@RestController
@Validated
public class WalletController extends BaseController {

    @Autowired
    private WalletService walletService;

    @ApiOperation(value = "查询钱包信息列表", notes = "根据用户id查询钱包信息列表。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "钱包地址类型（1：充值，2：提币）", dataType = "int", paramType = "query", defaultValue = "1", required = true)
    })
    @GetMapping
    @Auth
    public ResponseEntity<List<WalletVO>> findWalletListByType(@ApiIgnore @CurrentUser User user,
                                                               @RequestParam @NotNull(message = "钱包地址类型不能为空") @Range(min = 1, max = 2, message = "钱包地址类型错误") Integer type) {
        return SimpleResponseEntity.get(walletService.findWalletListByType(user, type));
    }

    @ApiOperation(value = "添加钱包信息", notes = "选择币种，然后添加相应钱包信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "walletDTO", value = "钱包信息", paramType = "body", dataType = "WalletDTO", required = true)
    })
    @PostMapping
    @Auth
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity addWallet(@ApiIgnore @CurrentUser User user,
                                    @RequestBody @Validated WalletDTO walletDTO) {
        walletService.addWallet(user, walletDTO);
        return SimpleResponseEntity.post();
    }

    @ApiOperation(value = "修改钱包信息", notes = "修改已添加的钱包信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "walletDTO", value = "钱包信息", paramType = "body", dataType = "WalletDTO", required = true)
    })
    @PutMapping("/{walletId}")
    @Auth
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity modifyWallet(@ApiIgnore @CurrentUser User user,
                                       @PathVariable @NotNull(message = "钱包信息id不能为空") Long walletId,
                                       @RequestBody @Validated WalletDTO walletDTO) {
        walletService.modifyWallet(user, walletId, walletDTO);
        return SimpleResponseEntity.put();
    }

    @ApiOperation(value = "删除钱包信息", notes = "根据钱包信息id删除钱包信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "walletId", value = "钱包信息id", dataType = "long", paramType = "path", defaultValue = "11", required = true)
    })
    @DeleteMapping("/{walletId}")
    @Auth
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity removeWallet(@ApiIgnore @CurrentUser User user,
                                       @PathVariable @NotNull(message = "钱包信息id不能为空") Long walletId) {
        walletService.removeWallet(user, walletId);
        return SimpleResponseEntity.delete();
    }

    @ApiOperation(value = "确认提币申请", notes = "输入CPTO数额，然后兑换成选择提取的其他币种。显示提币后兑换数额和手续费。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "toCoinKind", value = "兑换币种", paramType = "query", defaultValue = "BTC", required = true),
            @ApiImplicitParam(name = "toChainAddress", value = "转入地址", paramType = "query", defaultValue = "34p7rgjoYtHYA8ViMbFViq5yuyp2ta6Lw4", required = true),
            @ApiImplicitParam(name = "cptoAmount", value = "提币数额", dataType = "double", paramType = "10000", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "chainMemo", value = "区块链备注", paramType = "query", defaultValue = "备注")
    })
    @GetMapping("/withdrawal-applications/confirmation")
    @Auth
    public ResponseEntity<WithdrawalApplicationVO> confirmWithdrawalApplication(@ApiIgnore @CurrentUser User user,
                                                                                @RequestParam @NotBlank(message = "兑换币种不能为空") @Pattern(regexp = "\\b(BTC|ETH)\\b", message = "兑换币种错误") String toCoinKind,
                                                                                @RequestParam @NotNull(message = "转入地址不能为空") @Size(max = 100, message = "转入地址必须为100位以内字符") String toChainAddress,
                                                                                @RequestParam @NotNull(message = "提币数额不能为空") @Positive(message = "提币数额必须大于0") BigDecimal cptoAmount,
                                                                                @RequestParam @Size(max = 100, message = "区块链备注必须为100位以内字符") String chainMemo) {
        return SimpleResponseEntity.get(walletService.confirmWithdrawalApplication(user, toCoinKind, toChainAddress, cptoAmount, chainMemo));
    }

    @ApiOperation(value = "提币申请", notes = "调用【确认提币申请】后，输入支付密码，进行提币申请。（待人工审核后，手动转账）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "withdrawalApplicationDTO", value = "提币信息", paramType = "body", dataType = "WithdrawalApplicationDTO", required = true)
    })
    @PostMapping("/withdrawal-applications")
    @Auth
    @PayAuth
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity withdrawalApplication(@ApiIgnore @CurrentUser User user,
                                                @RequestBody @Validated WithdrawalApplicationDTO withdrawalApplicationDTO) {
        walletService.withdrawalApplication(user, withdrawalApplicationDTO);
        return SimpleResponseEntity.post();
    }
}