package com.cpto.dapp.api.controller;

import com.cpto.dapp.auth.annotation.Auth;
import com.cpto.dapp.auth.annotation.CurrentUser;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.User;
import com.cpto.dapp.pojo.dto.FeedbackDTO;
import com.cpto.dapp.service.FeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 反馈Controller
 *
 * @author sunli
 * @date 2019/01/29
 */
@Api(tags = "反馈")
@RequestMapping("/users/self/feedbacks")
@RestController
@Validated
public class FeedbackController extends BaseController {

    @Autowired
    private FeedbackService feedbackService;

    @ApiOperation(value = "提交反馈", notes = "添加反馈信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "feedbackDTO", value = "反馈信息", paramType = "body", dataType = "FeedbackDTO", required = true)
    })
    @PostMapping
    @Auth
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity addFeedback(@ApiIgnore @CurrentUser User user,
                                      @RequestBody @Validated FeedbackDTO feedbackDTO) {
        feedbackService.addFeedback(user, feedbackDTO);
        return SimpleResponseEntity.post();
    }
}