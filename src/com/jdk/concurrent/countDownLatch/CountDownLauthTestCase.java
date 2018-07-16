package com.jdk.concurrent.countDownLatch;

import java.util.concurrent.CountDownLatch;

//TODO http://yhjhappy234.blog.163.com/blog/static/3163283220135875759265/
/**
 * void await() 使当前线程在锁存器倒计数至零之前一直等待，除非线程被中断。 <br>
 * boolean await(long timeout, TimeUnit unit)<br>
 * 使当前线程在锁存器倒计数至零之前一直等待，除非线程被中断或超出了指定的等待时间。 <br>
 * void countDown()<br>
 * 递减锁存器的计数，如果计数到达零，则释放所有等待的线程。<br>
 * long getCount() 返回当前计数。 <br>
 * String toString() 返回标识此锁存器及其状态的字符串。<br/>
 * <br/>
 * CountDownLatch维护了一个正数计数器，countDown方法对计数器做减操作，await方法等待计数器达到0。所有await的线程都会阻塞直到计数器为0或者等待线程中断或者超时。
 * <br/>
 * 这个示例里面使用了两个CountDownLauch，分别构建了两种场景，第一个startLauch相当于开关，在开启之前，没有任何一个线程执行，当开启之后，所有线程同时可以执行。第二个workerLauch其实就是一个计数器，当计数器没有减到零的时候，主线程一直等待，当所有线程执行完毕后，主线程取消阻塞继续执行！
 */
class Worker extends Thread {

    private int            workNo;    //工号
    private CountDownLatch startLauch;//启动器-闭锁
    private CountDownLatch workLauch; //工作进程-计数器

    public Worker(int workNo, CountDownLatch startLauch, CountDownLatch workLauch) {
        this.workNo = workNo;
        this.startLauch = startLauch;
        this.workLauch = workLauch;
    }

    @Override
    public void run() {
        try {
            System.out.println(" - Thread_" + workNo + " 准备就绪！准备开工！");
            startLauch.await();//等待老板发指令，使当前线程在锁存器倒计数至零之前一直等待，除非线程被中断
            System.out.println(" - Thread_" + workNo + " 正在干活...");
            Thread.sleep(100);//每人花100ms干活
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(" - Thread_" + workNo + " 工作完成!");
            workLauch.countDown();
        }

    }
}

//测试用例
public class CountDownLauthTestCase {

    public static void main(String[] args) throws InterruptedException {
        int workerCount = 3;//工人数目
        CountDownLatch startLauch = new CountDownLatch(1);//闭锁 相当于开关
        CountDownLatch workLauch = new CountDownLatch(workerCount);//计数器
        System.out.println(" - Boss：集合准备开工了！");
        for (int i = 0; i < workerCount; ++i) {
            new Worker(i, startLauch, workLauch).start();
        }
        Thread.sleep(1000);
        System.out.println(" - Boss：休息1s后开工!");
        Thread.sleep(1000);
        System.out.println(" - Boss：开工!");
        startLauch.countDown();//打开开关,递减锁存器的计数，如果计数到达零，则释放所有等待的线程
        workLauch.await();//任务完成后通知Boss
        System.out.println(" - Boss：不错!任务都完成了！收工回家！");
    }
}
