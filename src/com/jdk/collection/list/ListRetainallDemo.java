package com.jdk.collection.list;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ListRetainallDemo {

    public static void main(String[] args) {

        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");

        List<String> list2 = new ArrayList<String>();
        list2.add("a");
        list2.add("b");
        list2.add("d");

        boolean ret = list.retainAll(list2);

        if (ret) {
            System.out.println("元素被移除成功");//结果
        } else {
            System.out.println("列表中不包含要移除的元素");
        }
        System.out.println("list=" + ToStringBuilder.reflectionToString(list.toArray()));//a\b
        System.out.println("list2=" + ToStringBuilder.reflectionToString(list.toArray()));//a\b

    }

}
