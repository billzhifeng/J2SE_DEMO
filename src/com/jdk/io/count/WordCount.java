package com.jdk.io.count;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 统计文本中单词数量
 */
public class WordCount {

    public void count(String filePath) {
        File file = new File(filePath);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;//读出文本的某一行
            Map<String, Long> map = new HashMap<String, Long>();//单词为key,次数为value
            StringBuffer word = new StringBuffer();//某个单词
            Long wordSum = 0L;//单词总数
            boolean isWord = false;//是否是单词

            while ((line = reader.readLine()) != null) {
                int lineLength = line.length();//每一行长度

                for (int i = 0; i < lineLength; i++) {

                    char c = line.charAt(i);//每个字符
                    if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {//如果是a-z||A-Z就是单词
                        word.append(c);//单词拼接每个字符
                        isWord = true;//是单词

                        //行最后一列如果是单个字符,也是一个单词
                        if (i == (lineLength - 1)) {
                            isWord = false;
                            String key = word.toString();
                            map.put(key, null == map.get(key) ? 1L : (map.get(key) + 1));
                            wordSum++;
                            word.delete(0, word.length());
                        }

                    } else if (isWord) {
                        isWord = false;
                        String key = word.toString();
                        map.put(key, null == map.get(key) ? 1L : (map.get(key) + 1));
                        wordSum++;
                        word.delete(0, word.length());
                    }

                }

            }

            reader.close();
            System.out.println("\n对单词进行统计,单词总数为:" + wordSum + "个. ");

            map = (HashMap<String, Long>) sort(map);

            for (Entry<String, Long> entry : map.entrySet()) {
                System.out.println(String.valueOf(entry.getKey() + " 出现 " + entry.getValue() + "次\n"));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map sort(Map<String, Long> map) {

        List<Entry<String, Long>> list = new LinkedList(map.entrySet());

        Collections.sort(list, new Comparator<Entry<String, Long>>() {
            public int compare(Entry<String, Long> o1, Entry<String, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Map<String, Long> result = new LinkedHashMap<String, Long>();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            result.put((String) entry.getKey(), (Long) entry.getValue());
        }

        return result;
    }

}
