package designPattern.singleton;

import org.junit.Test;

/**
 * 懒汉单例模式
 *
 * @author dylsw
 *
 */
public class TestSingleton {

    @Test
    public void testLazySingleton() {
        // 多线程
        new Thread(new Runnable() {
            public void run() {
                LazySingleton single1 = LazySingleton.getInstance();
                System.out.println(single1);
            }
        }).start();
        ;
        // 多线程
        new Thread(new Runnable() {
            public void run() {
                LazySingleton single2 = LazySingleton.getInstance();
                System.out.println(single2);
            }
        }).start();
        ;
    }
}
