package com.cpto.dapp.manager.api.controller;

import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.pojo.vo.UserIncomeDetailLogVO;
import com.cpto.dapp.pojo.vo.UserIncomeLogVO;
import com.cpto.dapp.service.UserIncomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;

/**
 * 收益Controller
 *
 * @author sunli
 * @date 2019/04/26
 */
@Api(tags = "收益")
@RequestMapping("/users/{userId}/incomes")
@RestController
@Validated
public class IncomeController extends BaseController {

    @Autowired
    private UserIncomeService userIncomeService;

    @ApiOperation(value = "查询用户的各项目收益信息", notes = "根据用户id查询用户的各项目收益信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "path", defaultValue = "1")
    })
    @GetMapping
    @ManagerAuth
    public ResponseEntity<UserIncomeLogVO> findIncomeLogList(@PathVariable @NotNull(message = "用户id不能为空") Long userId) {
        return SimpleResponseEntity.get(userIncomeService.findUserIncomeProjectLogList(userId));
    }

    @ApiOperation(value = "查询用户的某项目详细收益信息", notes = "根据用户id和项目id查询用户的某项目详细收益信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", paramType = "path", defaultValue = "11", required = true),
            @ApiImplicitParam(name = "searchTime", value = "查询时间", dataType = "java.sql.Timestamp", paramType = "query", example = "2019-01-01 10:20:30"),
            @ApiImplicitParam(name = "page", value = "当前页数", dataType = "int", paramType = "query", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int", paramType = "query", defaultValue = "10", required = true)
    })
    @GetMapping("/projects/{projectId}")
    @ManagerAuth
    public ResponseEntity<PageVO<UserIncomeDetailLogVO>> findIncomeLogListByProject(@PathVariable @NotNull(message = "用户id不能为空") Long userId,
                                                                                    @PathVariable @NotNull(message = "项目id不能为空") Long projectId,
                                                                                    @RequestParam(required = false) Timestamp searchTime,
                                                                                    @RequestParam @NotNull(message = "当前页数不能为空") @PositiveOrZero Integer page,
                                                                                    @RequestParam @NotNull(message = "每页条数不能为空") @Positive Integer pageSize) {
        return SimpleResponseEntity.get(userIncomeService.findUserIncomeDetailLogListByProject(userId, projectId, searchTime, page, pageSize));
    }
}