package com.cpto.dapp.api.controller;

import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.enums.StatusEnum;
import com.cpto.dapp.pojo.vo.NoticeVO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.service.NoticeService;
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
 * 公告Controller
 *
 * @author sunli
 * @date 2019/02/18
 */
@Api(tags = "公告")
@RequestMapping(value = "/notices")
@RestController
@Validated
public class NoticeController extends BaseController {

    @Autowired
    private NoticeService noticeService;

    @ApiOperation(value = "取得公告列表【非token认证】", notes = "根据公告类型取得有效的公告列表。", tags = "非token认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchTime", value = "查询时间", dataType = "java.sql.Timestamp", paramType = "query", example = "2019-01-01 10:20:30"),
            @ApiImplicitParam(name = "page", value = "当前页数", dataType = "int", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "noticeType", value = "公告类型（1：项目公告，2：回报公告）", dataType = "int", paramType = "query", defaultValue = "1")
    })
    @GetMapping
    public ResponseEntity<PageVO<NoticeVO>> findNoticeListByValid(@RequestParam(required = false) Timestamp searchTime,
                                                                  @RequestParam @NotNull(message = "当前页数不能为空") @PositiveOrZero Integer page,
                                                                  @RequestParam @NotNull(message = "每页条数不能为空") @Positive Integer pageSize,
                                                                  @RequestParam(required = false) Integer noticeType) {
        return SimpleResponseEntity.get(noticeService.searchNotice(searchTime, page, pageSize, noticeType, StatusEnum.VALID.getCode()));
    }

    @ApiOperation(value = "取得公告详情【非token认证】", notes = "根据公告id，取得公告详情。", tags = "非token认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "noticeId", value = "公告id", paramType = "path", defaultValue = "1", required = true)
    })
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeVO> findNotice(@PathVariable @NotNull(message = "公告id不能为空") Long noticeId) {
        return SimpleResponseEntity.get(noticeService.findNotice(noticeId));
    }
}