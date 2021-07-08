package com.yuanzhy.tools.commons.collections;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CollectionUtilsDemo {
    @Test
    public void collectionUtils() {
        String str = null;
        List list1 = Arrays.asList(new String[]{"1", "2", "3"});
        List list2 = Arrays.asList(new String[]{"1", "2", "4"});
        // 判断是否为空（null或空list都为true）
        CollectionUtils.isEmpty(list1);
        // 添加元素（忽略null元素）
        CollectionUtils.addIgnoreNull(list1, str);
        // list是否包含subList中的所有元素
        CollectionUtils.containsAll(list1, list2); // false
        // list是否包含subList中的任意一个元素
        CollectionUtils.containsAny(list1, list2); // true
        // list1 减去 list2
        CollectionUtils.subtract(list1, list2); // ["3"]
        // 合并两个list并去重
        CollectionUtils.union(list1, list2); //["1", "2", "3", "4"]
        // 取两个list同时存在的元素
        CollectionUtils.intersection(list1, list2); // [1", "2"]
    }
    @Test
    public void listUtils() {
        List list1 = Arrays.asList(new String[]{"1", "2", "3"});
        List list2 = Arrays.asList(new String[]{"1", "2", "4"});
        // 同CollectionUtils, 返回结果为List
        ListUtils.subtract(list1, list2); // ["3"]
        ListUtils.union(list1, list2); //["1", "2", "3", "4"]
        ListUtils.intersection(list1, list2); // [1", "2"]
        // 判断两个集合中的内容是否完全相同（顺序也一致）
        ListUtils.isEqualList(list1, list2); // false
        // list1如果为null则转换为空List
        ListUtils.emptyIfNull(list1);
        // list1中所有元素做Hash
        ListUtils.hashCodeForList(list1);
    }

    public void mapUtils() {
        // TODO
    }
}
