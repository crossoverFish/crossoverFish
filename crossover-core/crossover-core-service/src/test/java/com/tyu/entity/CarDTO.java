package com.tyu.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CarDTO {
 
    private Long id;
 
    private Double totalPrice;
 
    private String createDate;
 
    private String color;
 
    private String name;
 
    private CarDetailDTO carDetailDTO;
 
    private List<PartDTO> partDTOList;
}

