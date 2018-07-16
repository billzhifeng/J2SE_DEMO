package com.jdk.concurrent.cyclicBarrier.factoryDemo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

//工人
class Wordker extends Thread {

    private CyclicBarrier barrier; //集结点
    private int           workNo;  //工号
    private String        taskName;//任务名称

    public Wordker(CyclicBarrier barrier, int workNo, String taskName) {
        this.barrier = barrier;
        this.workNo = workNo;
        this.taskName = taskName;
    }

    @Override
    public void run() {
        try {
            MonitorMechine.reciveWorker(taskName, workNo);//通知监控平台进度
            int index = barrier.await();//还需等待index个线程才能达到屏障点
            /**
             * 1)、 await()方法将挂起线程，直到同组的其它线程执行完毕才能继续（这点和CountDownLauch相似） 2)、
             * await()方法有返回值，返回对应到达当前线程的索引，索引是从(任务数-1)开始，到0结束（CountDownLauch也是从任务数-1开始，到0结束，但是CountDownLauch的await()方法没有返回值哦）
             */
            //System.out.println("workNo_" + workNo + " " + taskName + " 完成,barrier.avait=" + index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//监控器
class MonitorMechine {

    //接受工人传递的信息  适时发出通知
    public static void reciveWorker(String taskName, int workNo) {
        System.out.println("workNo_" + workNo + " " + taskName + " 任务完成！");
    }

    //监控 当对应批次任务完成后自动通知工人进行下一批任务操作
    public static void waitForNext(CyclicBarrier barrier, int workerCount, String oldTaskName, String newTaskName) {
        try {
            int index = barrier.await();//主线程准备就绪
            //System.out.println("waitForNext index =" + index);
            //            CyclicBarrier 的构造函数允许携带一个任务，这个任务将在0%屏障点执行，它将在await()==0后执行。
            if (index == 0)
                System.out.println("监控平台:" + oldTaskName + "任务完成！");
            notice(workerCount, oldTaskName, newTaskName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

    }

    //监控 当对应批次任务完成后自动通知工人进行下一批任务操作
    public static void notice(int workerCount, String oldTaskName, String newTaskName) {
        if (newTaskName != null)//等于NULL说明所以任务已经完成
        {
            System.out.println("监控平台:开始 下个任务 - " + newTaskName);
        } else
            System.out.println("监控平台:所有任务都已经完成，收工回家");
    }

}
