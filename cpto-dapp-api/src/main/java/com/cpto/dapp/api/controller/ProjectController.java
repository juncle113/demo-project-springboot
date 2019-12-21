package com.cpto.dapp.api.controller;

import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.pojo.vo.ProjectDetailVO;
import com.cpto.dapp.pojo.vo.ProjectEvaluationVO;
import com.cpto.dapp.pojo.vo.ProjectReportVO;
import com.cpto.dapp.pojo.vo.ProjectSummaryVO;
import com.cpto.dapp.service.ProjectEvaluationService;
import com.cpto.dapp.service.ProjectReportService;
import com.cpto.dapp.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 项目Controller
 *
 * @author sunli
 * @date 2019/01/13
 */
@Api(tags = "项目")
@RequestMapping("/projects")
@RestController
@Validated
public class ProjectController extends BaseController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectEvaluationService projectEvaluationService;

    @Autowired
    private ProjectReportService projectReportService;

    @ApiOperation(value = "取得项目概要信息【非token认证】", notes = "所有正在募集的项目概要信息。", tags = "非token认证")
    @GetMapping("/summary")
    public ResponseEntity<List<ProjectSummaryVO>> findProjectSummaryList() {
        return SimpleResponseEntity.get(projectService.findProjectSummaryList());
    }

    @ApiOperation(value = "取得项目评估信息【非token认证】", notes = "所有进行中的项目评估信息。", tags = "非token认证")
    @GetMapping("/evaluation")
    public ResponseEntity<List<ProjectEvaluationVO>> findProjectEvaluationList() {
        return SimpleResponseEntity.get(projectEvaluationService.findProjectEvaluationList());
    }

    @ApiOperation(value = "取得项目报告信息【非token认证】", notes = "所有投资完成的项目报告信息。(最多显示一年之内的数据)", tags = "非token认证")
    @GetMapping("/reports")
    public ResponseEntity<List<ProjectReportVO>> findProjectReportList() {
        return SimpleResponseEntity.get(projectReportService.findProjectReportList());
    }

    @ApiOperation(value = "取得项目详细信息【非token认证】", notes = "根据项目id取得项目详细信息。", tags = "非token认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", dataType = "long", paramType = "path", defaultValue = "11", required = true)
    })
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailVO> findProject(@PathVariable @NotNull(message = "项目id不能为空") Long projectId) {
        return SimpleResponseEntity.get(projectService.findProject(projectId));
    }

    @ApiOperation(value = "根据项目编号查询项目信息【非token认证】", notes = "根据项目编号查询项目信息。", tags = "非token认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "no", value = "项目编号", paramType = "path", defaultValue = "P20190226", required = true)
    })
    @GetMapping("/nos/{no}")
    public ResponseEntity<ProjectDetailVO> findProjectByNo(@PathVariable @NotNull(message = "项目编号不能为空") String no) {
        return SimpleResponseEntity.get(projectService.findProjectByNo(no));
    }

    @ApiOperation(value = "取得项目评估信息", notes = "修改项目评估信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", dataType = "long", paramType = "path", defaultValue = "11", required = true)
    })
    @GetMapping("/{projectId}/evaluation")
    public ResponseEntity<ProjectEvaluationVO> findProjectEvaluation(@PathVariable @NotNull(message = "项目id不能为空") Long projectId) {
        return SimpleResponseEntity.get(projectEvaluationService.findProjectEvaluation(projectId));
    }
}