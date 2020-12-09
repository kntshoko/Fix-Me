package com.fixme;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class Client {
    public AsynchronousSocketChannel channel;
    public ByteBuffer buffer;
    public Thread mainThread;
    public boolean isRead;
    public String clientId;
}
