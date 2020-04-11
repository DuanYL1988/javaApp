package com.designpatters.factory.factorymethod;

import com.designpatters.factory.generalfactory.Sender;

/**
 * 抽象工厂模式 再简单工厂基础上，再添加一个接口，让简单工厂的类实现接口
 * 这样，在扩展功能时，只需实现Provider接口，就可以创建一个工厂类和具体的实现类。 相对于工厂类，更便于功能的扩展
 */
public interface Provider {

    /**
     * 最基础的Sender接口
     */
    public Sender produce();
}
