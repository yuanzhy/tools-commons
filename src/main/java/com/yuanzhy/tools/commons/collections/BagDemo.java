package com.yuanzhy.tools.commons.collections;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.junit.Test;

import static com.yuanzhy.tools.commons.Util.println;

public class BagDemo {
    @Test
    public void bag() {
        // bag 带计数功能的集合
        Bag<String> bag = new HashBag<>();
        bag.add("a");
        bag.add("b");
        bag.add("a");
        println(bag.size()); // 3
        println(bag.getCount("a")); // 2
    }
}
