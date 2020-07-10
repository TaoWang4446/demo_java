package com.wst.demo_java.base.siglton_demo;

/**
 * 静态内部类实现 单例模式
 * 优点
 *     资源利用率高，不执行getInstance()不被实例，可以执行该类其他静态方法
 * 缺点
 *     第一次加载时反应不够快
 *
 * 总结：
 *     一般采用饿汉式，若对资源十分在意可以采用静态内部类，不建议采用懒汉式及双重检测
 */
class CheckDoubleSigleton {
    private CheckDoubleSigleton() {
    }

    private static class SingletonHelp {
        static CheckDoubleSigleton instance = new CheckDoubleSigleton();
    }

    public static CheckDoubleSigleton getInstance() {
        return SingletonHelp.instance;
    }
}