package com.cpto.dapp.pojo.vo;

import com.cpto.dapp.common.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 分页列表信息
 *
 * @author sunli
 * @date 2019/03/01
 */
@ApiModel(value = "PageVO", description = "分页列表信息")
@Data
public class PageVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(position = 1, value = "列表数据")
    private List<T> rows;

    @ApiModelProperty(position = 2, value = "总条数", example = "20")
    private Long total;

    @ApiModelProperty(position = 3, value = "总页数", example = "2")
    private Integer totalPage;

    @ApiModelProperty(position = 4, value = "是否有下一页数据", example = "true")
    private Boolean hasNext;

    @ApiModelProperty(position = 5, value = "查询时间", example = "2019-01-01 10:20:30")
    private Timestamp searchTime;

    @ApiModelProperty(position = 6, value = "app查询开始时间", example = "2019-01-01 00:00:00")
    private String searchFromTime;

    @ApiModelProperty(position = 7, value = "app查询结束时间", example = "2019-12-31 23:59:59")
    private String searchToTime;

    public PageVO() {
        this.searchFromTime = DateUtil.fullFromTime("2019");
        this.searchToTime = DateUtil.fullToTime(DateUtil.today());
    }
}