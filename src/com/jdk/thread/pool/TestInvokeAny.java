package com.jdk.thread.pool;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestInvokeAny {

    public static void main(String[] args) throws Exception {

        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        //如果3个任务都是抛出异常的,此时线程池会抛出异常

        /*
         * 执行给定的任务集合，返回任务中成功执行完成的一个任务的返回值。 比如有3个任务，第一个任务由于异常而结束，此时不会返回，
         * 而是等待其他两个任务中的某一个成功执行完成才返回。(参考TestInvokeAny) 如果所有的任务都由于异常而结束，此时会抛出异常。
         * 无论是成功返回还是由于异常而返回，都不会取消正在执行的任务
         */
        Integer value = threadPool.invokeAny(Arrays.asList(new Task1(), new Task1(), new Task2()));
        System.out.println(value);
        threadPool.shutdown();
    }

    static class Task1 implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " execute!");
            return (int) (Math.random() * 1000);
        }
    }

    static class Task2 implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            // 抛出的异常会被线程池隐藏
            throw new RuntimeException("执行出错");
        }
    }
}
