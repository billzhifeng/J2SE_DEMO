package com.jdk.thread.pool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 固定延迟时间地周期性调度
 */
public class TestScheduledWithFixedDelay {

    public static void main(String[] args) {

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);

        SimpleDateFormat fat = new SimpleDateFormat("HH:mm:ss");
        System.out.println("start schedule: " + fat.format(new Date()));
        /*
         * scheduleWithFixedDelay执行说明: 第一个任务的执行是在给定的延迟时间后执行，这里的延迟时间为0秒
         * 后续的任务是在上一个任务执行完成后,经过delay后继续执行(这里的delay为1秒)
         */
        /*
         * 创建任务并且以固定的延迟时间周期性地执行。 初始任务的开始执行是在给定的initialDelay之后；
         * 而后续的任务都将在上一个任务执行完毕后， 再经过delay，然后开始执行。
         * 对于该方法，如果在调度的过程中发生异常，那么也将导致后续任务的终结.<br> 如果调用get方法,会报出异常.
         * ******后一个任务一定要等待前一个执行完,才会执行.
         */
        // public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,long initialDelay,long delay,TimeUnit unit);
        ScheduledFuture<?> future = scheduledThreadPool.scheduleWithFixedDelay(new Task3(), 0, 1, TimeUnit.SECONDS);
        //try {
        // System.out.println("get=" + future.get());
        System.out.println("isDone=" + future.isDone());
        // } catch (InterruptedException | ExecutionException e) {
        //  e.printStackTrace();
        //}
    }

    static class Task3 implements Runnable {
        SimpleDateFormat fat = new SimpleDateFormat("HH:mm:ss");

        @Override
        public void run() {
            try {
                int tem = (int) (10 * Math.random());
                System.out.println(fat.format(new Date()) + "," + Thread.currentThread().getName() + " doSomething!");
                System.out.println("tem=" + tem);
                Thread.sleep(tem * 1000);
                if (5 == tem || 7 == tem) {
                    throw new RuntimeException();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
