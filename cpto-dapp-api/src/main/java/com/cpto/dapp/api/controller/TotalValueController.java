package com.cpto.dapp.api.controller;

import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.pojo.vo.TotalValueVO;
import com.cpto.dapp.service.TotalValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 价值总量Controller
 *
 * @author sunli
 * @date 2019/01/09
 */
@Api(tags = "价值总量")
@RequestMapping("/value")
@RestController
@Validated
public class TotalValueController extends BaseController {

    @Autowired
    private TotalValueService totalValueService;

    @ApiOperation(value = "取得价值总量【非token认证】", notes = "取得价值总量中的价值概要、总量分配、流通来源等信息。", tags = "非token认证")
    @GetMapping
    public ResponseEntity<TotalValueVO> findTotalValue() {
        return SimpleResponseEntity.get(totalValueService.findTotalValue());
    }
}