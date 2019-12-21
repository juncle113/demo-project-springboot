package com.cpto.dapp.manager.api.controller;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.auth.annotation.CurrentAdmin;
import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.ExchangeRateDTO;
import com.cpto.dapp.pojo.vo.ExchangeRateVO;
import com.cpto.dapp.service.ExchangeRateService;
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
 * 币种Controller
 *
 * @author sunli
 * @date 2019/03/07
 */
@Api(tags = "币种")
@RequestMapping("/coin")
@RestController
@Validated
public class CoinController extends BaseController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @ApiOperation(value = "查询币种汇率信息列表", notes = "查询币种汇率信息列表。")
    @GetMapping("/rates")
    @ManagerAuth
    public ResponseEntity<List<ExchangeRateVO>> findExchangeRateList() {
        return SimpleResponseEntity.get(exchangeRateService.findExchangeRateList());
    }

    @ApiOperation(value = "查询币种汇率", notes = "根据币种汇率id查询币种汇率。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "exchangeRateId", value = "币种汇率id", paramType = "path", defaultValue = "1", required = true)
    })
    @GetMapping("/rates/{exchangeRateId}")
    @ManagerAuth
    public ResponseEntity<ExchangeRateVO> findExchangeRate(@PathVariable @NotNull(message = "币种汇率id不能为空") Long exchangeRateId) {
        return SimpleResponseEntity.get(exchangeRateService.findExchangeRate(exchangeRateId));
    }

    @ApiOperation(value = "修改币种汇率", notes = "修改币种汇率。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "exchangeRateId", value = "币种汇率id", paramType = "path", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "exchangeRateDTO", value = "币种汇率信息", paramType = "body", dataType = "ExchangeRateDTO", required = true)
    })
    @PutMapping("/rates/{exchangeRateId}")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity modifyNotice(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                       @PathVariable @NotNull(message = "币种汇率id不能为空") Long exchangeRateId,
                                       @RequestBody @Validated ExchangeRateDTO exchangeRateDTO) {
        exchangeRateService.modifyExchangeRate(admin, exchangeRateId, exchangeRateDTO);
        return SimpleResponseEntity.put();
    }
}