package com.designpatters.factory.generalfactory;

/**
 * 具体动作的实现类
 */
public class MailSender implements Sender {

    /*
     * 邮件的发送
     */
    @Override
    public void send() {
        System.out.println("this is mail sender!");
    }
}
