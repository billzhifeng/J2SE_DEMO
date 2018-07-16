package com.jdk.concurrent.semaphore.connectionDemo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

//连接
class Connection {
    private String url;
    private String username;
    private String password;

    public Connection(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection(Connection template) {
        this.url = template.getUrl();
        this.username = template.getUsername();
        this.password = template.getPassword();
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}

//连接池
class ConnectionPool {

    private int        initSize; //初始容量
    private int        maxSize;  //最大容量
    private Semaphore  semaphore;//许可证
    private Connection template; //连接模版

    private class Node {//链表节点
        Connection conn;//当前节点的连接对象
        Node       next;//下一个节点

        public Node(Connection conn) {
            this.conn = conn;
        }
    }

    private Node          head;                      //头结点
    private Node          tail;                      //尾节点
    private ReentrantLock lock = new ReentrantLock();//操作锁

    //默认构造函数
    public ConnectionPool(String url, String username, String password) {
        this(5, 10, url, username, password);
    }

    //构造函数
    public ConnectionPool(int initSize, int maxSize, String url, String username, String password) {
        this.initSize = initSize;
        this.maxSize = maxSize;
        this.template = new Connection(url, username, password);
        initPool();
        semaphore = new Semaphore(maxSize);//许可的连接数为最大连接数
    }

    //初始化连接池
    public void initPool() {
        if (initSize <= 0 || maxSize < initSize)
            throw new IllegalArgumentException();
        Node cur = new Node(new Connection(template));
        tail = head = cur;
        for (int i = 1; i < initSize; ++i) {
            Node newNode = new Node(new Connection(template));
            cur.next = newNode;
            cur = cur.next;
        }
        tail = cur;
        //printConn(head); 打印列表
    }

    //获取连接
    public Connection getConnection() throws InterruptedException {
        semaphore.acquire();//先看是否许可执行，不许可则阻塞等待
        lock.lock();//非原子操作，为保证数据一致性，需加锁
        try {
            if (head == null) {//超过了初始限定的连接数，需扩容
                return new Node(new Connection(template)).conn;
            } else {
                Node ret = head;
                head = head.next;
                if (head == null)//队列中没有元素了
                    tail = head = null;
                return ret.conn;
            }
        } finally {
            lock.unlock();
        }
    }

    //释放连接
    public void closeConnection(Connection conn) {
        lock.lock();
        try {
            Node ret = new Node(conn);
            if (tail == null) {//队列中没有元素
                head = tail = ret;
            } else {
                tail.next = ret;
                tail = tail.next;
            }
        } finally {
            lock.unlock();
        }
        semaphore.release();//结束后释放连接
    }

    void printConn(Node cur) {
        if (cur != null) {
            System.out.println(cur.toString());
            if (cur.next != null) {
                printConn(cur.next);
            }
        }
    }
}
