package com.cpto.dapp.manager.api.controller;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.auth.annotation.CurrentAdmin;
import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.ProjectIncomeDTO;
import com.cpto.dapp.pojo.vo.ProjectIncomeLogVO;
import com.cpto.dapp.service.ProjectIncomeService;
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
 * 项目收益Controller
 *
 * @author sunli
 * @date 2019/03/08
 */
@Api(tags = "项目收益")
@RequestMapping("/projects")
@RestController
@Validated
public class ProjectIncomeController extends BaseController {

    @Autowired
    private ProjectIncomeService projectIncomeService;

    @ApiOperation(value = "发放项目收益", notes = "输入项目待发放收益，根据投资数额和募集总额占比来分配给用户。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", dataType = "long", paramType = "path", defaultValue = "11", required = true),
            @ApiImplicitParam(name = "projectIncomeDTO", value = "待发放项目收益信息", dataType = "ProjectIncomeDTO", paramType = "body", required = true)
    })
    @PostMapping("/{projectId}/incomes")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity grantProjectIncome(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                             @PathVariable @NotNull(message = "项目id不能为空") Long projectId,
                                             @RequestBody @Validated ProjectIncomeDTO projectIncomeDTO) {
        projectIncomeService.grantProjectIncome(admin, projectId, projectIncomeDTO);
        return SimpleResponseEntity.post();
    }

    @ApiOperation(value = "取得项目收益记录列表", notes = "根据项目id，取得该项目收益记录列表。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", dataType = "long", paramType = "path", defaultValue = "11", required = true)
    })
    @GetMapping("/{projectId}/incomes")
    @ManagerAuth
    public ResponseEntity<List<ProjectIncomeLogVO>> findProjectIncomeLogList(@PathVariable @NotNull(message = "项目id不能为空") Long projectId) {
        return SimpleResponseEntity.get(projectIncomeService.findProjectIncomeLogList(projectId));
    }

    @ApiOperation(value = "撤销项目收益", notes = "撤销已经发放的收益。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", dataType = "long", paramType = "path", defaultValue = "11", required = true),
            @ApiImplicitParam(name = "incomeId", value = "收益id", dataType = "long", paramType = "path", defaultValue = "1", required = true)
    })
    @DeleteMapping("/{projectId}/incomes/{incomeId}")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity cancelProjectIncome(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                              @PathVariable @NotNull(message = "项目id不能为空") Long projectId,
                                              @PathVariable @NotNull(message = "收益id不能为空") Long incomeId) {
        projectIncomeService.cancelProjectIncome(admin, projectId, incomeId);
        return SimpleResponseEntity.delete();
    }
}