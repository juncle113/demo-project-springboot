package com.cpto.dapp.api.controller;

import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.pojo.vo.SystemNoticeVO;
import com.cpto.dapp.pojo.vo.VersionVO;
import com.cpto.dapp.service.SystemInfoService;
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
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统信息Controller
 *
 * @author sunli
 * @date 2019/01/10
 */
@Api(tags = "系统信息")
@RequestMapping("/system")
@RestController
@Validated
public class SystemInfoController extends BaseController {

    @Autowired
    private SystemInfoService systemInfoService;

    @ApiOperation(value = "取得系统公告【非token认证】", notes = "取得系统公告信息。", tags = "非token认证")
    @GetMapping("/notices")
    public ResponseEntity<List<SystemNoticeVO>> findSystemNoticeList() {
        return SimpleResponseEntity.get(systemInfoService.findSystemNoticeList());
    }

    @ApiOperation(value = "检查版本【非token认证】", notes = "根据客户端当前版本判断是否需要更新。", tags = "非token认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceType", value = "设备类型（1：Android，2：iOS）", dataType = "int", paramType = "query", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "currentVersion", value = "当前版本号", paramType = "query", defaultValue = "1.0.9", required = true)
    })
    @GetMapping("/version")
    public ResponseEntity<VersionVO> checkVersion(@RequestParam @NotNull(message = "设备类型不能为空") Integer deviceType,
                                                  @RequestParam @NotBlank(message = "当前版本号不能为空") String currentVersion) {
        return SimpleResponseEntity.get(systemInfoService.checkVersion(deviceType, currentVersion));
    }
}