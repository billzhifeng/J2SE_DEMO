package com.jdk.io.count;

import java.io.FileReader;

public class CharCount {

    public String filename;

    public CharCount(String str) {
        filename = str;
    }

    //方法，统计字符个数
    public void countChar() {
        int a;
        char ch, c;
        int[] num_upper = new int[26]; //存放26个大写字母的个数
        int[] num_lower = new int[26]; //存放26个小写字母的个数，java中int初始化自动为0，不用手动设置

        try {
            FileReader in = new FileReader(filename); //字符流，输入

            while ((a = in.read()) != -1) {
                ch = (char) a;
                for (int i = 0; i < 26; i++) {
                    //小写字母
                    c = (char) ('a' + i);
                    if (ch == c) {
                        num_lower[i]++; //判断是哪个字母，并给对应字母的个数加1
                        break;
                    }

                    //大写字母
                    c = (char) ('A' + i);
                    if (ch == c) {
                        num_upper[i]++;
                        break;
                    }
                }
            }
            
            in.close();

            //打印输出
            System.out.println("对各字符进行统计,各个字符数量分别为:");
            for (int i = 0; i < 26; i++) {
                c = (char) ('a' + i);
                System.out.print((i + 1) % 5 == 0 ? c + ":" + num_lower[i] + "\n" : c + ":" + num_lower[i] + "\t"); //每行放5个
            }
            System.out.println();

            for (int i = 0; i < 26; i++) {
                c = (char) ('A' + i);
                System.out.print((i + 1) % 5 == 0 ? c + ":" + num_upper[i] + "\n" : c + ":" + num_upper[i] + "\t");
            }
            System.out.println();
        } catch (Exception e) {
        }

    }

}
