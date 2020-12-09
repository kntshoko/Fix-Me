package com.fixme;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

public class Client {

    public AsynchronousServerSocketChannel server;
    public AsynchronousSocketChannel client;
    public  String connector;
    public String rW;
    public String mes;
    public ByteBuffer buffer;
}
