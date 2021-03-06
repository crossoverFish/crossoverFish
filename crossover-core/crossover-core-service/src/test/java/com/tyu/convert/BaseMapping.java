package com.tyu.convert;
 
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.MapperConfig;
 
import java.util.List;
import java.util.stream.Stream;
 
/**
 * @author Administrator
 */
@MapperConfig
public interface BaseMapping<S, T> {
 
    /**
     * 映射同名属性
     * @param source
     * @return
     */
    T source2target(S source);
 
    /**
     * 反向映射同名属性
     * @param target
     * @return
     */
    @InheritInverseConfiguration(name = "source2target")
    S target2Source(T target);
 
    /**
     * 集合形式的映射同名属性
     * @param source
     * @return
     */
    @InheritConfiguration(name = "source2target")
    List<T> source2target(List<S> source);
 
    /**
     * 集合形式的反向映射同名属性
     * @param target
     * @return
     */
    @InheritConfiguration(name = "target2Source")
    List<S> target2Source(List<T> target);
 

}
 