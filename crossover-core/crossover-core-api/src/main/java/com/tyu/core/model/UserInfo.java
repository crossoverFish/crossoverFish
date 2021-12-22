package com.tyu.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value="创建时间")
    private Date createTime;
}