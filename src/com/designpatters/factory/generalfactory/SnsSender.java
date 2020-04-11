package com.designpatters.factory.generalfactory;

/**
 * 具体动作的实现类
 */
public class SnsSender implements Sender {

    /*
     * 短信的发送
     */
    @Override
    public void send() {
        System.out.println("this is sns sender!");
    }

}
