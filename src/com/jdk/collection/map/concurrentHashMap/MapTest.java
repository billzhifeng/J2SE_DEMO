package com.jdk.collection.map.concurrentHashMap;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class MapTest<K, V> {

    public void mapSortTest() {
        /**
         * TreeMap实现SortMap接口，能够把它保存的记录根据键排序,默认是按键值的升序排序.
         * 也可以指定排序的比较器，当用Iterator遍历TreeMap时，得到的记录是排过序的。
         * TreeMap取出来的是排序后的键值对。但如果您要按自然顺序或自定义顺序遍历键，那么TreeMap会更好。
         */
        Map<String, String> teMap = new TreeMap();
        teMap.put("2", "Value2");
        teMap.put("1", "Value1");
        teMap.put("b", "ValueB");
        teMap.put("a", "ValueA");

        for (Map.Entry<String, String> entry : teMap.entrySet()) {
            System.out.println(entry.getKey() + "_" + entry.getValue());
        }
        //打印顺序 1,2,a,b

        Map<String, String> tree = new TreeMap<String, String>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });

        tree.putAll(teMap);
        for (Map.Entry<String, String> entry : tree.entrySet()) {
            System.out.println(entry.getKey() + "_" + entry.getValue());
        }
        //b,a,2,1

        /**
         * LinkedHashMap保存了记录的插入顺序，在用Iterator遍历LinkedHashMap时，先得到的记录肯定是先插入的.
         * 也可以在构造时用带参数，按照应用次数排序。在遍历的时候会比HashMap慢。 不过有种情况例外，当HashMap容量很大，实际数据较少时。
         * 遍历起来可能会比LinkedHashMap慢，因为LinkedHashMap的遍历速度只和实际数据有关，和容量无关，
         * 而HashMap的遍历速度和他的容量有关。
         */
        Map<String, String> lhsMap = new LinkedHashMap<String, String>();
        lhsMap.put("3", "Value3");
        lhsMap.put("1", "Value1");
        lhsMap.put("2", "Value2");
        lhsMap.put("b", "ValueB");
        lhsMap.put("a", "ValueA");

        for (Map.Entry<String, String> entry : lhsMap.entrySet()) {
            System.out.println(entry.getKey() + "_" + entry.getValue());
        }
        //3,1,2,b,a
    }

    public void testConcurrentHashMap() {
        /**
         * 效率低下的HashTable容器
         * HashTable容器使用synchronized来保证线程安全，但在线程竞争激烈的情况下HashTable的效率非常低下。
         * 因为当一个线程访问HashTable的同步方法时，其他线程访问HashTable的同步方法时，可能会进入阻塞或轮询状态。
         * 如线程1使用put进行添加元素，线程2不但不能使用put方法添加元素，并且也不能使用get方法来获取元素，所以竞争越激烈效率越低。
         */
        /**
         * 锁分段技术
         * HashTable容器在竞争激烈的并发环境下表现出效率低下的原因是所有访问HashTable的线程都必须竞争同一把锁，那假如容器里有多把锁
         * ，每一把锁用于锁容器其中一部分数据，那么当多线程访问容器里不同数据段的数据时，线程间就不会存在锁竞争，从而可以有效的提高并发访问效率，
         * 这就是ConcurrentHashMap所使用的锁分段技术，首先将数据分成一段一段的存储，然后给每一段数据配一把锁，
         * 当一个线程占用锁访问其中一个段数据的时候，其他段的数据也能被其他线程访问。
         */
        /**
         * java5中新增了ConcurrentMap接口和它的一个实现类ConcurrentHashMap。
         * ConcurrentHashMap提供了和Hashtable以及SynchronizedMap中所不同的锁机制。
         * Hashtable中采用的锁机制是一次锁住整个hash表，从而同一时刻只能由一个线程对其进行操作；
         * 而ConcurrentHashMap中则是一次锁住一个桶。ConcurrentHashMap默认将hash表分为16个桶，诸如get,
         * put,remove等常用操作只锁当前需要用到的桶。这样，原来只能一个线程进入，现在却能同时有16个写线程执行，
         * 并发性能的提升是显而易见的。
         */
        /**
         * 上面说到的16个线程指的是写线程，而读操作大部分时候都不需要用到锁。只有在size等操作时才需要锁住整个hash表。
         * 在迭代方面，ConcurrentHashMap使用了一种不同的迭代方式。在这种迭代方式中，
         * 当iterator被创建后集合再发生改变就不再是抛出ConcurrentModificationException，
         * 取而代之的是在改变时new新的数据从而不影响原有的数据，iterator完成后再将头指针替换为新的数据，
         * 这样iterator线程可以使用原来老的数据，而写线程也可以并发的完成改变。
         */
        /**
         * 首先哈希桶的个数是固定的，有用户构建的时候输入，一旦构建，个数就已经固定；查找的时候首先将key值通过哈希函数获取哈希值，
         * 根据哈希值获取到对应的哈希桶，然后遍历哈希桶内的pairs数组获取。
         */
        Map map = new ConcurrentHashMap();
    }

    public void testSynchronizedMap() {

        //Hashtable是线程安全的，它的方法是同步了的，可以直接用在多线程环境中。
        //而HashMap则不是线程安全的。在多线程环境中，需要手动实现同步机制。
        //在Collections类中提供了一个方法返回一个同步版本的HashMap用于多线程的环境
        Map<K, V> synMap = java.util.Collections.synchronizedMap(new HashMap<K, V>());
        //SynchronizedMap类是定义在Collections中的一个静态内部类。
        //它实现了Map接口，并对其中的每一个方法实现，通过synchronized关键字进行了同步控制,性能低下

        // public V get(Object key) 
        //    synchronized (mutex) {return m.get(key);}

        //public V put(K key, V value) 
        //   synchronized (mutex) {return m.put(key, value);}

        // public V remove(Object key) 
        //    synchronized (mutex) {return m.remove(key);}

        //潜在的线程安全问题,上面提到Collections为HashMap提供了一个并发版本SynchronizedMap。这个版本中的方法都进行了同步，但是这并不等于这个类就一定是线程安全的。在某些时候会出现一些意想不到的结果。
        //如下面这段代码：Java代码
        // shm是SynchronizedMap的一个实例  
        //if(shm.containsKey('key')){  
        //shm.remove(key);  
        //} 
        /**
         * 这段代码用于从map中删除一个元素之前判断是否存在这个元素。
         * 这里的containsKey和reomve方法都是同步的，但是整段代码却不是。
         * 考虑这么一个使用场景：线程A执行了containsKey方法返回true，准备执行remove操作；
         * 这时另一个线程B开始执行，同样执行了containsKey方法返回true，并接着执行了remove操作；
         * 然后线程A接着执行remove操作时发现此时已经没有这个元素了。
         * 要保证这段代码按我们的意愿工作，一个办法就是对这段代码进行同步控制，但是这么做付出的代价太大。 <br>
         */
        /**
         * 在这里，有一个地方需要注意的是：得到的keySet和迭代器都是Map中元素的一个“视图”，而不是“副本”。问题也就出现在这里，
         * 当一个线程正在迭代Map中的元素时，另一个线程可能正在修改其中的元素。此时，
         * 在迭代元素时就可能会抛出ConcurrentModificationException异常。为了解决这个问题通常有两种方法，
         * 一是直接返回元素的副本，而不是视图。这个可以通过 集合类的
         * toArray()方法实现，但是创建副本的方式效率比之前有所降低，特别是在元素很多的情况下；另一种方法就是在迭代的时候锁住整个集合，
         * 这样的话效率就更低了
         */
    }

    public void testHashMap() {
        Map hash = new HashMap();
        try {
            System.out.println(hash.get("a"));//不抛异常
            hash.put(null, null);//不抛异常
            //在HashMap中，null可以作为键,这样的键只有一个,可以有一个或多个键所对应的值为null。
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
