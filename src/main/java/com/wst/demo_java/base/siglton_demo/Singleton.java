package com.wst.demo_java.base.siglton_demo;

/**
 * 双重检查
 * 优点
 *     资源利用率高，不执行getInstance()就不被实例，可以执行该类其他静态方法
 * 缺点
 *     第一次加载时反应不快，由于java内存模型一些原因偶尔失败
 */
public class Singleton {
    private Singleton() {
    }
    public static Singleton instance = null;

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
