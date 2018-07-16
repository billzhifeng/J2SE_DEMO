package com.jdk.concurrent.semaphore.connectionDemo;

import java.util.Random;

//Semaphore测试用例
/**
 * Semaphore在实际应用中经常被称之为“信号量”、“许可”等，它维护了一个许可集。如有必要，在许可可用前会阻塞每一个
 * acquire()，然后再获取该许可。每个 release()
 * 添加一个许可，从而可能释放一个正在阻塞的获取者。但是，不使用实际的许可对象，Semaphore 只对可用许可的号码进行计数，并采取相应的行动。
 * Semaphore 通常用于限制可以访问某些资源（物理或逻辑的）的线程数目。缓存池、连接池、对象池等经常使用。下面我们来看一个数据库连接池的模拟示例。
 */
/**
 * 模拟了一个连接池的过程，其中使用一个链表来存储有效的可被许可的连接。
 * 获取连接时，首先使用Semaphore获取一个许可（这里的许可数是最大的连接数maxSize），如果没有获取到许可则进行等待，获得许可后从池子中拿取一个连接（这里特别说明的是信号量只是在信号不够的时候挂起线程，但是并不能保证信号量足够的时候获取对象和返还对象是线程安全的，所以还需要一把锁来保证数据的一致性），释放资源的时候先将数据放置到池子中，然后再释放许可。整个过程中Semaphore扮演的只是一个许可的计数器，并不参与许可对象。
 * 当Semaphore的许可资源为一时，Semaphore最多只有一个许可，形似上类似于排它锁（也可以作为开关，可参考CountDownLauch）,这通常也称为二进制信号量，因为它只能有两种状态：一个可用的许可，或零个可用的许可。按此方式使用时，二进制信号量具有某种属性（与很多
 * Lock 实现不同），即可以由线程释放“锁”，而不是由所有者（因为信号量没有所有权的概念）。
 * 另外同公平锁非公平锁一样，信号量也有公平性。如果一个信号量是公平的表示线程在获取信号量时按FIFO的顺序得到许可，也就是按照请求的顺序得到释放。这里特别说明的是：所谓请求的顺序是指在请求信号量而进入FIFO队列的顺序，有可能某个线程先请求信号而后进去请求队列，那么次线程获取信号量的顺序就会晚于其后请求但是先进入请求队列的线程。
 */
public class SemaphoreTestCase {

    public static void main(String[] args) {
        //final ConnectionPool pool = new ConnectionPool(1, 2, "http://localhost:3306/dbtest/", "root", "******");
        final ConnectionPool pool = new ConnectionPool("http://localhost:3306/dbtest/", "root", "******");
        final Random random = new Random();

        for (int i = 0; i < 10; ++i) {//启用10个线程 去获取连接
            final String threadNo = "Thread_" + i;
            new Thread() {
                public void run() {
                    try {
                        System.out.println(threadNo + "： 等待连接...");
                        Connection conn = pool.getConnection();
                        System.out.println(threadNo + "： 获取连接 " + conn);
                        long sleepTime = random.nextInt(1000);
                        Thread.sleep(sleepTime);
                        System.out.println(threadNo + "： 持有连接 " + sleepTime / 1000.0 + "s，持有数据" + conn);
                        pool.closeConnection(conn);
                        System.out.println(threadNo + "： 释放连接 " + conn);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                };

            }.start();
        }
    }
}
