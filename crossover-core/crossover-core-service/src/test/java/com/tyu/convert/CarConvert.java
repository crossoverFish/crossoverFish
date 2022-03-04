package com.tyu.convert;

import com.tyu.entity.*;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarConvert {
 
    // 如果用了上面那个 @Mapper(componentModel = "spring") ，就是跟spring 整合了，其实就不需要用这中方式调用，这行代码就可以注释掉
    CarConvert INSTANCE = Mappers.getMapper(CarConvert.class);
 
    @Mappings({
            @Mapping(source = "totalPrice", target = "totalPrice", numberFormat = "#.00"),
            @Mapping(source = "createDate", target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "color", ignore = true),
            @Mapping(source = "name", target = "carName"),
            @Mapping(source = "carDetailDTO", target = "carDetailVO") // 引用对象转换
    })
    CarVO toCarVO(CarDTO carDTO);
 
 
    /**
     * 这个方法也可以用于上面那个引用对象转换
     */
    @Mapping(source = "id", target = "carDetailId")
    @Mapping(source = "code", target = "carDetailCode")
    CarDetailVO carDetailDTOToCarDetailVo(CarDetailDTO carDetailDTO);
 
    /**
     * 自定义属性的映射
     *
     * @AfterMapping :表示让mapstruct在调用完成自动转换的方法之后，会来自动调用本方法
     * @MappingTarget :表示传来的carVO对象是已经赋值过的
     */
    @AfterMapping
    public default void carDTOToVOAfter(CarDTO carDTO,@MappingTarget CarVO carVO){
        if (!CollectionUtils.isEmpty(carDTO.getPartDTOList())){
            carVO.setHasPart(true);
        }
    }
 
    /**
     * 集合批量转换（就是上面这个方法的批量转换-toCarVO）
     */
    List<CarVO> toCarVOList(List<CarDTO> carDTOList);
 
    /**
     * 忽略mapstruct 的默认的映射行为
     *
     * 下面这个ignore 就是忽略了 totalPrice 属性的映射，不让这个属性值被映射过来；
     * 但是若是一个类中我们需要忽略的属性有几十个，可能要写几十行这个@Mapping(ignore=),非常不方便
     *
     * 所以可以通过@BeanMapping 来进行配置
     */
    @Mapping(source = "totalPrice", target = "totalPrice", ignore = true)
//    BaoMaVO toBaoMaVO(CarDTO carDTO);
 
    /**
     * 配置忽略mapstruct的默认映射行为，只映射那些配置了@Mapping的属性
     */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id") // 这里只映射id属性
    @Mapping(source = "name", target = "brandName") // 这里还映射name属性
    BaoMaVO toBaoMaVO2(CarDTO carDTO);





    /**
     * 更新场景：继承配置，避免同样的配置写多份
     */
    @InheritConfiguration
    void updateBaoMaVo(CarDTO carDTO,@MappingTarget BaoMaVO baoMaVO);
 
}