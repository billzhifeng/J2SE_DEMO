package com.jdk.collection.map.concurrentHashMap;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class MapTest2<K, V> {

    public static void main(String args[]) {
        /**
         * TreeMap实现SortMap接口，能够把它保存的记录根据键排序,默认是按键值的升序排序.
         * 也可以指定排序的比较器，当用Iterator遍历TreeMap时，得到的记录是排过序的。
         * TreeMap取出来的是排序后的键值对。但如果您要按自然顺序或自定义顺序遍历键，那么TreeMap会更好。
         */
        Map<String, String> teMap = new TreeMap();
        teMap.put("version", "1.0");
        teMap.put("timestamp", "1414488825");
        teMap.put("mch_id", "1223816102");
        teMap.put("contract_display_account", "name1");
        teMap.put("plan_id", "106");
        teMap.put("contract_code", "122");
        teMap.put("request_serial", "123");
        teMap.put("notify_url", "www.qq.com%2Ftest%2Fpapay");
        teMap.put("appid", "wx426a3015555a46be");

        for (Map.Entry<String, String> entry : teMap.entrySet()) {
            System.out.print(entry.getKey() + "=" + entry.getValue() + "&");
        }
        //打印顺序 1,2,a,b

        Map<String, String> tree = new TreeMap<String, String>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });

        //        tree.putAll(teMap);
        //        for (Map.Entry<String, String> entry : tree.entrySet()) {
        //            System.out.println(entry.getKey() + "_" + entry.getValue());
        //        }
        //b,a,2,1
    }

}
