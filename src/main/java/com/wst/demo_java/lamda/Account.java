package com.wst.demo_java.lamda;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Account {
    private String username;
    private Integer money;

    public Account() {
    }
    public Account(String username, Integer money) {
        this.username = username;
        this.money = money;
    }
    
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("mayun",2000);
        map.put("mahuateng",1200);
        map.put("liuqiangdong",700);
        List<Account> collect = map.entrySet().stream().map(en -> new Account(en.getKey(), en.getValue())).collect(Collectors.toList());

        collect.stream().forEach(n-> System.out.println(n));
        collect = collect.stream().filter(account -> account.getMoney()==1200)
                .map(account -> new Account(account.getUsername(),account.getMoney()))
                .collect(Collectors.toList());
        System.out.println("--------");
        collect.stream().forEach(n-> System.out.println(n));

    }
}
