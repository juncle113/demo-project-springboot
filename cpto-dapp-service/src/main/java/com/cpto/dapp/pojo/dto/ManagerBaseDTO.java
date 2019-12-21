package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

/**
 * ManagerBaseDTO
 *
 * @author sunli
 * @date 2019/04/15
 */
@Data
public class ManagerBaseDTO extends BaseDTO {

    @ApiModelProperty(position = 101, value = "状态", example = "1")
    private Integer status;

    @ApiModelProperty(position = 102, value = "管理备注", example = "备注内容")
    @Size(max = 200, message = "备注必须为200位以内字符")
    private String remark;

    @ApiModelProperty(position = 103, value = "最后修改时间", example = "2018-11-30 11:22:33", dataType = "java.util.Date")
    @Past(message = "最后修改时间必须是过去的时间")
    private Timestamp modifiedTime;
}