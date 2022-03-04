package com.tyu.entity;

/**
 * @author crossoverFish
 * @version 1.0.0
 * @Description TODO
 * @date 2022/02/16 15:21
 */
public class Cat {
    private static int age = 1;
    private  String name = "tyu";

    public static class Clothes {
        private String color = "yellow";
        private static int condition = 8;
        /**
         * 静态内部类获取不到外部类的非静态属性
         */
        public void sweater(){
            System.out.println("我的岁数是:" + age + "我的毛衣颜色是:"+ color);            
        }
    }

    /**
     * 非静态内部类可以获取外部类的任意属性
     */
    public class Shoes {
        private String size = "41";
        public void nike(){
            System.out.println("我的nike尺寸是:" + size + "我的岁数是:" + age + "我的名字是:" + name);
        }
    }

    /**
     * 外部类获取不到静态内部类的非静态属性
     */
    public void wai(){
        System.out.println(Clothes.condition);
    }

}