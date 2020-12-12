package com.fixme;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

class ReadWriteHandler implements CompletionHandler<Integer, Client> {
    public ConnectorsLists connectorsLists;
    public  Client receiver;
    public ReadWriteHandler(ConnectorsLists connectorsLists) {
        this.connectorsLists = connectorsLists;
    }

    @Override
    public void completed(Integer result, Client client) {

        if(result == -1){
            try {
                client.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(client.rW.equals("r")){
            client.buffer.flip();
            int l = client.buffer.limit();
            byte[] bytes = new byte[ client.buffer.remaining()];
            client.buffer.duplicate().get(bytes);
            String mes = new String(bytes, StandardCharsets.UTF_8);

            if(mes.contains("Router")){
                mes = "Router assigns  "+client.connector+" with ID: [" +Integer.toString(client.id)+"]";
            }
            if(mes.length() > 1)
                System.out.println(mes);


            if(client.connector.equals("broker")){
                if(mes.contains("Msg")){

                    System.out.println("*** "+mes);
                    receiver = connectorsLists.getIdConnector(100000, "market");
                    if(receiver != null){
                        receiver.buffer.clear();
                        receiver.buffer.put(mes.getBytes());
                        receiver.buffer.flip();
                        client.rW = "w";
                        receiver.client.write(receiver.buffer, receiver, client.handler);
                    }
                    client.buffer.clear();
                    client.client.read(client.buffer, client, this);
                }else{
                    client.rW = "w";
                    client.buffer.clear();
                    client.buffer.put(mes.getBytes());
                    client.buffer.flip();
                    client.client.write(client.buffer, client, this);
                }
            }
            else if(client.connector.equals("market")){
                if(mes.contains("Msg")){

                    System.out.println("*** "+mes);
                    receiver = connectorsLists.getIdConnector(100000, "broker");
                    if(receiver != null){
                        receiver.buffer.clear();
                        receiver.buffer.put(mes.getBytes());
                        receiver.buffer.flip();
                        client.rW = "w";
                        receiver.client.write(receiver.buffer, receiver, client.handler);
                    }
                    client.buffer.clear();
                    client.client.read(client.buffer, client, this);
                }else{
                    client.rW = "w";
                    client.buffer.clear();
                    client.buffer.put(mes.getBytes());
                    client.buffer.flip();
                    client.client.write(client.buffer, client, this);
                }
            }
            /*client.rW = "w";
            client.buffer.clear();
            client.buffer.put(mes.getBytes());
            client.buffer.flip();
            client.client.write(ByteBuffer.wrap(mes.getBytes()), client, client.handler);*/
        }else {
            client.rW = "r";
            client.buffer.clear();
            client.client.read(client.buffer, client, this);
        }
    }

    @Override
    public void failed(Throwable exc, Client client) {

    }
