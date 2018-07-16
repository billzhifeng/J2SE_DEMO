package com.jdk.concurrent.queue;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 模拟LinkedBlockingQueue
 * 实现,通过上面的分析，我们可以发现LinkedBlockingQueue在入队列和出队列时使用的不是同一个Lock，
 * 这也意味着它们之间的操作不会存在互斥操作。在多个CPU的情况下，它们可以做到真正的在同一时刻既消费、又生产，能够做到并行处理
 */
public class MyLinkedBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Linked list node 所有的元素都通过Node这个静态内部类来进行存储，这与LinkedList的处理方式完全一样
     */
    static class Node<E> {
        E       item; //使用item来保存元素本身
        Node<E> next; //保存当前节点的后继节点

        Node(E x) {
            item = x;
        }
    }

    /** the capacity bound,or Integer.MAX_VALUE if none */
    /**
     * 阻塞队列所能存储的最大容量 用户可以在创建时手动指定最大容量,如果用户没有指定最大容量 那么最默认的最大容量为Integer.MAX_VALUE.
     */
    private final int           capacity;

    /** current number of elements */
    /**
     * 当前阻塞队列中的元素数量 PS:如果你看过ArrayBlockingQueue的源码,你会发现
     * ArrayBlockingQueue底层保存元素数量使用的是一个 普通的int类型变量。其原因是在ArrayBlockingQueue底层
     * 对于元素的入队列和出队列使用的是同一个lock对象。而数 量的修改都是在处于线程获取锁的情况下进行操作，因此不 会有线程安全问题。
     * 而LinkedBlockingQueue却不是，它的入队列和出队列使用的是两个不同的lock对象,因此无论是在入队列还是出队列，都会涉及对元素数
     * 量的并发修改，(之后通过源码可以更加清楚地看到)因此这里使用了一个原子操作类 来解决对同一个变量进行并发修改的线程安全问题。
     */
    private final AtomicInteger count    = new AtomicInteger();

    /** Head of linked list */
    transient Node<E>           head;

    /** tail of linked list */
    private transient Node<E>   last;

    /**
     * lock held by take 元素出队列时线程所获取的锁 当执行take、poll等操作时线程需要获取的锁
     */
    private final ReentrantLock takeLock = new ReentrantLock();

    /** wait queue for waitting take 当队列为空时，通过该Condition让从队列中获取元素的线程处于等待状态 */
    private final Condition     notEmpty = takeLock.newCondition();

    /** lock held by put */
    private final ReentrantLock putLock  = new ReentrantLock();

    /** Wait queue for put,当队列的元素已经达到capactiy，通过该Condition让元素入队列的线程处于等待状态 */
    private final Condition     notFull  = putLock.newCondition();

    /**
     * Signals a waiting take,called by put/offer)
     */
    private void signalNotEmpty() {

        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
    }

    /** signals a waitting put,called by take/pool */
    private void signalNotFull() {
        final ReentrantLock putLock = this.putLock;
        putLock.lock();
        try {
            notFull.signal();
        } finally {
            putLock.unlock();
        }
    }

    /**
     * linked node at end queue 其实下面的代码等价于如下内容: last.next=node; last = node;
     * 其实也没有什么花样: 就是让新入队列的元素成为原来的last的next，让进入的元素称为last
     */
    private void enqueue(Node<E> node) {
        last = last.next = node;
    }

    /**
     * 让头部元素出队列的过程 其最终的目的是让原来的head被GC回收，让其的next成为head 并且新的head的item为null.
     * 因为LinkedBlockingQueue的头部具有一致性:即元素为null。
     */
    private E dequeue() {

        Node<E> h = head;
        //System.out.println(head.item);
        //        System.out.println(h.item);
        Node<E> first = h.next;
        //        System.out.println(first.item);
        h.next = h; // help GC
        //        System.out.println(h.item);
        head = first;
        //        System.out.println(head.item);
        E x = first.item;
        //        System.out.println(x);
        first.item = null;
        return x;
    }

    /**
     * Locks to prevent both puts and takes.
     */
    void fullyLock() {
        putLock.lock();
        takeLock.lock();
    }

    /**
     * Unlocks to allow both puts and takes.
     */
    void fullyUnlock() {
        takeLock.unlock();
        putLock.unlock();
    }

    public MyLinkedBlockingQueue() {
        this(Integer.MAX_VALUE);
    }

    /**
     * 可以看到,当队列中没有任何元素的时候,此时队列的头部就等于队列的尾部, 指向的是同一个节点,并且元素的内容为null
     */
    public MyLinkedBlockingQueue(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException();
        this.capacity = capacity;
        last = head = new Node<E>(null);
    }

    /*
     * 在初始化LinkedBlockingQueue的时候，还可以直接将一个集合 中的元素全部入队列，此时队列最大容量依然是int的最大值。
     */
    public MyLinkedBlockingQueue(Collection<? extends E> c) {

        this(Integer.MAX_VALUE);
        final ReentrantLock putLock = this.putLock;
        putLock.lock();
        try {
            int n = 0;
            //迭代集合中的每一个元素,让其入队列,并且更新一下当前队列中的元素数量
            for (E e : c) {
                if (e == null)
                    throw new NullPointerException();

                if (n == capacity)
                    throw new IllegalStateException("Queue full");

                enqueue(new Node<E>(e));
                ++n;
            }
            count.set(n);
        } finally {
            putLock.unlock();
        }
    }

    @Override
    public E poll() {
        final AtomicInteger count = this.count;
        if (count.get() == 0)
            return null;
        E x = null;
        int c = -1;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            if (count.get() > 0) {
                x = dequeue();
                c = count.getAndDecrement();
                if (c > 1)
                    notEmpty.signal();
            }
        } finally {
            takeLock.unlock();
        }
        if (c == capacity)
            signalNotFull();
        return x;
    }

    @Override
    public E peek() {
        if (count.get() == 0)
            return null;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            Node<E> first = head.next;
            if (first == null)
                return null;
            else
                return first.item;
        } finally {
            takeLock.unlock();
        }
    }

    /**
     * 在BlockingQueue接口中除了定义put方法外(当队列元素满了之后就会阻塞，
     * 直到队列有新的空间可以方法线程才会继续执行)，还定义一个offer方法，
     * 该方法会返回一个boolean值，当入队列成功返回true,入队列失败返回false。 该方法与put方法基本操作基本一致，只是有细微的差异。
     */
    @Override
    public boolean offer(E e) {
        if (e == null)
            throw new NullPointerException();

        final AtomicInteger count = this.count;

        /*
         * 当队列已经满了，它不会继续等待,而是直接返回。 因此该方法是非阻塞的。
         */
        if (count.get() == capacity)
            return false;
        int c = -1;
        Node<E> node = new Node<E>(e);

        final ReentrantLock putLock = this.putLock;

        putLock.lock();
        try {
            /*
             * 当获取到锁时，需要进行二次的检查,因为可能当队列的大小为capacity-1时，
             * 两个线程同时去抢占锁，而只有一个线程抢占成功，那么此时 当线程将元素入队列后，释放锁，后面的线程抢占锁之后，此时队列
             * 大小已经达到capacity，所以将它无法让元素入队列。 下面的其余操作和put都一样，此处不再详述
             */
            if (count.get() < capacity) {
                enqueue(node);
                c = count.getAndIncrement();
                if (c + 1 < capacity)
                    notFull.signal();
            }
        } finally {
            putLock.unlock();
        }
        if (c == 0)
            signalNotEmpty();
        return c >= 0;
    }

    /**
     * 元素入队列,通过上面的分析，我们应该比较清楚地知道了LinkedBlockingQueue的入队列的操作，<br/>
     * 其主要是通过获取到putLock锁来完成，当队列的数量达到最大值，此时会导致线程处于阻塞状态或者返回false(根据具体的方法来看)；<br/>
     * 如果队列还有剩余的空间，那么此时会新创建出一个Node对象，将其设置到队列的尾部，作为LinkedBlockingQueue的last元素。
     */
    @Override
    public void put(E e) throws InterruptedException {

        if (e == null)
            throw new NullPointerException();
        /*
         * 注意上面这句话,约定所有的put/take操作都会预先设置本地变量, 可以看到下面有一个将putLock赋值给了一个局部变量的操作
         */
        int c = -1;
        Node<E> node = new Node<E>(e);

        /*
         * 在这里首先获取到putLock,以及当前队列的元素数量 即上面所描述的预设置本地变量操作
         */
        final ReentrantLock putLock = this.putLock;
        final AtomicInteger count = this.count;

        /*
         * 执行可中断的锁获取操作,即意味着如果线程由于获取 锁而处于Blocked状态时，线程是可以被中断而不再继
         * 续等待，这也是一种避免死锁的一种方式，不会因为 发现到死锁之后而由于无法中断线程最终只能重启应用。
         */
        putLock.lockInterruptibly();
        try {
            /*
             * 当队列的容量到底最大容量时,此时线程将处于等待状态，直到队列有空闲的位置才继续执行。使用while判
             * 断依旧是为了放置线程被"伪唤醒”而出现的情况,即当 线程被唤醒时而队列的大小依旧等于capacity时，线程应该继续等待。
             */
            while (count.get() == capacity) {
                notFull.await();
            }

            //让元素进行队列的末尾,enqueue代码在上面分析过了
            enqueue(node);

            //首先获取原先队列中的元素个数,然后再对队列中的元素个数+1.
            c = count.getAndIncrement();

            /*
             * 注:c+1得到的结果是新元素入队列之后队列元素的总和。 当前队列中的总元素个数小于最大容量时,此时唤醒其他执行入队列的线程
             * 让它们可以放入元素,如果新加入元素之后,队列的大小等于capacity，
             * 那么就意味着此时队列已经满了,也就没有必须要唤醒其他正在等待入队列的线程,因为唤醒它们之后，它们也还是继续等待。
             */
            if (c + 1 < capacity) {
                notFull.signal();
            }
        } finally {
            putLock.unlock();
        }
        /*
         * 当c=0时，即意味着之前的队列是空队列,出队列的线程都处于等待状态，
         * 现在新添加了一个新的元素,即队列不再为空,因此它会唤醒正在等待获取元素的线程。
         */
        if (c == 0)
            signalNotEmpty();
    }

    /**
     * BlockingQueue还定义了一个限时等待插入操作，即在等待一定的时间内，如果队列有空间可以插入，那么就将元素入队列，然后返回true,如果在过完指定的时间后依旧没有空间可以插入，那么就返回false
     */
    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {

        if (e == null)
            throw new NullPointerException();
        //将指定的时间长度转换为毫秒来进行处理
        long nanos = unit.toNanos(timeout);
        int c = -1;
        Node<E> node = new Node<E>(e);

        final ReentrantLock putLock = this.putLock;
        final AtomicInteger count = this.count;

        putLock.lockInterruptibly();
        try {
            while (count.get() == capacity) {
                //如果等待的剩余时间小于等于0，那么直接返回
                if (nanos <= 0)
                    return false;
                /*
                 * 通过condition来完成等待，此时当前线程会完成锁的，并且处于等待状态 直到被其他线程唤醒该线程、或者当前线程被中断、
                 * 等待的时间截至才会返回，该返回值为***从方法调用到返回所经历的时长***。
                 * 注意：上面的代码是condition的awitNanos()方法的通用写法，
                 * 可以参看Condition.awaitNaos的API文档。 下面的其余操作和put都一样，此处不再详述
                 */
                nanos = notFull.awaitNanos(nanos);
            }
            enqueue(node);
            c = count.incrementAndGet();
            if (c + 1 < capacity) {
                notFull.signal();
            }
        } finally {
            putLock.unlock();
        }
        if (c == 0)
            signalNotEmpty();

        return true;
    }

    @Override
    public E take() throws InterruptedException {
        E x;
        int c = -1;
        final AtomicInteger count = this.count;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        try {
            //当队列为空时，则让当前线程处于等待
            while (count.get() == 0) {
                notEmpty.await();
            }
            //完成元素的出队列
            x = dequeue();
            /*
             * 先获取原来队列的长度,然后队列元素个数完成原子化操作-1,
             * 可以看到count元素会在插入元素的线程和获取元素的线程进行并发修改操作。
             */
            c = count.getAndDecrement();

            /*
             * 当原来队列元素个数大于1时,取出一个元素后,剩余队列元素依旧大于0, 当前线程会唤醒其他执行元素出队列的线程,让它们也
             * 可以执行元素的获取
             */
            if (c > 1)
                notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
        /*
         * 当c==capaitcy时，即在获取当前元素之前， 队列已经满了，而此时获取元素之后，队列就会
         * 空出一个位置，故当前线程会唤醒执行插入操作的线 程通知其他中的一个可以进行插入操作。
         */
        if (c == capacity)
            signalNotFull();
        return x;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        E x = null;
        int c = -1;
        long nanos = unit.toNanos(timeout);
        final AtomicInteger count = this.count;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        try {
            while (count.get() == 0) {
                if (nanos <= 0)
                    return null;
                nanos = notEmpty.awaitNanos(nanos);
            }
            x = dequeue();
            c = count.getAndDecrement();
            if (c > 1)
                notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
        if (c == capacity)
            signalNotFull();
        return x;
    }

    @Override
    public int remainingCapacity() {
        return capacity - count.get();
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        if (c == null)
            throw new NullPointerException();
        if (c == this)
            throw new IllegalArgumentException();
        if (maxElements <= 0)
            return 0;
        boolean signalNotFull = false;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            int n = Math.min(maxElements, count.get());
            // count.get provides visibility to first n Nodes
            Node<E> h = head;
            int i = 0;
            try {
                while (i < n) {
                    Node<E> p = h.next;
                    c.add(p.item);
                    p.item = null;
                    h.next = h;
                    h = p;
                    ++i;
                }
                return n;
            } finally {
                // Restore invariants even if c.add() threw
                if (i > 0) {
                    // assert h.item == null;
                    head = h;
                    signalNotFull = (count.getAndAdd(-i) == capacity);
                }
            }
        } finally {
            takeLock.unlock();
            if (signalNotFull)
                signalNotFull();
        }
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public int size() {
        return count.get();
    }

    void unlink(Node<E> p, Node<E> trail) {
        // assert isFullyLocked();
        // p.next is not changed, to allow iterators that are
        // traversing p to maintain their weak-consistency guarantee.
        p.item = null;
        trail.next = p.next;
        if (last == p)
            last = trail;
        if (count.getAndDecrement() == capacity)
            notFull.signal();
    }

    public boolean remove(Object o) {
        if (o == null)
            return false;
        fullyLock();
        try {
            for (Node<E> trail = head, p = trail.next; p != null; trail = p, p = p.next) {
                if (o.equals(p.item)) {
                    unlink(p, trail);
                    return true;
                }
            }
            return false;
        } finally {
            fullyUnlock();
        }
    }

    public boolean contains(Object o) {
        if (o == null)
            return false;
        fullyLock();
        try {
            for (Node<E> p = head.next; p != null; p = p.next)
                if (o.equals(p.item))
                    return true;
            return false;
        } finally {
            fullyUnlock();
        }
    }

}
