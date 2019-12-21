package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 公告信息
 *
 * @author sunli
 * @date 2019/02/18
 */
@ApiModel(value = "NoticeDTO", description = "公告信息")
@Data
public class NoticeDTO extends ManagerBaseDTO {

    @ApiModelProperty(position = 1, value = "标题（中文）", example = "标题（中文）", required = true)
    @NotBlank(message = "标题（中文）不能为空")
    @Size(max = 50, message = "标题（中文）必须为50位以内字符")
    private String titleZh;

    @ApiModelProperty(position = 2, value = "标题（英文）", example = "titleEn", required = true)
    @NotBlank(message = "标题（英文）不能为空")
    @Size(max = 50, message = "标题（英文）必须为50位以内字符")
    private String titleEn;

    @ApiModelProperty(position = 3, value = "编号", example = "cpto201901")
    @Size(max = 20, message = "编号必须为20位以内字符")
    private String no;

    @ApiModelProperty(position = 4, value = "作者（中文）", example = "张三")
    @Size(max = 50, message = "作者（中文）必须为50位以内字符")
    private String authorZh;

    @ApiModelProperty(position = 5, value = "作者（英文）", example = "authorEn")
    @Size(max = 50, message = "作者（英文）必须为50位以内字符")
    private String authorEn;

    @ApiModelProperty(position = 6, value = "网址（中文）", example = "https://www.baidu.com")
    @Size(max = 2000, message = "网址（中文）必须为2000位以内字符")
    private String urlZh;

    @ApiModelProperty(position = 7, value = "网址（英文）", example = "https://www.google.com")
    @Size(max = 2000, message = "网址（英文）必须为2000位以内字符")
    private String urlEn;

    @ApiModelProperty(position = 8, value = "内容（中文）", example = "内容")
    private String contentZh;

    @ApiModelProperty(position = 9, value = "内容（英文）", example = "contentEn")
    private String contentEn;

    @ApiModelProperty(position = 10, value = "公告类型（1：项目公告，2：回报公告）", example = "1", required = true)
    @NotNull(message = "公告类型不能为空")
    @Range(min = 1, max = 2, message = "公告类型错误")
    private Integer noticeType;
}