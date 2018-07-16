package com.jdk.concurrent.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceTest {
    public static void main(String[] args) {

        ExecutorService exe = Executors.newFixedThreadPool(4);
        //exe.execute()
    }

}
