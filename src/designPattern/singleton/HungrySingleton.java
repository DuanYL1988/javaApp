package designPattern.singleton;

/**
 * 饿汉模式
 *
 * @author dylsw
 *
 */
public class HungrySingleton {

    // 初始化时赋值默认值
    private static HungrySingleton instance = new HungrySingleton();

    private HungrySingleton() {
    }

    public static HungrySingleton getInstance() {
        return instance;
    }
}
