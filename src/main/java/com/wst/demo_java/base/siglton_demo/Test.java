package com.wst.demo_java.base.siglton_demo;

/**
 * 饿汉式（推荐）
 *优点
 *     1.线程安全
 *     2.在类加载的同时已经创建好一个静态对象，调用时反应速度快
 * 缺点
 *     资源效率不高，可能getInstance()永远不会执行到，但执行该类的其他静态方法或者加载了该类（class.forName)，那么这个实例仍然初始化
 */
 public class Test {
         private Test() {
         }
         public static Test instance = new Test();
         public Test getInstance() {
                 return instance;
         }
 }