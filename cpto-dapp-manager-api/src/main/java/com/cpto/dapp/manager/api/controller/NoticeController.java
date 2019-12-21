package com.cpto.dapp.manager.api.controller;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.auth.annotation.CurrentAdmin;
import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.NoticeDTO;
import com.cpto.dapp.pojo.vo.NoticeVO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.service.NoticeService;
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
 * 公告Controller
 *
 * @author sunli
 * @date 2018/12/18
 */
@Api(tags = "公告")
@RequestMapping(value = "/notices")
@RestController
@Validated
public class NoticeController extends BaseController {

    @Autowired
    private NoticeService noticeService;

    @ApiOperation(value = "新增公告", notes = "使用管理员账户添加公告。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "noticeDTO", value = "公告信息", paramType = "body", dataType = "NoticeDTO", required = true)
    })
    @PostMapping
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity addNotice(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                    @RequestBody @Validated NoticeDTO noticeDTO) {
        noticeService.addNotice(admin, noticeDTO);
        return SimpleResponseEntity.post();
    }

    @ApiOperation(value = "修改公告", notes = "修改公告详情。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "noticeId", value = "公告id", paramType = "path", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "noticeDTO", value = "公告信息", paramType = "body", dataType = "NoticeDTO", required = true)
    })
    @PutMapping("/{noticeId}")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity modifyNotice(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                       @PathVariable @NotNull(message = "公告id不能为空") Long noticeId,
                                       @RequestBody @Validated NoticeDTO noticeDTO) {
        noticeService.modifyNotice(admin, noticeId, noticeDTO);
        return SimpleResponseEntity.put();
    }

    @ApiOperation(value = "删除公告", notes = "逻辑删除公告。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "noticeId", value = "公告id", paramType = "path", defaultValue = "1", required = true)
    })
    @DeleteMapping("/{noticeId}")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity removeNotice(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                       @PathVariable Long noticeId) {
        noticeService.removeNotice(admin, noticeId);
        return SimpleResponseEntity.delete();
    }

    @ApiOperation(value = "查询公告", notes = "查询满足条件的公告。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchTime", value = "查询时间", dataType = "java.sql.Timestamp", paramType = "query", defaultValue = "2019-01-01 10:20:30"),
            @ApiImplicitParam(name = "page", value = "当前页数", dataType = "int", paramType = "query", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int", paramType = "query", defaultValue = "10", required = true),
            @ApiImplicitParam(name = "noticeType", value = "公告类型（1：项目公告，2：回报公告）", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "status", value = "状态（1：有效，2：无效）", dataType = "int", paramType = "query", defaultValue = "1")
    })
    @GetMapping
    @ManagerAuth
    public ResponseEntity<PageVO<NoticeVO>> searchNotice(@RequestParam(required = false) Timestamp searchTime,
                                                         @RequestParam @NotNull(message = "当前页数不能为空") @PositiveOrZero Integer page,
                                                         @RequestParam @NotNull(message = "每页条数不能为空") @Positive Integer pageSize,
                                                         @RequestParam(required = false) Integer noticeType,
                                                         @RequestParam(required = false) Integer status) {
        return SimpleResponseEntity.get(noticeService.searchNotice(searchTime, page, pageSize, noticeType, status));
    }

    @ApiOperation(value = "取得公告详情", notes = "根据公告id，取得公告详情。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "noticeId", value = "公告id", paramType = "path", defaultValue = "1", required = true)
    })
    @GetMapping("/{noticeId}")
    @ManagerAuth
    public ResponseEntity<NoticeVO> findNotice(@PathVariable @NotNull(message = "公告id不能为空") Long noticeId) {
        return SimpleResponseEntity.get(noticeService.findNotice(noticeId));
    }
}
