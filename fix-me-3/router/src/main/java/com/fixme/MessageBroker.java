package com.fixme;

public class MessageBroker  extends AbstractMessager{
    public MessageBroker(int level){
        this.level = level;
        
    }

    @Override
    protected void write(String mes) {
        //System.out.println("*******broker********");

    }
}

