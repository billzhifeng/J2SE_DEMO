package com.jdk.io.count;

/*
 * 2014年3月22日16:57:55
 * 
 * 测试Count和KeywordIdentifier类*/

public class TestCount {
    public static void main(String args[]) {
        CharCount cu = new CharCount("counttest.txt");
        cu.countChar();

        //WordCount c = new WordCount();
        //c.count("counttest.txt");
    }
}
