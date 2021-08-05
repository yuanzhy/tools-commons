package com.yuanzhy.tools.commons.collections;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.list.FixedSizeList;
import org.apache.commons.collections4.list.LazyList;
import org.apache.commons.collections4.list.PredicatedList;
import org.apache.commons.collections4.list.SetUniqueList;
import org.apache.commons.collections4.list.TransformedList;
import org.junit.Before;
import org.junit.Test;

import static com.yuanzhy.tools.commons.Util.println;

/**
 *
 * @author yuanzhy
 * @date 2021-08-05
 */
public class ListDemo {

    private List<String> sourceList;

    @Before
    public void before() {
        sourceList = new ArrayList<>();
        sourceList.add("1");
        sourceList.add("2");
        sourceList.add("3");
    }

    @Test
    public void fixedSizeList() {
        FixedSizeList<String> list = FixedSizeList.fixedSizeList(sourceList);
        list.set(0, "11");
        println(list); // [11,2,3]
        // 以下改变容器size的操作会抛出异常
        list.add("4"); // UnsupportedOperationException("List is fixed size")
        list.remove("5"); // UnsupportedOperationException("List is fixed size")
        list.clear(); // UnsupportedOperationException("List is fixed size")
    }
    @Test
    public void setUniqueList() {
        // 元素不重复的list
        SetUniqueList<String> list = SetUniqueList.setUniqueList(sourceList);
        // 存在则不处理，不会影响原来顺序
        list.add("2");
        println(list);//[1,2,3]
    }

    @Test
    public void transformedList() {
        // 转换list,在添加元素的时候会通过第二个参数Transformer转换一下（Transformer接口只有一个抽象方法可以使用lambda表达式）
        // transformingList不会对原list的已有元素做转换
        TransformedList<String> list = TransformedList.transformingList(sourceList, e -> e.concat("_"));
        list.add("a");
        println(list); // [1, 2, 3, a_]

        // transformedList会对原list的已有元素做转换
        list = TransformedList.transformedList(sourceList, e -> e.concat("_"));
        list.add("a");
        println(list); // [1_, 2_, 3_, a_]
    }
    @Test
    public void predicatedList() {
        // 在添加元素的时候会通过第二个参数Predicate判断一下是否符合要求，符合要求才添加进来
        PredicatedList<String> list = PredicatedList.predicatedList(new ArrayList<>(), e -> e.startsWith("_"));
        list.add("_4");
        println(list); // [_4]

        // 以下会抛异常：java.lang.IllegalArgumentException: Cannot add Object '4'
        list.add("4");
    }
    @Test
    public void lazyList() {
        // TODO
//        LazyList<String> list = LazyList.lazyList(sourceList, () -> "s");
//        list.add("s");
//        println(list);
    }
}
