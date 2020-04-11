package com.designpatters.factory.factorymethod;

import com.designpatters.factory.generalfactory.MailSender;
import com.designpatters.factory.generalfactory.Sender;

public class MailSenderFactory implements Provider {

    @Override
    public Sender produce() {
        return new MailSender();
    }

}
