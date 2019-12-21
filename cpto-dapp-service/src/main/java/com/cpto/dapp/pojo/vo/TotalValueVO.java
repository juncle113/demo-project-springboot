package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 价值总量信息
 *
 * @author sunli
 * @date 2019/01/09
 */
@ApiModel(value = "TotalValueVO", description = "价值总量信息")
@Data
public class TotalValueVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "价值概要")
    private List<ValueSummary> valueSummaries;

    @ApiModelProperty(position = 2, value = "总量分配")
    private List<TotalAllocation> totalAllocations;

    @ApiModelProperty(position = 3, value = "流通数额", example = "10000000")
    private BigDecimal circulationAmount;

    @ApiModelProperty(position = 4, value = "流通来源")
    private List<CirculationSource> circulationSources;

    @ApiModel(value = "ValueSummary", description = "价值概要")
    @Data
    public class ValueSummary {

        @ApiModelProperty(position = 5, value = "序号", example = "1")
        private Integer sortNo;

        @ApiModelProperty(position = 6, value = "标题", example = "完结投资项目日产油量：")
        private String title;

        @ApiModelProperty(position = 7, value = "数额", example = "100")
        private BigDecimal amount;

    }

    @ApiModel(value = "TotalAllocation", description = "总量分配")
    @Data
    public class TotalAllocation {

        @ApiModelProperty(position = 8, value = "序号", example = "1")
        private Integer sortNo;

        @ApiModelProperty(position = 9, value = "标题（中文）", example = "完结投资项目日产油量：")
        private String title;

        @ApiModelProperty(position = 10, value = "数额", example = "0.1")
        private BigDecimal amount;

        @ApiModelProperty(position = 11, value = "颜色", example = "#FFFFFF")
        private String color;
    }

    @ApiModel(value = "CirculationSource", description = "流通来源")
    @Data
    public class CirculationSource {

        @ApiModelProperty(position = 12, value = "序号", example = "1")
        private Integer sortNo;

        @ApiModelProperty(position = 13, value = "标题", example = "市场流通")
        private String title;

        @ApiModelProperty(position = 14, value = "数额", example = "2000000")
        private BigDecimal amount;

        @ApiModelProperty(position = 15, value = "颜色", example = "#FFFFFF")
        private String color;
    }
}