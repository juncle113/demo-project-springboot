package com.cpto.dapp.manager.api.controller;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.auth.annotation.CurrentAdmin;
import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.ProjectReportDTO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.pojo.vo.ProjectReportDetailVO;
import com.cpto.dapp.pojo.vo.ProjectReportVO;
import com.cpto.dapp.service.ProjectReportService;
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
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;
import java.util.List;

/**
 * 项目报告Controller
 *
 * @author sunli
 * @date 2019/03/15
 */
@Api(tags = "项目报告")
@RequestMapping("/projects")
@RestController
@Validated
public class ProjectReportController extends BaseController {

    @Autowired
    private ProjectReportService projectReportService;

    @ApiOperation(value = "取得全部项目报告信息列表", notes = "所有投资完成的项目报告信息。(最多显示一年之内的数据)")
    @GetMapping("/reports")
    @ManagerAuth
    public ResponseEntity<List<ProjectReportVO>> findProjectReportList() {
        return SimpleResponseEntity.get(projectReportService.findProjectReportList());
    }

    @ApiOperation(value = "取得某项目报告信息列表", notes = "根据项目id，取得报表信息。")
    @ApiImplicitParams({@ApiImplicitParam(name = "searchTime", value = "查询时间", dataType = "java.sql.Timestamp", paramType = "query", example = "2019-01-01 10:20:30"),
            @ApiImplicitParam(name = "page", value = "当前页数", dataType = "int", paramType = "query", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int", paramType = "query", defaultValue = "10", required = true),
            @ApiImplicitParam(name = "projectId", value = "项目id", dataType = "long", paramType = "path", defaultValue = "22", required = true)
    })
    @GetMapping("/{projectId}/reports")
    @ManagerAuth
    public ResponseEntity<PageVO<ProjectReportDetailVO>> findProjectReportList(@RequestParam(required = false) Timestamp searchTime,
                                                                               @RequestParam @NotNull(message = "当前页数不能为空") @PositiveOrZero Integer page,
                                                                               @RequestParam @NotNull(message = "每页条数不能为空") @Positive Integer pageSize,
                                                                               @PathVariable @NotNull(message = "项目id不能为空") Long projectId) {
        return SimpleResponseEntity.get(projectReportService.findProjectReportListByProject(searchTime, page, pageSize, projectId));
    }

    @ApiOperation(value = "取得项目报告详情", notes = "根据项目报告id，取得项目报告详情。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reportId", value = "公告id", paramType = "path", defaultValue = "1", required = true)
    })
    @GetMapping("/{projectId}/reports/{reportId}")
    @ManagerAuth
    public ResponseEntity<ProjectReportDetailVO> findProjectReport(@PathVariable @NotNull(message = "公告id不能为空") Long reportId) {
        return SimpleResponseEntity.get(projectReportService.findProjectReport(reportId));
    }

    @ApiOperation(value = "新增项目报告", notes = "录入项目运营信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", dataType = "long", paramType = "path", defaultValue = "11", required = true),
            @ApiImplicitParam(name = "projectReportDTO", value = "项目报告信息", dataType = "ProjectReportDTO", paramType = "body", required = true)
    })
    @PostMapping("/{projectId}/reports")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity addProjectReport(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                           @PathVariable @NotNull(message = "项目id不能为空") Long projectId,
                                           @RequestBody @Validated ProjectReportDTO projectReportDTO) {
        projectReportService.addProjectReport(admin, projectId, projectReportDTO);
        return SimpleResponseEntity.post();
    }

    @ApiOperation(value = "修改公告", notes = "修改公告详情。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", dataType = "long", paramType = "path", defaultValue = "11", required = true),
            @ApiImplicitParam(name = "reportId", value = "报告id", paramType = "path", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "projectReportDTO", value = "项目报告信息", paramType = "body", dataType = "ProjectReportDTO", required = true)
    })
    @PutMapping("/{projectId}/reports/{reportId}")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity modifyProjectReport(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                              @PathVariable @NotNull(message = "项目id不能为空") Long projectId,
                                              @PathVariable @NotNull(message = "报告id不能为空") Long reportId,
                                              @RequestBody @Validated ProjectReportDTO projectReportDTO) {
        projectReportService.modifyProjectReport(admin, projectId, reportId, projectReportDTO);
        return SimpleResponseEntity.put();
    }

    @ApiOperation(value = "删除项目报告", notes = "删除项目报告。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", dataType = "long", paramType = "path", defaultValue = "11", required = true),
            @ApiImplicitParam(name = "reportId", value = "报告id", dataType = "long", paramType = "path", defaultValue = "1", required = true)
    })
    @DeleteMapping("/{projectId}/reports/{reportId}")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity removeProjectReport(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                              @PathVariable @NotNull(message = "项目id不能为空") Long projectId,
                                              @PathVariable @NotNull(message = "报告id不能为空") Long reportId) {
        projectReportService.removeProjectReport(admin, projectId, reportId);
        return SimpleResponseEntity.delete();
    }
}