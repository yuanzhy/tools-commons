package com.yuanzhy.tools.commons.collections;

import org.apache.commons.collections4.ListValuedMap;
import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.map.AbstractReferenceMap;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.collections4.map.FixedSizeMap;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.apache.commons.collections4.map.PredicatedMap;
import org.apache.commons.collections4.map.ReferenceMap;
import org.apache.commons.collections4.map.TransformedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.yuanzhy.tools.commons.Util.println;

public class MapDemo {
    // user: 张三，李四，张三
    // age: 12
    public void jdkValueMap() {
        // 使用JDK实现
        Map<String, List<String>> map = new HashMap<>();
        List<String> users = map.get("user");
        if (users == null) {
            users = new ArrayList<>();
            map.put("user", users);
        }
        users.add("张三");
        users.add("李四");
        users.add("张三");
        List<String> ages = map.get("age");
        if (ages == null) {
            ages = new ArrayList<>();
            map.put("age", ages);
        }
        ages.add("12");

        // 当然可以使用Java8的方法简化
        map.computeIfPresent("user", (k, v)-> new ArrayList<>()).add("张三");
        map.computeIfPresent("user", (k, v)-> new ArrayList<>()).add("李四");
        map.computeIfPresent("user", (k, v)-> new ArrayList<>()).add("张三");
        map.computeIfPresent("age", (k, v)-> new ArrayList<>()).add("12");
        users = map.get("user"); // [张三,李四,张三]

//        map.containsKey("user"); // true
//        map.values().contains("张三");
//        map.getOrDefault("user", Collections.emptyList()).contains("张三");
    }

    // 多值map
    // put相同key时不再覆盖，而是生成一个List将所有value放起来
    @Test
    public void multiValueMap() {
        // 使用commons实现
        ListValuedMap<String, String> map = new ArrayListValuedHashMap<>(); // list实现，允许value重复
        map.put("user", "张三");
        map.put("user", "李四");
        map.put("user", "张三");
        map.put("age", "12");
        List<String> users2 = map.get("user"); // [张三,李四,张三]

        // multiMap的其他方法
        map.containsKey("user"); // true
        map.containsValue("张三"); // true
        map.containsMapping("user", "张三"); // true

        int size = map.size(); // 4

        Collection<String> ss = map.values();// [张三,李四,12]
        map.remove("user"); // 清空user的所有value
        // 转换为原生map
        Map<String, Collection<String>> jMap = map.asMap();
    }
    @Test
    public void caseInsensitiveMap() {
        // key大小写不敏感
        Map<String, Integer> map = new CaseInsensitiveMap<>();
        map.put("one", 1);
        map.put("two", 2);
        Integer o = map.get("ONE");
        println(o);
    }
    @Test
    public void fixedSizeMap() {
        // FixedSizeMap包装其他map，包装后不允许改变其大小，但允许覆盖key
        Map<String, String> sourceMap = new HashMap<>();
        sourceMap.put("a", "aV");
        sourceMap.put("b", "bV");
        sourceMap.put("c", "cV");
        FixedSizeMap<String, String> map = FixedSizeMap.fixedSizeMap(sourceMap);
        map.put("c", "new cV"); // 允许覆盖操作

        // 以下操作全部抛出异常
        map.put("d", "dV"); // java.lang.UnsupportedOperationException: Map is fixed size
        map.remove("b");// java.lang.UnsupportedOperationException: Map is fixed size
        map.clear();// java.lang.UnsupportedOperationException: Map is fixed size
    }
    @Test
    public void orderedMap() {
        // key有序：按照插入顺序
        // 如果使用hashMap的话key会按照hash值排序，可能和插入顺序一样，也可能不一样。key数量和不同JDK版本都可能影响顺序
        // 这是由于不同版本jdk map的hash算法有区别，hash算法和当前map的容量也有一定关系。
        OrderedMap<String, String> map = new ListOrderedMap<>();
        map.put("哈哈", "1");
        map.put("此处", "2");
        map.put("cc", "3");
        map.put("dd", "4");
        Set<String> set = map.keySet(); // 哈哈,此处,cc,dd
        String nk = map.nextKey("此处"); // cc
        String pk = map.previousKey("此处"); // 哈哈
    }
    @Test
    public void lruMap() {
        // LRU（Least recently used，最近最少使用）算法根据数据的历史访问记录来进行淘汰数据，其核心思想是“如果数据最近被访问过，那么将来被访问的几率也更高”。
        LRUMap<String, String> map = new LRUMap<>(2);
        map.put("aa", "1");
        map.put("bb", "2");
        map.put("cc", "3");
        // 最早没有被使用的aa将被移出
        println(map); // [bb:2, cc:3]
        // 访问一次bb，此时在put的话将会移出最早没有被访问的cc
        map.get("bb");
        map.put("dd", "4");
        println(map); // [bb:2, dd:4]
    }

    public void multiKeyMap() {
        // 避免自行拼接key了
        MultiKeyMap<String, String> map = new MultiKeyMap<>();
        map.put("zhang", "san", "张三");
        map.put("li", "si", "李四");
        String zs = map.get("zhang", "san");
    }

    public void passiveExpiringMap() {
        // TODO
        PassiveExpiringMap<String, String> map = new PassiveExpiringMap<>();
    }

    @Test
    public void predicatedMap() {
        // 用来装饰其他的map, 通过两个Predicate参数来控制key和value的合法性校验，
        // 调用其put方法时，Predicate返回true则put成功，否则抛出IllegalArgumentException
        Map<String, String> sourceMap = new HashMap<>();
        // 装饰其他map只允许key的规则是 user_ 开头
        Map<String, String> map = PredicatedMap.predicatedMap(sourceMap, k -> k.startsWith("user_"), v -> true);
        map.put("user_a", "aa");
        map.put("user_b", "bb");
        // 以下将抛出异常
        map.put("cc", "cc");//java.lang.IllegalArgumentException: Cannot add key - Predicate rejected it
    }

    public void referenceMap() {
        ReferenceMap<String, String> map = new ReferenceMap<>(AbstractReferenceMap.ReferenceStrength.SOFT, AbstractReferenceMap.ReferenceStrength.SOFT);
        // TODO  ReferenceIdentityMap
    }
    @Test
    public void transformedMap() {
        Map<String, String> sourceMap = new HashMap<>();
        // 包装其他map, 将key转换为大写，value添加_后缀
        TransformedMap<String, String> map = TransformedMap.transformedMap(sourceMap, k -> k.toUpperCase(), v -> v.concat("_"));
        map.put("aa", "1");
        map.put("bb", "2");
        println(map);//{AA=1_, BB=2_}
    }
}
