package com.cpto.dapp.manager.api.controller;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.auth.annotation.CurrentAdmin;
import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.UserDTO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.pojo.vo.UserVO;
import com.cpto.dapp.service.UserService;
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

/**
 * 用户Controller
 *
 * @author sunli
 * @date 2019/02/19
 */
@Api(tags = "用户")
@RequestMapping("/users")
@RestController
@Validated
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "查询用户", notes = "查询满足条件的用户。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchTime", value = "查询时间", dataType = "java.sql.Timestamp", paramType = "query", example = "2019-01-01 10:20:30"),
            @ApiImplicitParam(name = "page", value = "当前页数", dataType = "int", paramType = "query", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int", paramType = "query", defaultValue = "10", required = true),
            @ApiImplicitParam(name = "id", value = "用户id", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "用户名", paramType = "query"),
            @ApiImplicitParam(name = "areaCode", value = "手机号", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号归属地代码", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "邮箱", paramType = "query"),
            @ApiImplicitParam(name = "isSubscribeMail", value = "是否订阅", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "inviteCode", value = "邀请码", paramType = "query"),
            @ApiImplicitParam(name = "parentId", value = "上级邀请人id", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态（1：已支付，2：已取消，3：已完成，4：已失效）", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "备注", paramType = "query"),
            @ApiImplicitParam(name = "fromCreatedTime", value = "创建开始时间", paramType = "query"),
            @ApiImplicitParam(name = "toCreatedTime", value = "创建结束时间", paramType = "query")
    })
    @GetMapping
    @ManagerAuth
    public ResponseEntity<PageVO<UserVO>> searchUser(@RequestParam(required = false) Timestamp searchTime,
                                                     @RequestParam @NotNull(message = "当前页数不能为空") @PositiveOrZero Integer page,
                                                     @RequestParam @NotNull(message = "每页条数不能为空") @Positive Integer pageSize,
                                                     @RequestParam(required = false) Long id,
                                                     @RequestParam(required = false) String userName,
                                                     @RequestParam(required = false) String areaCode,
                                                     @RequestParam(required = false) String phone,
                                                     @RequestParam(required = false) String email,
                                                     @RequestParam(required = false) Boolean isSubscribeMail,
                                                     @RequestParam(required = false) String inviteCode,
                                                     @RequestParam(required = false) Long parentId,
                                                     @RequestParam(required = false) Integer status,
                                                     @RequestParam(required = false) String remark,
                                                     @RequestParam(required = false) String fromCreatedTime,
                                                     @RequestParam(required = false) String toCreatedTime) {
        return SimpleResponseEntity.get(userService.searchUser(searchTime, page, pageSize, id, userName, areaCode, phone, email, isSubscribeMail, inviteCode, parentId, status, remark, fromCreatedTime, toCreatedTime));
    }

    @ApiOperation(value = "取得用户信息", notes = "根据用户id，取得用户信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "long", paramType = "path", defaultValue = "1", required = true)
    })
    @GetMapping("/{userId}")
    @ManagerAuth
    public ResponseEntity<UserVO> findUser(@PathVariable @NotNull(message = "用户id不能为空") Long userId) {
        return SimpleResponseEntity.get(userService.findUser(userId));
    }

    @ApiOperation(value = "修改用户信息", notes = "根据用户id，修改用户信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "path", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "userDTO", value = "用户信息", paramType = "body", dataType = "UserDTO", required = true)
    })
    @PutMapping("/{userId}")
    @ManagerAuth(value = AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity modifyUser(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                     @PathVariable @NotNull(message = "用户id不能为空") Long userId,
                                     @RequestBody @Validated UserDTO userDTO) {
        userService.modifyUser(admin, userId, userDTO);
        return SimpleResponseEntity.put();
    }

    @ApiOperation(value = "导出用户邮箱列表", notes = "将订阅邮件的用户邮箱地址导出到csv文件中。")
    @GetMapping("/email/download")
    // TODO【改善】后台管理中window.open()的请求方式无法通过header传递token，所以无法验证权限
//    @ManagerAuth
    public ResponseEntity downloadEmail() {
        return userService.downloadEmail();
    }
}