package com.cpto.dapp.api.controller;

import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.enums.StatusEnum;
import com.cpto.dapp.pojo.vo.MessageVO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.service.MessageService;
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

    @ApiOperation(value = "取得消息列表【非token认证】", notes = "取得消息列表。", tags = "非token认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchTime", value = "查询时间", dataType = "java.sql.Timestamp", paramType = "query", example = "2019-01-01 10:20:30"),
            @ApiImplicitParam(name = "page", value = "当前页数", dataType = "int", paramType = "query", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int", paramType = "query", defaultValue = "10", required = true),
            @ApiImplicitParam(name = "messageType", value = "消息类型（1：系统消息，2：投资消息）", dataType = "int", paramType = "query", defaultValue = "1")
    })
    @GetMapping
    public ResponseEntity<PageVO<MessageVO>> findMessageList(@RequestParam(required = false) Timestamp searchTime,
                                                             @RequestParam @NotNull(message = "当前页数不能为空") @PositiveOrZero Integer page,
                                                             @RequestParam @NotNull(message = "每页条数不能为空") @Positive Integer pageSize,
                                                             @RequestParam(required = false) Integer messageType) {
        return SimpleResponseEntity.get(messageService.searchMessage(searchTime, page, pageSize, messageType, StatusEnum.VALID.getCode()));
    }
}