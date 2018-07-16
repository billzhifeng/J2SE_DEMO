package com.jdk.thread.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestScheduleAtFixedRate {

    public static void main(String[] args) {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);

        System.out.println("Start schedule: " + System.currentTimeMillis() / 1000);

        /*
         * 创建任务并且以固定频率周期性地执行。 初始任务的开始执行是在给定的initialDelay之后；
         * 而后续第1个任务的开始于initialDelay+2*period，
         * 第2个任务开始于initialDelay+2*period，以此类推...
         * 如果一个任务的执行时间大于给定的执行周期，那么会导致其后面的一个任务执行时间被延后。如果发生这种情况，
         * 后面的任务会在当前任务执行完毕后立即执行，JDK的线程池不会这样延迟而新创建一个线程让后面的任务与当前任务并发执行。
         * 参考(TestScheduleAtFixedRate和TestscheduleAtFixedRate2)
         * 如果任务在执行中的由于异常而停止，那么后续的任务也都不会再执行
         * (***因此对于调度的任务，我们在编写程序时必须确保异常能够得到正确处理，从而避免因此异常而导致整个调度任务的终结****)，
         * 否则如果想要停止定时任务的话，只能通过ScheduledFuture来取消任务或者关闭线程池。
         */
        //public ScheduledFuture<?> scheduleAtFixedRate( command,long initialDelay,long period,TimeUnit unit);

        //case 0: 初始任务执行延迟2秒,每隔4秒执行下一个任务,每个任务在<4秒内完成,所以每4秒会执行下一个任务
        scheduledThreadPool.scheduleAtFixedRate(new Task(), 2, 4, TimeUnit.SECONDS);

        //case 1: 任务的调度周期为4秒，但是每个任务的执行却是6秒,此时每个任务的执行时间都会被延后 其执行的时间与上一个任务之间相差为6秒
        //scheduledThreadPool.scheduleAtFixedRate(new Task2(), 2, 4, TimeUnit.SECONDS);
        System.out.println(scheduledThreadPool.isShutdown());

        scheduledThreadPool.shutdownNow();

    }

    static class Task implements Runnable {
        @Override
        public void run() {
            try {
                int tem = (int) (10 * Math.random());
                System.out.println("currentTime: " + System.currentTimeMillis() / 1000 + ","
                        + Thread.currentThread().getName() + " doSomething!");

                if (tem == 5 || tem == 7) {
                    System.out.println("Task Over,tem=" + tem + " throw RuntimeException!");
                    throw new RuntimeException();
                }
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Task2 implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println("currentTime: " + System.currentTimeMillis() / 1000 + ","
                        + Thread.currentThread().getName() + " doSomething!");
                //任务执行时间为6秒
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
