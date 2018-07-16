package com.jdk.concurrent.cyclicBarrier.factoryDemo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

//任务调度室 CyclicBarrier 测试用例总指挥
public class CyclicBarrierTestCase {

    //初始化任务
    public static List<String> initTaskNames() {
        List<String> taskNames = new ArrayList<String>();
        taskNames.add("组装IPod");
        taskNames.add("组装IPhone5");
        //        taskNames.add("组装IPad4");
        //        taskNames.add("组装IWatch");
        //        taskNames.add("组装HTC ONE");
        //        taskNames.add("组装SamSung GALAXY S4");
        return taskNames;
    }

    //启动函数
    public static void main(String[] args) throws InterruptedException {

        List<String> taskNames = initTaskNames();
        int WorkerCount = 3;//工人数
        int taskCount = WorkerCount * taskNames.size();//任务数
        CyclicBarrier barrier = new CyclicBarrier(WorkerCount + 1);//加一个Boss发令（主线程）
        System.out.println("Boss:开始干活 - " + taskNames.get(0));

        for (int i = 0; i < taskCount; ++i) {
            new Wordker(barrier, i % WorkerCount, taskNames.get(i / WorkerCount)).start();
            Thread.sleep(1000);
            if ((i + 1) % WorkerCount == 0)//一个批次结束后 等待主线程发令 才能继续执行下一批次指令
            {
                if (i + 1 < taskCount)
                    MonitorMechine.waitForNext(barrier, WorkerCount, taskNames.get(i / WorkerCount),
                            taskNames.get((i + 1) / WorkerCount));
                else
                    MonitorMechine.waitForNext(barrier, WorkerCount, taskNames.get(i / WorkerCount), null);
            }
        }
    }

}
