package com.cpto.dapp.api.controller;

import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.pojo.vo.SourceVO;
import com.cpto.dapp.service.SourceService;
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

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 资源Controller
 *
 * @author sunli
 * @date 2019/02/22
 */
@Api(tags = "资源")
@RequestMapping("/sources")
@RestController
@Validated
public class SourceController extends BaseController {

    @Autowired
    private SourceService sourceService;

    @ApiOperation(value = "取得资源", notes = "取得图片、影音等文件资源。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "relationType", value = "关联类型（1：提币申请，2：订单，3：收益记录，4：项目，5：公告）", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "relationId", value = "关联id", dataType = "long", paramType = "query", required = true)
    })
    @GetMapping
    @ManagerAuth
    public ResponseEntity<List<SourceVO>> findSourceList(@RequestParam @NotNull(message = "关联类型不能为空") Integer relationType,
                                                         @RequestParam @NotNull(message = "关联id不能为空") Long relationId) {
        return SimpleResponseEntity.get(sourceService.findSourceList(relationType, relationId));
    }
}