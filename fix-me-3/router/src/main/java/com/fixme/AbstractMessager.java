package com.fixme;

public abstract class AbstractMessager {
    public static int broker = 1;
    public static int market = 2;

    protected int level;

    protected AbstractMessager nextMessager;

    public void setNextMessager(AbstractMessager nextMessager){
        this.nextMessager = nextMessager;
    }

    public void sendMessage(int level, String mes){
        if(this.level <= level){
            write(mes);
        }
        if(nextMessager != null){
            nextMessager.sendMessage(level, mes);
        }
    }

    abstract protected void write(String mes) ;
}
