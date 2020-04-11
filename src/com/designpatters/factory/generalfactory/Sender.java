package com.designpatters.factory.generalfactory;

/**
 * 设计模式总原则：开闭原则 对扩展开放，对修改关闭.接口方便扩展 有新的需求时不需要修改原有代码，而是再做一个实现类来满足需求
 * 发送动作的接口，具体实现在实现类中具体实装
 */
public interface Sender {

    /**
     * 发送方法
     */
    public void send();

}
