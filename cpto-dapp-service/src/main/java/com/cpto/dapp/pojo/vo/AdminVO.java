package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 管理员信息
 *
 * @author sunli
 * @date 2018/12/07
 */
@ApiModel(value = "AdminVO", description = "管理员信息")
@Data
public class AdminVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "用户名", example = "root")
    private String userName;

    @ApiModelProperty(position = 2, value = "姓名", example = "张三")
    private String name;

    @ApiModelProperty(position = 3, value = "角色类型（1：系统管理员，2：超级管理员，3：普通管理员）", example = "1")
    private Integer roleType;

    @ApiModelProperty(position = 4, value = "角色类型名", example = "系统管理员")
    private String roleTypeName;
}