package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 用户信息
 *
 * @author sunli
 * @date 2019/03/04
 */
@ApiModel(value = "UserDTO", description = "用户信息")
@Data
public class UserDTO extends ManagerBaseDTO {
}