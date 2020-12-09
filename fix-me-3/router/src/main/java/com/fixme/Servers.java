package com.fixme;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

public class Servers implements Runnable {
    private  int port;
    public Servers(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress("localhost",port));

            System.out.printf("[SERVER] listening at %s%n", serverChannel.getLocalAddress());

            Client client = new Client();
            client.server = serverChannel;
            client.rW = "w";
            if(port == 5000)
                client.connector = "broker";
            else
                client.connector = "market";
            serverChannel.accept(client, new ConnectionHandler());
            Thread.currentThread().join();
        }catch (IOException|InterruptedException e){
            e.printStackTrace();
        }
    }
}
