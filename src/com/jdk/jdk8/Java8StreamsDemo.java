package com.jdk.jdk8;

import java.util.Arrays;

public class Java8StreamsDemo {
    public static void main(String[] args) {

        declarative();

        Arrays.stream(new int[] { 1, 2, 3, 4, 5 }).map(n -> 2 * n + 1).average().ifPresent(System.out::println);
        // output: 7.0

        Arrays.stream(new int[] { 1, 2, 3, 4, 5 }).map(n -> 2 * n + 1).forEach(e -> System.out.println(e));
    }

    public static void declarative() {
        int[] iArr = { 1, 3, 4, 5, 6, 9, 8, 7, 4, 2 };
        Arrays.stream(iArr).forEach(System.out::println);

    }
}
