package com.cpto.dapp.api.controller;

import com.cpto.dapp.auth.annotation.Auth;
import com.cpto.dapp.auth.annotation.CurrentUser;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.User;
import com.cpto.dapp.pojo.dto.*;
import com.cpto.dapp.pojo.vo.InviteVO;
import com.cpto.dapp.pojo.vo.LoginVO;
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

/**
 * 用户Controller
 *
 * @author sunli
 * @date 2018/12/29
 */
@Api(tags = "用户")
@RequestMapping("/users")
@RestController
@Validated
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "注册【非token认证】", notes = "注册新的用户。", tags = "非token认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "registerDTO", value = "用户注册信息", paramType = "body", dataType = "RegisterDTO", required = true)
    })
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity register(@RequestBody @Validated RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return SimpleResponseEntity.post();
    }

    @ApiOperation(value = "登录【非token认证】", notes = "通过检查用户名和密码，完成登录。", tags = "非token认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginDTO", value = "用户登录信息", paramType = "body", dataType = "LoginDTO", required = true)
    })
    @PostMapping("/token")
    public ResponseEntity<LoginVO> login(@RequestBody @Validated LoginDTO loginDTO) {
        return SimpleResponseEntity.post(userService.login(loginDTO));
    }

    @ApiOperation(value = "注销", notes = "清除缓存中当前登录账号的token。")
    @DeleteMapping("/self/token")
    @Auth
    public ResponseEntity logout(@ApiIgnore @CurrentUser User user) {
        userService.logout(user);
        return SimpleResponseEntity.delete();
    }

    @ApiOperation(value = "取得用户信息", notes = "根据用户id，取得用户信息。")
    @GetMapping("/self")
    @Auth
    public ResponseEntity<UserVO> findUser(@ApiIgnore @CurrentUser User user) {
        return SimpleResponseEntity.get(userService.findUser(user));
    }

    @ApiOperation(value = "修改登录密码", notes = "修改登录密码时，设置新的密码。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modifyPasswordDTO", value = "修改登录密码信息", paramType = "body", dataType = "ModifyPasswordDTO", required = true)
    })
    @PutMapping("/self/password")
    @Auth
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity modifyPassword(@ApiIgnore @CurrentUser User user,
                                         @RequestBody @Validated ModifyPasswordDTO modifyPasswordDTO) {
        userService.modifyPassword(user, modifyPasswordDTO);
        return SimpleResponseEntity.put();
    }

    @ApiOperation(value = "重置登录密码【非token认证】", notes = "忘记登录密码时，设置新的密码。", tags = "非token认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resetPasswordDTO", value = "重置登录密码信息", paramType = "body", dataType = "ResetPasswordDTO", required = true)
    })
    @PutMapping("/password")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity resetPassword(@RequestBody @Validated ResetPasswordDTO resetPasswordDTO) {
        userService.resetPassword(resetPasswordDTO);
        return SimpleResponseEntity.put();
    }

    @ApiOperation(value = "修改支付密码", notes = "修改支付密码时，设置新的密码。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modifyPayPasswordDTO", value = "修改支付密码信息", paramType = "body", dataType = "ModifyPayPasswordDTO", required = true)
    })
    @PutMapping("/self/pay-password")
    @Auth
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity modifyPayPassword(@ApiIgnore @CurrentUser User user,
                                            @RequestBody @Validated ModifyPayPasswordDTO modifyPayPasswordDTO) {
        userService.modifyPayPassword(user, modifyPayPasswordDTO);
        return SimpleResponseEntity.put();
    }

    @ApiOperation(value = "重置支付密码【非token认证】", notes = "忘记支付密码时，设置新的密码。", tags = "非token认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resetPayPasswordDTO", value = "重置支付密码信息", paramType = "body", dataType = "ResetPayPasswordDTO", required = true)
    })
    @PutMapping("/pay-password")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity resetPayPassword(@RequestBody @Validated ResetPayPasswordDTO resetPayPasswordDTO) {
        userService.resetPayPassword(resetPayPasswordDTO);
        return SimpleResponseEntity.put();
    }

    @ApiOperation(value = "绑定手机号", notes = "注册成功后，修改手机号。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "setPhoneDTO", value = "绑定手机号信息", paramType = "body", dataType = "SetPhoneDTO", required = true)
    })
    @PutMapping("/self/phone")
    @Auth
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity setPhone(@ApiIgnore @CurrentUser User user,
                                   @RequestBody @Validated SetPhoneDTO setPhoneDTO) {
        userService.setPhone(user, setPhoneDTO);
        return SimpleResponseEntity.put();
    }

    @ApiOperation(value = "绑定邮箱", notes = "注册成功后，修改邮箱。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "setEmailDTO", value = "绑定邮箱信息", paramType = "body", dataType = "SetEmailDTO", required = true)
    })
    @PutMapping("/self/email")
    @Auth
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity setEmail(@ApiIgnore @CurrentUser User user,
                                   @RequestBody @Validated SetEmailDTO setEmailDTO) {
        userService.setEmail(user, setEmailDTO);
        return SimpleResponseEntity.put();
    }

    @ApiOperation(value = "设置是否订阅邮件", notes = "打开或关闭订阅邮件功能。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subscribeMailDTO", value = "订阅邮件信息", paramType = "body", dataType = "SubscribeMailDTO", required = true)
    })
    @PutMapping("/self/email/subscription")
    @Auth
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity subscribeMail(@ApiIgnore @CurrentUser User user,
                                        @RequestBody @Validated SubscribeMailDTO subscribeMailDTO) {
        userService.subscribeMail(user, subscribeMailDTO);
        return SimpleResponseEntity.put();
    }

    @ApiOperation(value = "取得邀请信息", notes = "取得当前用户邀请码、邀请次数和app下载地址等信息。")
    @GetMapping("/self/invite")
    @Auth
    public ResponseEntity<InviteVO> findInviteInfo(@ApiIgnore @CurrentUser User user) {
        return SimpleResponseEntity.get(userService.findInviteInfo(user));
    }

    @ApiOperation(value = "根据邀请码查询用户信息【非token认证】", notes = "根据邀请码查询用户信息", tags = "非token认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inviteCode", value = "邀请码", paramType = "query", defaultValue = "ABCD1111", required = true)
    })
    @GetMapping("/invites/{inviteCode}")
    public ResponseEntity<UserVO> findUserByInviteCode(@PathVariable @NotNull(message = "邀请码不能为空") String inviteCode) {
        return SimpleResponseEntity.get(userService.findUserByInviteCode(inviteCode));
    }
}