package com.fixme;

import java.io.IOException;
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
            byte[] bytes = new byte[ client.buffer.remaining()];
            client.buffer.duplicate().get(bytes);
            String mes = new String(bytes, StandardCharsets.UTF_8);

            if(mes.contains("Router")){
                mes = "Router assigns  "+client.connector+" with ID: " +Integer.toString(client.id)+"";
            }

            if(client.connector.equals("broker")){
                if(mes.length() > 5){
                    if(mes.contains("Router assigns  ")){
                        System.out.println(mes);
                    }
                    else if (mes.contains("8=FIX.4.2")){
                        System.out.println(client.connector+" with id:"+client.id+" says- "+mes);
                    } 
                }
                if(mes.contains("8=FIX.4.2")){
                    String messageArray[] = mes.split("\\|");
                    System.out.println(java.util.Arrays.toString(messageArray));
                    String[] sId = messageArray[4].split("\\=");

                    if (isNumber(sId[1]) == 1 && (messageArray[2].contains("35=buy") || messageArray[2].contains("35=sell")) == true){
                        receiver = connectorsLists.getIdConnector(Integer.parseInt(sId[1]), "market");
                    }
                    if(receiver != null){
                        receiver.buffer.clear();
                        receiver.buffer.put(mes.getBytes());
                        receiver.buffer.flip();
                        client.rW = "w";
                        receiver.rW = "w";
                        receiver.client.write(receiver.buffer, receiver, client.handler);
                    }/*else {
                        client.rW = "w";
                        client.buffer.clear();
                        client.buffer.put(mes.getBytes());
                        client.buffer.flip();
                        client.client.read(client.buffer, client, this);
                    }*/
                    client.rW = "w";
                    if( (messageArray[2].contains("35=buy") || messageArray[2].contains("35=sell")) == true)
                        return;
                    if (receiver != null && (messageArray[2].contains("35=Exeuted") || messageArray[2].contains("35=Rejected" ))){
                        client.rW = "w";
                        client.buffer.clear();
                        client.buffer.put("".getBytes());
                        client.buffer.flip();
                        client.client.read(client.buffer, client, this);
                    }
                    client.buffer.clear();
                    client.buffer.put("".getBytes());
                } else{
                    client.rW = "w";
                    client.buffer.clear();
                    client.buffer.put(mes.getBytes());
                    client.buffer.flip();
                    client.client.write(client.buffer, client, this);
                }
            }
            else if(client.connector.equals("market")){
                if(mes.length() > 5){
                    if(mes.contains("Router assigns  ")){
                        System.out.println(mes);
                    }
                    else{
                        System.out.println(client.connector+" with id:"+client.id+" says- "+mes);
                    } 
                }
                if(mes.contains("8=FIX.4.2")){
                    String messageArray[] = mes.split("\\|");
                    String[] sId = messageArray[4].split("\\=");
                    if (isNumber(sId[1]) == 1)
                        receiver = connectorsLists.getIdConnector(Integer.parseInt(sId[1]), "broker");
                    if(receiver != null){
                        receiver.buffer.clear();
                        receiver.buffer.put(mes.getBytes());
                        receiver.buffer.flip();
                        client.rW = "w";
                        receiver.rW = "w";
                        receiver.client.write(receiver.buffer, receiver, client.handler);

                    }else {
                        client.buffer.clear();
                        client.buffer.put(mes.getBytes());
                        client.buffer.flip();
                        client.client.read(client.buffer, client, this);}
                    if (receiver != null){
                        client.rW = "w";
                        client.buffer.clear();
                        client.buffer.put("".getBytes());
                        client.buffer.flip();
                        client.client.read(client.buffer, client, this);
                    }
                }else{
                    client.rW = "w";
                    client.buffer.clear();
                    client.buffer.put(" ".getBytes());
                    client.buffer.flip();
                    client.client.write(client.buffer, client, this);
                }
            }
        }else {
            client.rW = "r";
            client.buffer.clear();
            try {
                client.client.read(client.buffer, client, this);
            } catch (Exception e) {
                System.out.print("");
            }
        }
    }

    @Override
    public void failed(Throwable exc, Client client) {
        System.out.println("...");
    }

    public int isNumber(String id){
        char[] st = id.toCharArray();
        for(char c : st){
            if(!Character.isDigit(c)){
                return 0;
            }
        }
        return 1;
    }
}