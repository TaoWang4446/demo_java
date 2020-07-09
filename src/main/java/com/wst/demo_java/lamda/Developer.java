package com.wst.demo_java.lamda;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;

@Data
public class Developer {
    private String name;
    private BigDecimal salary;
    private int age;



    public Developer(String name, BigDecimal salary, int age) {
        this.name = name;
        this.salary = salary;
        this.age = age;
    }
}