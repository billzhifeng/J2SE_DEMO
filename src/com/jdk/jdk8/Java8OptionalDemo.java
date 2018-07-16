package com.jdk.jdk8;

import java.util.Optional;

public class Java8OptionalDemo {
    public static void main(String[] args) {
        Optional<String> str = Optional.ofNullable("a");
        System.out.println("str having value ? " + str.isPresent());
        // output : str having value ? false
    }
}
