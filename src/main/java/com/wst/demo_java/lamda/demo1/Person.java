package com.wst.demo_java.lamda.demo1;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
/**
 * 人类
 * @create 2018-10-17 12:40
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Serializable {
    private static final long serialVersionUID = -3236552889204227101L;
    private Integer id;
    private Integer age;
    private String name;
    private BigDecimal money;
 
    public Person(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) &&
                Objects.equals(name, person.name);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}