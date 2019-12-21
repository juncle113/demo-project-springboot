package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 公告信息
 *
 * @author sunli
 * @date 2019/02/18
 */
@ApiModel(value = "NoticeVO", description = "公告信息")
@Data
public class NoticeVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "标题", example = "标题")
    private String title;

    @ApiModelProperty(position = 2, value = "标题（中文）", example = "标题")
    private String titleZh;

    @ApiModelProperty(position = 3, value = "标题（英文）", example = "titleEn")
    private String titleEn;

    @ApiModelProperty(position = 4, value = "编号", example = "CPTO201901")
    private String no;

    @ApiModelProperty(position = 5, value = "作者", example = "张三")
    private String author;

    @ApiModelProperty(position = 6, value = "作者（中文）", example = "张三")
    private String authorZh;

    @ApiModelProperty(position = 7, value = "作者（英文）", example = "authorEn")
    private String authorEn;

    @ApiModelProperty(position = 8, value = "网址", example = "https://www.baidu.com")
    private String url;

    @ApiModelProperty(position = 9, value = "网址（中文）", example = "https://www.baidu.com")
    private String urlZh;

    @ApiModelProperty(position = 10, value = "网址（英文）", example = "https://www.google.com")
    private String urlEn;

    @ApiModelProperty(position = 11, value = "内容", example = "内容")
    private String content;

    @ApiModelProperty(position = 12, value = "内容（中文）", example = "内容")
    private String contentZh;

    @ApiModelProperty(position = 13, value = "内容（英文）", example = "contentEn")
    private String contentEn;

    @ApiModelProperty(position = 14, value = "公告类型（1：项目公告，2：回报公告）", example = "1")
    private Integer noticeType;

    @ApiModelProperty(position = 15, value = "公告类型名称", example = "项目公告")
    private String noticeTypeName;

    @ApiModelProperty(position = 16, value = "资源信息")
    private List<SourceVO> sources;
}