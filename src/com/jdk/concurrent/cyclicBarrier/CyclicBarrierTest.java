package com.jdk.concurrent.cyclicBarrier;

import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 同步辅助类,允许一组线程互相等待,直到达到某一个公共的屏障点(common barrier point).
 * 在涉及一组固定大小的线程中,这些线程必须不时的互相等待,此时cyclicBarrier 很有用.
 * 又因为barrier可以重用,所以称为循环的barrier.(注意countDownLauch只能用一次.)
 */
public class CyclicBarrierTest {

    private static final int NUMBER_OF_GROUP     = 3;    //旅游小组数量

    private static final int THREAD_SLEEP_MILLIS = 1000; //线程sleep时间

    private static boolean   isTourOver          = false;//是否结束

    public static void main(String[] args) throws InterruptedException {

        ExecutorService exe = Executors.newFixedThreadPool(NUMBER_OF_GROUP);

        CyclicBarrier cb = new CyclicBarrier(NUMBER_OF_GROUP, new Runnable() {

            @Override
            public void run() {
                if (isTourOver)
                    System.out.println("所有team都已经集合到大巴上,准备回家.");
            }
        });

        System.out.println("用cyclicbarrier 辅助工具模拟旅游过程中小组结合:");
        tour(exe, cb, 3);
        sleep(THREAD_SLEEP_MILLIS);

        //test1 -> tour(exe, cb, 2);//如果后面没有reset 所有线程都将一直等待.
        //test1 -> sleep(THREAD_SLEEP_MILLIS * 2);
        //cb.reset();//重置,抛异常,所有等待线程不再等待.屏障计数器归0,将屏障重置为其初始状态。
        /**
         * CyclicBarrier
         * 如果在await时因为中断、失败、超时等原因提前离开了屏障点，那么任务组中的其他任务将立即被中断，以InterruptedException异常离开线程。
         * 对于失败的同步尝试，CyclicBarrier 使用了一种要么全部要么全不 (all-or-none)
         * 的破坏模式：如果因为中断、失败或者超时等原因，导致线程过早地离开了屏障点，那么在该屏障点等待的其他所有线程也将通过
         * BrokenBarrierException（如果它们几乎同时被中断，则用 InterruptedException）以反常的方式离开。
         */

        //test2->isTourOver = true;
        //test2-> tour(exe, cb, 0);
        exe.shutdown();
    }

    private static void tour(ExecutorService exe, final CyclicBarrier cb, int groupNum) throws InterruptedException {
        System.out.println("自由游玩...");
        if (0 == groupNum)
            groupNum = NUMBER_OF_GROUP;

        for (int groupNumber = 1; groupNumber <= groupNum; groupNumber++) {
            exe.execute(new TeamGroup(cb, groupNumber));
            //Thread.sleep(1000L);
            //System.out.println("getNumberWaiting=" + cb.getNumberWaiting());//返回当前在屏障处等待的参与者数目。

            //System.out.println("getParties=" + cb.getParties());//返回要求启动此 barrier 的参与者数目。
            //System.out.println("isBroken=" + cb.isBroken());//查询此屏障是否处于损坏状态。
        }
    }

    private static void sleep(long millis) {
        try {
            System.out.println("sleep " + millis / 1000 + "s, 开始:" + new Date().toString());
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            System.out.println("线程sleep异常" + e.getMessage());
        }
    }
}

class TeamGroup implements Runnable {

    private CyclicBarrier cb;
    private int           groupNumber;

    public TeamGroup(CyclicBarrier cb, int groupNum) {
        this.cb = cb;
        this.groupNumber = groupNum;
    }

    @Override
    public void run() {

        try {
            System.out.println(String.format("第%d组完成该景点浏览,回到集合点.", groupNumber));
            cb.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println(String.format("第%d组从集合点回家.", groupNumber));
    }
}
