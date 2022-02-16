package designPattern.singleton;

/**
 * 静态内部类单例
 *
 * @author dylsw
 *
 */
public class InnerSingleton {

    private static class InnerClassHolder {
        private static InnerSingleton instance = new InnerSingleton();
    }

    private InnerSingleton() {
        if (InnerClassHolder.instance != null) {
            throw new RuntimeException("单例不允许多个实例");
        }
    }

    public static InnerSingleton getInstance() {
        // 包含一层,只有在使用InnerSingleton时才会初始化instance
        return InnerClassHolder.instance;
    }
}
