package com.tyu.tooltest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

/**
 * @author crossoverFish
 * @version 1.0.0
 * @Description TODO
 * @date 2022/02/18 15:03
 */
public class MapStructTest {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class A {
        String name;
        int age;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class B {
        String userName;
        int age;
    }

    @Mapper
    public interface ObjectConvert {
        ObjectConvert INSTANCE = Mappers.getMapper(ObjectConvert.class);

        @Mapping(source = "name",target = "userName")
        B convert(A a);


        List<B> convertList(List<A> list);
    }


    public static void main(String[] args) {
//        testSingle();

        testMulti();
    }

    private static void testMulti() {
        List<A> aList = Arrays.asList(A.builder().name("tyu").age(11).build(), A.builder().name("zxf").age(12).build());
        List<B> bList = ObjectConvert.INSTANCE.convertList(aList);
        System.out.println(bList);
    }

    private static void testSingle() {
        A sss = A.builder().name("sss").age(111).build();
        B convert = ObjectConvert.INSTANCE.convert(sss);
        System.out.println(convert);
    }
}