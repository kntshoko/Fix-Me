package com.fixme;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;


public class ConnectionHandler  implements CompletionHandler<AsynchronousSocketChannel, Client> {

    public int brokerCounter = 99999;
    public  int marketCounter = 99999;
    public  ConnectorsLists connectorsLists;
    public ConnectionHandler(ConnectorsLists connectorsLists) {
        this.connectorsLists = connectorsLists;
    }

    @Override
    public void completed(AsynchronousSocketChannel socketChannel, Client client) {


        SocketAddress address  = null;
        try {
            address = socketChannel.getRemoteAddress();
        } catch (IOException e) {
            System.out.println("**********");
        }


        client.server.accept(client, this);

        System.out.printf("Accepted a connection from: %s%n", address );
        ReadWriteHandler handler = new ReadWriteHandler(connectorsLists);
        Client newClient = new Client();
        newClient.client =socketChannel;
        newClient.connector = client.connector;
        if(  client.connector.equals("broker")){
            brokerCounter++;
            newClient.id = brokerCounter;
        }
        else{
            marketCounter++;
            newClient.id = marketCounter;
        }
        newClient.rW = "r";
        newClient.handler = handler;
        newClient.buffer = ByteBuffer.allocate(1000);
        newClient.buffer.put(("Router assigned ID = "+Integer.toString(newClient.id)).getBytes());
        newClient.buffer.flip();      
        connectorsLists.setCounter(newClient.connector,newClient.id,newClient);
        socketChannel.read(newClient.buffer,newClient,handler);
    }

    @Override
    public void failed(Throwable exc, Client attachment) {
        System.out.println("...");
    }
}
