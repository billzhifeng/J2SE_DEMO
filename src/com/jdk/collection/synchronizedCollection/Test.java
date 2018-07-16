package com.jdk.collection.synchronizedCollection;

import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

public class Test {

    public static void main(String[] args) {

        Vector<String> vector = new Vector<String>();

        // populate the vector
        vector.add("1");
        vector.add("2");
        vector.add("7");
        vector.add("4");
        vector.add("7");

        // create a synchronized view
        Collection<String> c = Collections.synchronizedCollection(vector);

        System.out.println("Sunchronized view is :" + c);
    }
}
