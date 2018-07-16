package com.jdk.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionsTest {

    //1) 排序(Sort) 
    //    使用sort方法可以根据元素的自然顺序 对指定列表按升序进行排序。
    //    列表中的所有元素都必须实现 Comparable 接口。此列表内的所有元素都必须是使用指定比较器可相互比较的
    private static void sortTest() {
        List list = new ArrayList();
        double array[] = { 112, 111, 23, 456, 231 };
        for (int i = 0; i < array.length; i++) {
            list.add(new Double(array[i]));
        }
        Collections.sort(list);
        for (int i = 0; i < array.length; i++) {
            //System.out.println(list.get(i));
        }

        //----
        //22) 混排（Shuffling） 
        //混排算法所做的正好与 sort 相反: 它打乱在一个 List 中可能有的任何排列的踪迹。
        //也就是说，基于随机源的输入重排该 List, 这样的排列具有相同的可能性（假设随机源是公正的）。
        //这个算法在实现一个碰运气的游戏中是非常有用的。例如，它可被用来混排代表一副牌的 Card 对象的一个 List 。另外，在生成测试案例时，它也是十分有用的。 
        list.clear();
        double array2[] = { 112, 111, 23, 456, 231 };
        for (int i = 0; i < array2.length; i++) {
            list.add(new Double(array2[i]));
        }
        Collections.shuffle(list);
        for (int i = 0; i < array2.length; i++) {
            //System.out.println(list.get(i));
        }

        //-----使用Reverse方法可以根据元素的自然顺序 对指定列表按降序进行排 
        list.clear();
        double array3[] = { 112, 111, 23, 456, 231 };
        for (int i = 0; i < array.length; i++) {
            list.add(new Double(array3[i]));
        }
        Collections.reverse(list);
        for (int i = 0; i < array3.length; i++) {
            System.out.println(list.get(i));
        }
    }

    public static void main(String[] args) {
        sortTest();
    }

}
