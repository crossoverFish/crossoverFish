package com.tyu.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author crossoverFish
 * @version 1.0.0
 * @Description TODO
 * @date 2022/02/21 10:13
 */
@Data
public class CarVO {

    private Long id;

    private Double totalPrice;

    private Date createDate;

    private String color;

    private String carName;

    private CarDetailVO carDetailVO;

    private List<PartVo> partVoList;

    // 判断partDTOList是否有值
    private Boolean hasPart;
}