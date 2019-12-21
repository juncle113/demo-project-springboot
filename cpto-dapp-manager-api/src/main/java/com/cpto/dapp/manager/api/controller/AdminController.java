package com.cpto.dapp.manager.api.controller;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.auth.annotation.CurrentAdmin;
import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.AdminDTO;
import com.cpto.dapp.pojo.dto.AdminLoginDTO;
import com.cpto.dapp.pojo.vo.AdminLoginVO;
import com.cpto.dapp.pojo.vo.AdminVO;
import com.cpto.dapp.service.ManagerAdminService;
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
 * 管理员Controller
 *
 * @author sunli
 * @date 2018/12/07
 */
@Api(tags = "管理员")
@RequestMapping(value = "/admins")
@RestController
@Validated
public class AdminController extends BaseController {

    @Autowired
    private ManagerAdminService managerAdminService;

    @ApiOperation(value = "登录【非token认证】", notes = "通过检查管理员的用户名和密码，完成登录。", tags = "非token认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminLoginDTO", value = "管理员登录信息", paramType = "body", dataType = "AdminLoginDTO", required = true)
    })
    @PostMapping("/token")
    public ResponseEntity<AdminLoginVO> login(@RequestBody @Validated AdminLoginDTO adminLoginDTO) {
        return SimpleResponseEntity.post(managerAdminService.login(adminLoginDTO));
    }

    @ApiOperation(value = "注销", notes = "清除缓存中当前登录账号的token。")
    @DeleteMapping("/self/token")
    @ManagerAuth
    public ResponseEntity logout(@ApiIgnore @CurrentAdmin ManagerAdmin admin) {
        managerAdminService.logout(admin);
        return SimpleResponseEntity.delete();
    }

    @ApiOperation(value = "取得管理员列表", notes = "取得全部管理员信息。")
    @GetMapping
    @ManagerAuth
    public ResponseEntity<List<AdminVO>> findAdminList() {
        return SimpleResponseEntity.get(managerAdminService.findAdminList());
    }

    @ApiOperation(value = "取得管理员", notes = "根据管理员id，取得管理员信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminId", value = "管理员id", paramType = "path", defaultValue = "2", required = true)
    })
    @GetMapping("/{adminId}")
    @ManagerAuth
    public ResponseEntity<AdminVO> findAdmin(@PathVariable @NotNull(message = "管理员id不能为空") Long adminId) {
        return SimpleResponseEntity.get(managerAdminService.findAdmin(adminId));
    }

    @ApiOperation(value = "新建管理员", notes = "使用root账户创建管理员。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminDTO", value = "管理员信息", dataType = "AdminDTO", paramType = "body", required = true)
    })
    @PostMapping
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity addAdmin(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                   @RequestBody @Validated AdminDTO adminDTO) {
        managerAdminService.addAdmin(admin, adminDTO);
        return SimpleResponseEntity.post();
    }

    @ApiOperation(value = "修改管理员", notes = "修改管理员信息，不能操作root账户。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminId", value = "管理员id", paramType = "path", defaultValue = "2", required = true),
            @ApiImplicitParam(name = "adminDTO", value = "管理员信息", dataType = "AdminDTO", paramType = "body", required = true)
    })
    @PutMapping("/{adminId}")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity modifyAdmin(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                      @PathVariable @NotNull(message = "管理员id不能为空") Long adminId,
                                      @RequestBody @Validated AdminDTO adminDTO) {
        managerAdminService.modifyAdmin(admin, adminId, adminDTO);
        return SimpleResponseEntity.put();
    }

    @ApiOperation(value = "删除管理员", notes = "逻辑删除管理员，不能操作root账户。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminId", value = "管理员id", paramType = "path", defaultValue = "2", required = true)
    })
    @DeleteMapping("/{adminId}")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity removeAdmin(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                      @PathVariable Long adminId) {
        managerAdminService.removeAdmin(admin, adminId);
        return SimpleResponseEntity.delete();
    }
}