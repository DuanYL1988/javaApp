package com.designpatters.factory.generalfactory;

public class SenderFactory {

    public static void main(String[] args) {
        // 一般工厂模式
        SenderFactory factory = new SenderFactory();
        factory.existed("mail").send();
        factory.existed("sns").send();
        // 多方法工厂模式
        factory.mailSender().send();
        factory.snsSender().send();
        // 静态工厂
        simpleMailSender().send();
        simpleSnsSender().send();
    }

    /**
     * @param type
     *            种类
     * @return 发送的接口类
     */
    public Sender existed(String type) {
        if ("mail".equals(type)) {
            return new MailSender();
        } else if ("sns".equals(type)) {
            return new SnsSender();
        } else {
            return null;
        }
    }

    public Sender mailSender() {
        return new MailSender();
    }

    public Sender snsSender() {
        return new SnsSender();
    }

    public static Sender simpleMailSender() {
        return new MailSender();
    }

    public static Sender simpleSnsSender() {
        return new SnsSender();
    }
}
