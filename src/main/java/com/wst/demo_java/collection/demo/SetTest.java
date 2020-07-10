package com.wst.demo_java.collection.demo;

/*
HashSet与Treeset的适用场景

1.TreeSet 是二差树（红黑树的树据结构）实现的,Treeset中的数据是自动排好序的，不允许放入null值 

2.HashSet 是哈希表实现的,HashSet中的数据是无序的，可以放入null，但只能放入一个null，两者中的值都不能重复，就如数据库中唯一约束 

3.HashSet要求放入的对象必须实现HashCode()方法，放入的对象，是以hashcode码作为标识的，而具有相同内容的String对象，
hashcode是一样，所以放入的内容不能重复。但是同一个类的对象可以放入不同的实例

* */
public class SetTest {
}
