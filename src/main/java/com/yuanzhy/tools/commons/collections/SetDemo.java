package com.yuanzhy.tools.commons.collections;

import org.apache.commons.collections4.set.ListOrderedSet;
import org.junit.Test;

import java.util.Set;

import static com.yuanzhy.tools.commons.Util.println;

public class SetDemo {
    @Test
    public void set() {
        // 有序的set，按照插入顺序排序
        Set<String> set = new ListOrderedSet<>();
        set.add("aa");
        set.add("11");
        set.add("哈哈");
        println(set); // [aa,11,哈哈]
    }

    public void t() {

    }
}
