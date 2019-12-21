package com.cpto.dapp.api.controller;

import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.pojo.vo.ExchangeRateVO;
import com.cpto.dapp.service.ExchangeRateService;
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

import javax.validation.constraints.NotBlank;

/**
 * 币种Controller
 *
 * @author sunli
 * @date 2019/03/05
 */
@Api(tags = "币种")
@RequestMapping("/coin")
@RestController
@Validated
public class CoinController extends BaseController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @ApiOperation(value = "查询币种汇率【非token认证】", notes = "根据币种查询币种汇率。", tags = "非token认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fromCoinKind", value = "源币种", paramType = "query", defaultValue = "cpto", required = true),
            @ApiImplicitParam(name = "toCoinKind", value = "目标币种", paramType = "query", defaultValue = "USDT", required = true)
    })
    @GetMapping("/rates")
    public ResponseEntity<ExchangeRateVO> findExchangeRateByCoin(@RequestParam @NotBlank(message = "源币种不能为空") String fromCoinKind,
                                                                 @RequestParam @NotBlank(message = "目标币种不能为空") String toCoinKind) {
        return SimpleResponseEntity.get(exchangeRateService.findExchangeVORateByCoin(fromCoinKind, toCoinKind));
    }
}