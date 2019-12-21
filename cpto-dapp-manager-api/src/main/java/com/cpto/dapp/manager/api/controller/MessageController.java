package com.cpto.dapp.manager.api.controller;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.auth.annotation.CurrentAdmin;
import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.Add;
import com.cpto.dapp.pojo.dto.MessageDTO;
import com.cpto.dapp.pojo.vo.MessageVO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.service.MessageService;
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
 * 消息Controller
 *
 * @author sunli
 * @date 2019/02/18
 */
@Api(tags = "消息")
@RequestMapping("/messages")
@RestController
@Validated
public class MessageController extends BaseController {

    @Autowired
    private MessageService messageService;

    @ApiOperation(value = "查询消息", notes = "查询满足条件的消息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchTime", value = "查询时间", dataType = "java.sql.Timestamp", paramType = "query", example = "2019-01-01 10:20:30"),
            @ApiImplicitParam(name = "page", value = "当前页数", dataType = "int", paramType = "query", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int", paramType = "query", defaultValue = "10", required = true),
            @ApiImplicitParam(name = "messageType", value = "消息类型（1：系统消息，2：投资消息）", dataType = "int", paramType = "query", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "status", value = "状态（1：有效，2：无效）", dataType = "int", paramType = "query")
    })
    @GetMapping
    @ManagerAuth
    public ResponseEntity<PageVO<MessageVO>> searchMessage(@RequestParam(required = false) Timestamp searchTime,
                                                           @RequestParam @NotNull(message = "当前页数不能为空") @PositiveOrZero Integer page,
                                                           @RequestParam @NotNull(message = "每页条数不能为空") @Positive Integer pageSize,
                                                           @RequestParam(required = false) Integer messageType,
                                                           @RequestParam(required = false) Integer status) {
        return SimpleResponseEntity.get(messageService.searchMessage(searchTime, page, pageSize, messageType, status));
    }

    @ApiOperation(value = "取得消息", notes = "根据id取得消息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageId", value = "消息id", dataType = "long", paramType = "path", defaultValue = "1", required = true)
    })
    @GetMapping("/{messageId}")
    @ManagerAuth
    public ResponseEntity<MessageVO> findMessage(@PathVariable @NotNull(message = "消息id不能为空") Long messageId) {
        return SimpleResponseEntity.get(messageService.findMessage(messageId));
    }

    @ApiOperation(value = "新建消息", notes = "新建消息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageDTO", value = "消息信息", paramType = "body", dataType = "MessageDTO", required = true)
    })
    @PostMapping
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity addMessage(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                     @RequestBody @Validated({Add.class}) MessageDTO messageDTO) {
        messageService.addMessage(admin, messageDTO);
        return SimpleResponseEntity.post();
    }

    @ApiOperation(value = "修改消息", notes = "修改消息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageId", value = "消息id", paramType = "path", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "messageDTO", value = "消息信息", paramType = "body", dataType = "MessageDTO", required = true)
    })
    @PutMapping("/{messageId}")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity modifyMessage(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                        @PathVariable @NotNull(message = "管理员id不能为空") Long messageId,
                                        @RequestBody @Validated() MessageDTO messageDTO) {
        messageService.modifyMessage(admin, messageId, messageDTO);
        return SimpleResponseEntity.put();
    }

    @ApiOperation(value = "删除消息", notes = "删除消息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageId", value = "消息id", paramType = "path", defaultValue = "1", required = true)
    })
    @DeleteMapping("/{messageId}")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity removeMessage(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                        @PathVariable Long messageId) {
        messageService.removeMessage(admin, messageId);
        return SimpleResponseEntity.delete();
    }
}