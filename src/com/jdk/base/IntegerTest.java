package com.jdk.base;

public class IntegerTest {
    public static void main(String[] args) {
        int i = 0;

        i = i++;

        System.out.println(i);//打印结果 0 

        //原因:因为++在后面，所以先使用i，“使用”的含义就是i++这个表达式的值是0，但是并没有做赋值操作，
        //它在整个语句的最后才做赋值，也就是说在做了++操作后再赋值的，所以最终结果还是0
        /**
         * 让我们看的更清晰点： 1 2 int i = 0;//这个没什么说的 i = i++;//等效于下面的语句： 1 2 3 int temp
         * = i;//这个temp就是i++这个表达式的值 i++; //i自增 i = temp;//最终，将表达式的值赋值给i
         */
    }
}
