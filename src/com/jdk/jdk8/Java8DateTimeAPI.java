package com.jdk.jdk8;

import java.time.LocalDate;
import java.time.Month;

public class Java8DateTimeAPI {
    public static void main(String[] args) {
        LocalDate currentDate = LocalDate.now();
        System.out.println(currentDate);
        // output: 2015-11-24
        LocalDate twentyDecember2015 = LocalDate.of(2015, Month.DECEMBER, 20);
        System.out.println(twentyDecember2015);
        // output: 2015-12-20
        LocalDate firstDec2015 = LocalDate.of(2015, 12, 1);
        System.out.println(firstDec2015);
        // output: 2015-12-01
    }
}
