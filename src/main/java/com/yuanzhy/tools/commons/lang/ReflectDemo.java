package com.yuanzhy.tools.commons.lang;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectDemo {
    private static String sAbc = "111";
    private String abc = "123";
    public void fieldDemo() throws Exception {
        ReflectDemo reflectDemo = new ReflectDemo();
        // 反射获取对象实例属性的值
        // 原生写法
        Field abcField = reflectDemo.getClass().getDeclaredField("abc");
        abcField.setAccessible(true);// 设置访问级别，如果private属性不设置则访问会报错
        String value = (String) abcField.get(reflectDemo);// 123
        // commons写法
        String value2 = (String) FieldUtils.readDeclaredField(reflectDemo, "abc", true);//123
        // 方法名如果不含Declared会向父类上一直查找
    }

    public void fieldRelated() throws Exception {
        ReflectDemo reflectDemo = new ReflectDemo();
        // 反射获取对象属性的值
        String value2 = (String) FieldUtils.readField(reflectDemo, "abc", true);//123
        // 反射获取类静态属性的值
        String value3 = (String) FieldUtils.readStaticField(ReflectDemo.class, "sAbc", true);//111
        // 反射设置对象属性值
        FieldUtils.writeField(reflectDemo, "abc", "newValue", true);
        // 反射设置类静态属性的值
        FieldUtils.writeStaticField(ReflectDemo.class, "sAbc", "newStaticValue", true);
    }

    public void methodDemo() {
        // 获取被Test注解标识的方法

        // 原生写法
        List<Method> annotatedMethods = new ArrayList<Method>();
        for (Method method : ReflectDemo.class.getMethods()) {
            if (method.getAnnotation(Test.class) != null) {
                annotatedMethods.add(method);
            }
        }
        // commons写法
        Method[] methods = MethodUtils.getMethodsWithAnnotation(ReflectDemo.class, Test.class);
    }

    private static void testStaticMethod(String param1) {}
    private void testMethod(String param1) {}

    public void invokeDemo() throws Exception {
        // 调用函数"testMethod"
        ReflectDemo reflectDemo = new ReflectDemo();
        // 原生写法
        Method testMethod = reflectDemo.getClass().getDeclaredMethod("testMethod");
        testMethod.setAccessible(true); // 设置访问级别，如果private函数不设置则调用会报错
        testMethod.invoke(reflectDemo, "testParam");
        // commons写法
        MethodUtils.invokeExactMethod(reflectDemo, "testMethod", "testParam");

        // ---------- 类似​方法 ----------
        // 调用static方法
        MethodUtils.invokeExactStaticMethod(ReflectDemo.class, "testStaticMethod", "testParam");
        // 调用方法(含继承过来的方法)
        MethodUtils.invokeMethod(reflectDemo, "testMethod", "testParam");
        // 调用static方法(当前不存在则向父类寻找匹配的静态方法)
        MethodUtils.invokeStaticMethod(ReflectDemo.class, "testStaticMethod", "testParam");
    }
}
