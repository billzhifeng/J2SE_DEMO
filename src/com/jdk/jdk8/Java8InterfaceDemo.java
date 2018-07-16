package com.jdk.jdk8;

/**
 * @author wangzhifeng
 */
public interface Java8InterfaceDemo {
    abstract void add();

    /**
     * 在Java 8中，可以通过使用default关键字来添加默认的方法实现。
     * 接口的实现类可以直接使用这些默认的方法，同时还可以重写默认的方法，这不是强制性的重写。
     */
    default void display() {
        System.out.println("default method of interface");
    }

    /**
     * Java 8接口引入的第二种方法是静态方法。 这一点与类中的静态方法相似，可以在接口中使用static关键字定义静态方法。
     * 如果我们要调用接口定义的静态方法，只需使用接口名就可以访问这些静态方法。比如：
     */
    public static void show() {
        System.out.println("static method of interface");
    }
}
