package com.tyu.core.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* Created by crossoverFish on 2021/08/26
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    @ApiModelProperty(value="用户表主键")
    private Long id;

    @ApiModelProperty(value="用户名称")
    private String username;

    @ApiModelProperty(value="用户密码")
    private String password;

    @ApiModelProperty(value="逻辑删除")
    private String activeStatus;

    @ApiModelProperty(value="创建时间")
    private Date createTime;
}