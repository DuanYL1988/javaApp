package designPattern.singleton;

/**
 * 懒汉单例模式
 *
 * @author dylsw
 *
 */
public class LazySingleton {

    // volatile 防止JVM重排序
    private volatile static LazySingleton instance;

    // 防止外部实例化
    private LazySingleton() {

    }

    public static LazySingleton getInstance() {
        // 没有对象新建,不然返回
        if (instance == null) {
            // 防止多线程实例化多个对象
            synchronized (LazySingleton.class) {
                instance = new LazySingleton();
            }
        }
        return instance;
    }
}
