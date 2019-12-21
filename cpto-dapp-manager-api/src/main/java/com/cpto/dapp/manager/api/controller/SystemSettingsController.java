package com.cpto.dapp.manager.api.controller;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.auth.annotation.CurrentAdmin;
import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.SystemSettingsDTO;
import com.cpto.dapp.pojo.vo.SystemSettingsVO;
import com.cpto.dapp.service.SystemSettingsService;
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
 * 系统设置Controller
 *
 * @author sunli
 * @date 2019/02/21
 */
@Api(tags = "系统设置")
@RequestMapping("/system/settings")
@RestController
@Validated
public class SystemSettingsController extends BaseController {

    @Autowired
    private SystemSettingsService systemSettingsService;

    @ApiOperation(value = "取得系统参数列表", notes = "取得系统参数列表")
    @GetMapping
    @ManagerAuth
    public ResponseEntity<List<SystemSettingsVO>> findSystemSettingsList() {
        return SimpleResponseEntity.get(systemSettingsService.findSystemSettingsList());
    }

    @ApiOperation(value = "取得系统参数", notes = "取得系统参数。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "settingsId", value = "系统参数id", dataType = "long", paramType = "path", defaultValue = "1", required = true)
    })
    @GetMapping("/{settingsId}")
    @ManagerAuth
    public ResponseEntity<SystemSettingsVO> findSystemSettings(@PathVariable @NotNull(message = "系统参数id不能为空") Long settingsId) {
        return SimpleResponseEntity.get(systemSettingsService.findSystemSettingsVO(settingsId));
    }

    @ApiOperation(value = "修改系统参数", notes = "修改系统参数。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "settingsId", value = "系统参数id", dataType = "long", paramType = "path", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "systemSettingsDTO", value = "系统参数信息", dataType = "SystemSettingsDTO", paramType = "body", required = true)
    })
    @PutMapping("/{settingsId}")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity modifySystemSettings(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                               @PathVariable @NotNull(message = "系统参数id不能为空") Long settingsId,
                                               @RequestBody @Validated SystemSettingsDTO systemSettingsDTO) {
        systemSettingsService.modifySystemSettings(admin, settingsId, systemSettingsDTO);
        return SimpleResponseEntity.put();
    }
}