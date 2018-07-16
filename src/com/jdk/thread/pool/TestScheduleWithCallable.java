package com.jdk.thread.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TestScheduleWithCallable {

    public static void main(String[] args) {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);

        /*
         * 在给定的延迟时候后执行一个带返回值的任务，该任务同样也只会被执行<<<一次>>>。仅仅一次.
         * ScheduleFuture的get方法返回的结果是将是Callable执行返回的结果。
         */
        System.out.println("Start schedule: " + System.currentTimeMillis() / 1000);
        ScheduledFuture<Integer> future = scheduledThreadPool.schedule(new TaskCallable(), 1L, TimeUnit.SECONDS);

        try {
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(future.isDone());
        //scheduledThreadPool.shutdownNow();

        /*
         * 在给定的延迟时候后执行一个不带返回值的任务，该任务只会被执行一次。
         * 如果传入的时间小于等于0(参考下面的说明1，分析了JDK的线程池对传入的延迟时间的触发)，那么任务会立即触发执行。
         * 由于传入的是Runnable接口，因此ScheduleFuture的get方法返回的结果是null。
         */
        // public ScheduledFuture<?> schedule(Runnable command,long delay, TimeUnit unit);

    }

    static class TaskCallable implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " execute!");
            return (int) (Math.random() * 1000);
        }
    }

}
