package com.cpto.dapp.manager.api.controller;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.auth.annotation.CurrentAdmin;
import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.FeedbackDTO;
import com.cpto.dapp.pojo.vo.FeedbackVO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.service.FeedbackService;
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
 * 反馈Controller
 *
 * @author sunli
 * @date 2019/03/04
 */
@Api(tags = "反馈")
@RequestMapping("/feedbacks")
@RestController
@Validated
public class FeedbackController extends BaseController {

    @Autowired
    private FeedbackService feedbackService;

    @ApiOperation(value = "查询反馈", notes = "查询满足条件的反馈。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchTime", value = "查询时间", dataType = "java.sql.Timestamp", paramType = "query", defaultValue = "2019-01-01 10:20:30"),
            @ApiImplicitParam(name = "page", value = "当前页数", dataType = "int", paramType = "query", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int", paramType = "query", defaultValue = "10", required = true),
            @ApiImplicitParam(name = "feedbackType", value = "反馈类型（1：申请，2：投诉）", dataType = "int", paramType = "query", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "id", value = "反馈id", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "内容", paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "备注", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态（1：未处理，2：处理中，3：已处理）", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "用户名", paramType = "query"),
            @ApiImplicitParam(name = "fromCreatedTime", value = "创建开始时间", paramType = "query"),
            @ApiImplicitParam(name = "toCreatedTime", value = "创建结束时间", paramType = "query")
    })
    @GetMapping
    @ManagerAuth
    public ResponseEntity<PageVO<FeedbackVO>> searchFeedback(@RequestParam(required = false) Timestamp searchTime,
                                                             @RequestParam @NotNull(message = "当前页数不能为空") @PositiveOrZero Integer page,
                                                             @RequestParam @NotNull(message = "每页条数不能为空") @Positive Integer pageSize,
                                                             @RequestParam @NotNull(message = "反馈类型不能为空") Integer feedbackType,
                                                             @RequestParam(required = false) Long id,
                                                             @RequestParam(required = false) String content,
                                                             @RequestParam(required = false) String remark,
                                                             @RequestParam(required = false) Integer status,
                                                             @RequestParam(required = false) Long userId,
                                                             @RequestParam(required = false) String userName,
                                                             @RequestParam(required = false) String fromCreatedTime,
                                                             @RequestParam(required = false) String toCreatedTime) {
        return SimpleResponseEntity.get(feedbackService.searchFeedback(searchTime, page, pageSize, feedbackType, id, content, remark, status, userId, userName, fromCreatedTime, toCreatedTime));
    }

    @ApiOperation(value = "取得反馈详情", notes = "根据反馈id，取得反馈详情。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "feedbackId", value = "反馈id", paramType = "path", defaultValue = "1", required = true)
    })
    @GetMapping("/{feedbackId}")
    @ManagerAuth
    public ResponseEntity<FeedbackVO> findFeedback(@PathVariable @NotNull(message = "反馈id不能为空") Long feedbackId) {
        return SimpleResponseEntity.get(feedbackService.findFeedback(feedbackId));
    }

    @ApiOperation(value = "修改反馈", notes = "修改反馈详情。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "feedbackId", value = "反馈id", paramType = "path", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "feedbackDTO", value = "反馈信息", paramType = "body", dataType = "FeedbackDTO", required = true)
    })
    @PutMapping("/{feedbackId}")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity modifyFeedback(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                         @PathVariable @NotNull(message = "反馈id不能为空") Long feedbackId,
                                         @RequestBody @Validated FeedbackDTO feedbackDTO) {
        feedbackService.modifyFeedback(admin, feedbackId, feedbackDTO);
        return SimpleResponseEntity.put();
    }
}