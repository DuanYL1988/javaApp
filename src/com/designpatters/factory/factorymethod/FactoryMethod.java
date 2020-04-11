package com.designpatters.factory.factorymethod;

public class FactoryMethod {
    public static void main(String[] args) {
        Provider mail = new MailSenderFactory();
        mail.produce().send();

        Provider sns = new SnsSenderFactory();
        sns.produce().send();
    }
}
