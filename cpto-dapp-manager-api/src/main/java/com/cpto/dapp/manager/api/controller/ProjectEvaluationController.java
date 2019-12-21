package com.cpto.dapp.manager.api.controller;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.auth.annotation.CurrentAdmin;
import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.ProjectEvaluationDTO;
import com.cpto.dapp.pojo.vo.ProjectEvaluationVO;
import com.cpto.dapp.service.ProjectEvaluationService;
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

/**
 * 项目评估Controller
 *
 * @author sunli
 * @date 2019/01/23
 */
@Api(tags = "项目评估")
@RequestMapping("/projects")
@RestController
@Validated
public class ProjectEvaluationController extends BaseController {

    @Autowired
    private ProjectEvaluationService projectEvaluationService;

    @ApiOperation(value = "取得项目评估信息", notes = "修改项目评估信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", dataType = "long", paramType = "path", defaultValue = "11", required = true)
    })
    @GetMapping("/{projectId}/evaluation")
    @ManagerAuth
    public ResponseEntity<ProjectEvaluationVO> findProjectEvaluation(@PathVariable @NotNull(message = "项目id不能为空") Long projectId) {
        return SimpleResponseEntity.get(projectEvaluationService.findProjectEvaluation(projectId));
    }

    @ApiOperation(value = "修改项目评估信息", notes = "修改项目评估信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", dataType = "long", paramType = "path", defaultValue = "11", required = true),
            @ApiImplicitParam(name = "projectEvaluationDTO", value = "项目评估信息", dataType = "ProjectEvaluationDTO", paramType = "body", required = true)
    })
    @PutMapping("/{projectId}/evaluation")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity modifyProjectEvaluation(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                                  @PathVariable @NotNull(message = "项目id不能为空") Long projectId,
                                                  @RequestBody @Validated ProjectEvaluationDTO projectEvaluationDTO) {
        projectEvaluationService.modifyProjectEvaluation(admin, projectId, projectEvaluationDTO);
        return SimpleResponseEntity.put();
    }
}