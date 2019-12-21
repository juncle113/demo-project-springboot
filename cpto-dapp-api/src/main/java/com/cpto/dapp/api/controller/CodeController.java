package com.cpto.dapp.api.controller;

import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.pojo.dto.SendCodeDTO;
import com.cpto.dapp.service.CodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码Controller
 *
 * @author sunli
 * @date 2019/01/03
 */
@Api(tags = "验证码")
@RequestMapping("/code")
@RestController
@Validated
public class CodeController extends BaseController {

    @Autowired
    private CodeService codeService;

    @ApiOperation(value = "发送验证码【非token认证】", notes = "通过手机短信或电子邮件的方式发送验证码。", tags = "非token认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sendCodeDTO", value = "发送验证码信息", paramType = "body", dataType = "SendCodeDTO", required = true)
    })
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity sendCode(@RequestBody @Validated SendCodeDTO sendCodeDTO) {
        codeService.sendCode(sendCodeDTO);
        return SimpleResponseEntity.post();
    }
}