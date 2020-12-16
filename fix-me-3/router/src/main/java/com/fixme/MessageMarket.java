package com.fixme;

public class MessageMarket  extends AbstractMessager{
    public MessageMarket(int level){
        this.level = level;
        
    }

    @Override
    protected void write(String mes) {
        //System.out.println("*******Market******");

    }
}
