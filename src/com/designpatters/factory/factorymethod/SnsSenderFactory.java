package com.designpatters.factory.factorymethod;

import com.designpatters.factory.generalfactory.Sender;
import com.designpatters.factory.generalfactory.SnsSender;

public class SnsSenderFactory implements Provider {

    @Override
    public Sender produce() {
        return new SnsSender();
    }

}
