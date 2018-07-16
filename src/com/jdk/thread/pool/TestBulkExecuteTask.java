package com.jdk.thread.pool;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 提交批量任务到线程池执行
 */
public class TestBulkExecuteTask {
    public static void main(String[] args) throws Exception {

        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        /*
         * case 0: 批量执行一组任务集合，当所有的任务全部执行完毕之后才返回。
         * <br>返回的结果为一个存放Future的List，其中每一个元素都保存 了其对应任务的执行结果和状态。
         * <br>Future的isDone()方法都是返回true<br> 特别注意：任务的结束可能有两种情况，一是正常执行完毕，或者
         * 由于在执行的过程中抛出了异常而结束。 如果抛出异常，只有在返回的Future调用get方法是才会抛出异常 否则，线程池会将异常隐藏。
         * (执行下面的TestBulkExecuteTask,默认情况下程序会正常退出)
         */
        //List<Future<Integer>> futures = threadPool
        //      .invokeAll(Arrays.asList(new Task1(), new Task2(), new Task1(), new Task2(), new Task1()));

        /*
         * case 1: 批量执行一组任务，并且显示等待任务的结束。 当所有任务执行完毕或者超时返回。
         * Future的isDone()方法都是返回true。 <br>如果一旦超时返回，那么未执行完的任务都会被取消。
         * 如果任务因为取消而结束,在调用Future的get方法时会引发CancellationException。
         */
        List<Future<Integer>> futures = threadPool.invokeAll(Arrays.asList(new Task0(), new Task1(), new Task1(),
                new Task1(), new Task1(), new Task1(), new Task1()), 1L, TimeUnit.SECONDS);

        try {
            //批量执行Callable任务，当任务由于异常而结束,线程池并不会将异常抛出 只有当通过Future获取任务的返回结果时才会真正抛出异常
            for (Future<Integer> future : futures) {
                System.out.println(future.get());
                System.out.println(future.isDone());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        threadPool.shutdown();
    }

    static class Task0 implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println(Thread.currentThread().getName() + " execute!");
            return (int) (Math.random() * 1000);
        }
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
            //抛出的异常会被线程池隐藏
            throw new RuntimeException("执行出错");
        }
    }
}
