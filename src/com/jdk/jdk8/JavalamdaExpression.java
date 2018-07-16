package com.jdk.jdk8;

import java.util.Arrays;

public class JavalamdaExpression {
    public static void main(String[] args) {
        //Lambda表达式
        Arrays.asList("j", "a", "v", "a", "8").forEach(e -> System.out.print(e));
    }
}
