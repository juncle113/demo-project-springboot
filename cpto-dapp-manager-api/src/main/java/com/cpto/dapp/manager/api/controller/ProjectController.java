package com.cpto.dapp.manager.api.controller;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.auth.annotation.CurrentAdmin;
import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.ProjectDTO;
import com.cpto.dapp.pojo.vo.ProjectDetailVO;
import com.cpto.dapp.pojo.vo.ProjectVO;
import com.cpto.dapp.service.ProjectService;
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
 * 项目Controller
 *
 * @author sunli
 * @date 2019/01/23
 */
@Api(tags = "项目")
@RequestMapping("/projects")
@RestController
@Validated
public class ProjectController extends BaseController {

    @Autowired
    private ProjectService projectService;

    @ApiOperation(value = "新增项目", notes = "使用管理员账户添加项目。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectDTO", value = "新增项目信息", dataType = "ProjectDTO", paramType = "body", required = true)
    })
    @PostMapping
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity addProject(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                     @RequestBody @Validated ProjectDTO projectDTO) {
        projectService.addProject(admin, projectDTO);
        return SimpleResponseEntity.post();
    }

    @ApiOperation(value = "修改项目", notes = "修改项目详情。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", dataType = "long", paramType = "path", defaultValue = "11", required = true),
            @ApiImplicitParam(name = "projectDTO", value = "项目信息", dataType = "ProjectDTO", paramType = "body", required = true)
    })
    @PutMapping("/{projectId}")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity modifyProject(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                        @PathVariable @NotNull(message = "项目id不能为空") Long projectId,
                                        @RequestBody @Validated ProjectDTO projectDTO) {
        projectService.modifyProject(admin, projectId, projectDTO);
        return SimpleResponseEntity.put();
    }

    @ApiOperation(value = "删除项目", notes = "逻辑删除项目。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", paramType = "path", defaultValue = "11", required = true)
    })
    @DeleteMapping("/{projectId}")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity removeProject(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                        @PathVariable Long projectId) {
        projectService.removeProject(admin, projectId);
        return SimpleResponseEntity.delete();
    }

    @ApiOperation(value = "取得项目列表", notes = "取得全部项目信息。")
    @GetMapping
    @ManagerAuth
    public ResponseEntity<List<ProjectVO>> findProjectList() {
        return SimpleResponseEntity.get(projectService.findProjectList());
    }

    @ApiOperation(value = "取得项目详细信息", notes = "根据项目id，取得项目详细信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", dataType = "long", paramType = "path", defaultValue = "11", required = true)
    })
    @GetMapping("/{projectId}")
    @ManagerAuth
    public ResponseEntity<ProjectDetailVO> findProject(@PathVariable @NotNull(message = "项目id不能为空") Long projectId) {
        return SimpleResponseEntity.get(projectService.findProject(projectId));
    }

//    @ApiOperation(value = "取得项目状态申请信息列表", notes = "取得修改项目状态的申请。")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "projectId", value = "项目id", dataType = "long", paramType = "path", defaultValue = "11", required = true)
//    })
//    @GetMapping("/{projectId}/status/applications")
//    @ManagerAuth
//    public ResponseEntity<List<ProjectStatusApplicationVO>> findProjectStatusApplication(@PathVariable @NotNull(message = "项目id不能为空") Long projectId) {
//        return SimpleResponseEntity.get(projectService.findProjectStatusApplication());
//    }

//    @ApiOperation(value = "审批项目状态申请", notes = "修改项目状态。")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "projectId", value = "项目id", dataType = "long", paramType = "path", defaultValue = "11", required = true),
//            @ApiImplicitParam(name = "applicationId", value = "申请id", dataType = "long", paramType = "path", defaultValue = "1", required = true),
//            @ApiImplicitParam(name = "projectStatusApplicationDTO", value = "项目状态申请信息", dataType = "ProjectStatusApplicationDTO", paramType = "body", required = true)
//    })
//    @PutMapping("/{projectId}/status/applications/{applicationId}")
//    @ManagerAuth(AuthManager.WRITE)
//    @Transactional(rollbackFor = Exception.class)
//    public ResponseEntity confirmProjectStatusApplication(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
//                                                          @PathVariable @NotNull(message = "项目id不能为空") Long projectId,
//                                                          @PathVariable @NotNull(message = "申请id不能为空") Long applicationId,
//                                                          @RequestBody @Validated ProjectStatusApplicationDTO projectStatusApplicationDTO) {
//        projectService.confirmProjectStatusApplication(admin, projectId, applicationId, projectStatusApplicationDTO);
//        return SimpleResponseEntity.put();
//    }
}