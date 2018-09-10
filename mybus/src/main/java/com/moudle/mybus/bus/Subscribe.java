package com.moudle.mybus.bus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2018/8/8.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)   //运行时有效，就是一直有效，会被加载进入java虚拟机；
public @interface Subscribe {

    String[] value();
}
