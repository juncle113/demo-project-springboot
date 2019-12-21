package com.cpto.dapp.api.controller;

import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.pojo.vo.SystemSettingsVO;
import com.cpto.dapp.service.SystemSettingsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统设置Controller
 *
 * @author sunli
 * @date 2019/04/08
 */
@Api(tags = "系统设置")
@RequestMapping("/system/settings")
@RestController
@Validated
public class SystemSettingsController extends BaseController {

    @Autowired
    private SystemSettingsService systemSettingsService;

    @ApiOperation(value = "取得系统参数", notes = "取得系统参数。")
    @GetMapping
    public ResponseEntity<List<SystemSettingsVO>> findSystemSettingsByApp(@RequestParam @NotNull(message = "系统参数key不能为空")
                                                                          @ApiParam(value = "系统参数key\n" +
                                                                                  "url_ad_pic（广告图网址）\n" +
                                                                                  "url_ad（广告页跳转网址）\n" +
                                                                                  "url_help（帮助页面网址）\n" +
                                                                                  "url_user_agreement（用户协议网址）\n" +
                                                                                  "url_app_download（App下载页面）\n" +
                                                                                  "number_withdraw_max（提币上限）\n" +
                                                                                  "number_withdraw_min（提币下限）\n" +
                                                                                  "number_btc_withdraw_fee_rate（BTC提币手续费率）\n" +
                                                                                  "number_eth_withdraw_fee_rate（ETH提币手续费率）",
                                                                                  required = true) List<String> paramKeyList) {
        return SimpleResponseEntity.get(systemSettingsService.findSystemSettingsByApp(paramKeyList));
    }
}