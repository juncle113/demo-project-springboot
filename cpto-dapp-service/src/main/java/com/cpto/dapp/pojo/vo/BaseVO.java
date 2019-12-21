package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * BaseVO
 *
 * @author sunli
 * @date 2018/12/07
 */
@Data
public class BaseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(position = 101, value = "id", example = "1")
    private Long id;

    @ApiModelProperty(position = 102, value = "状态", example = "1")
    private Integer status;

    @ApiModelProperty(position = 103, value = "状态名称", example = "有效")
    private String statusName;

    @ApiModelProperty(position = 104, value = "备注", example = "备注内容")
    private String remark;

    @ApiModelProperty(position = 105, value = "创建人", example = "root")
    private String createdBy;

    @ApiModelProperty(position = 106, value = "创建时间", example = "2019-01-15 20:00:00", dataType = "java.util.Date")
    private Timestamp createdTime;

    @ApiModelProperty(position = 107, value = "修改人", example = "root")
    private String modifiedBy;

    @ApiModelProperty(position = 108, value = "修改时间", example = "2018-11-30 11:22:33", dataType = "java.util.Date")
    private Timestamp modifiedTime;
}