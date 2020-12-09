package com.fixme;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.sql.ClientInfoStatus;

public class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, Client> {
    @Override
    public void completed(AsynchronousSocketChannel socketChannel, Client client) {


        SocketAddress address  = null;
        try {
            address = socketChannel.getRemoteAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }


        client.server.accept(client, this);
        System.out.printf("Accepted a connection from: %s%n", address );
        ReadWriteHandler handler = new ReadWriteHandler();
        client.client = socketChannel;
        client.buffer = ByteBuffer.allocate(1000);
        System.out.println(client.buffer);
        if(client.client.isOpen()){
            client.buffer.clear();
            client.buffer.flip();
            socketChannel.write(client.buffer,client,handler);
        }


    }

    @Override
    public void failed(Throwable exc, Client attachment) {

    }
}
