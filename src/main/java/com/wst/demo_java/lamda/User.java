package com.wst.demo_java.lamda;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

@Data
@AllArgsConstructor
public class User{
    private String name;
    private int age;

    public static void main(String[] args) {
        Map<String, User> map = new HashMap<>();
        map.put("1", new User("zhangsan", 17));
        map.put("2", new User("lisi", 10));
        map.put("3", new User("wangwu", 20));
        map.put("4", new User("zhaoliu", 19));
        // 排序前
        map.forEach((key, value) -> System.out.println("key: " + key + ", value:" + value));
        map = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(comparingInt(User::getAge)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        // 排序后
        map.forEach((key, value) -> System.out.println("key: " + key + ", value:" + value));
    }

}
