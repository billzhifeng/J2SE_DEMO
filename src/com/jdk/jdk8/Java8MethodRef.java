package com.jdk.jdk8;

import java.util.Arrays;

public class Java8MethodRef {
    public static void main(String[] args) {
        Arrays.asList("a", "b", "c").forEach(new Java8MethodRef()::show);
        Arrays.asList("a", "b", "c").forEach(e -> System.out.println(e));
    }

    public void show(String str) {
        System.out.print(str + " ");
    }
    // output: a b c
}
