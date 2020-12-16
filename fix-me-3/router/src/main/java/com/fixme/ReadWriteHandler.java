package com.fixme;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

class ReadWriteHandler implements CompletionHandler<Integer, Client> {
    public ConnectorsLists connectorsLists;
    public Client receiver;

    public ReadWriteHandler(ConnectorsLists connectorsLists) {
        this.connectorsLists = connectorsLists;
    }

    private static AbstractMessager getChaninOfMessages() {
        AbstractMessager bAbstractMessager = new MessageBroker(AbstractMessager.broker);
        AbstractMessager mAbstractMessager = new MessageMarket(AbstractMessager.market);

        bAbstractMessager.setNextMessager(bAbstractMessager);
        mAbstractMessager.setNextMessager(bAbstractMessager);

        return bAbstractMessager;
    }

    @Override
    public void completed(Integer result, Client client) {

        if (result == -1) {
            try {
                client.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

            if (client.rW.equals("r")) {
                client.buffer.flip();
                byte[] bytes = new byte[client.buffer.remaining()];
                client.buffer.duplicate().get(bytes);
                String mes = new String(bytes, StandardCharsets.UTF_8);
    
                if (mes.contains("Router")) {
                    mes = "Router assigns  " + client.connector + " with ID: " + Integer.toString(client.id) + "";
                }
    
                if (mes.length() > 5) {
                    if (mes.contains("Router assigns  ")) {
                        System.out.println(mes);
                    } else if (mes.contains("8=FIX.4.2")) {
                        System.out.println(client.connector + " with id:" + client.id + " says- " + mes);
                    }
                }
    
                if (mes.contains("8=FIX.4.2")) {
    
                    if ( client.connector.equals("market")) {
                        if((mes.contains("35=buy") || mes.contains("35=sell"))){
                        client.rW = "w";
                        client.buffer.clear();
                        
                        try{
                            client.client.read(client.buffer, client, this);
                        } catch (Exception e) {
                            System.out.print("...");
                        }
                    }
                }
                    if ( client.connector.equals("broker")) {
                        if((mes.contains("35=Reject") || mes.contains("35=Exeuted")))
                        {client.rW = "w";
                        client.buffer.clear();
                        try{
                            client.client.read(client.buffer, client, this);
                        } catch (Exception e) {
                            System.out.print("...");
                        }
                        }
                    }
                    String messageArray[] = mes.split("\\|");
                    String[] sId = messageArray[4].split("\\=");
                    client.rW = "w";
                    if (client.connector.equals("broker") && isNumber(sId[1]) == 1
                            && (messageArray[2].contains("35=buy") || messageArray[2].contains("35=sell")) == true) {
                        receiver = connectorsLists.getIdConnector(Integer.parseInt(sId[1]), "market");
                        if (receiver != null) {
                            receiver.buffer.clear();
                            receiver.buffer.put(mes.getBytes());
                            receiver.buffer.flip();
                            receiver.rW = "w";
                            client.buffer.clear();
                            client.buffer.flip();
                            try{
                                receiver.client.write(receiver.buffer, receiver, receiver.handler);
                            } catch (Exception e) {
                                System.out.print("...");
                            }
                           
    
                        } else {
                            try{
                                client.client.write(client.buffer, client, this);
                            } catch (Exception e) {
                                System.out.print("...");
                            }
                           
                        }
                        try{
                            client.client.write(client.buffer, client, this);
                        } catch (Exception e) {
                            System.out.print("...");
                        }
                        
                    }
                    if (client.connector.equals("market") && isNumber(sId[1]) == 1 
                            && (messageArray[2].contains("35=Reject") || messageArray[2].contains("35=Exeuted")) == true) {
                        receiver = connectorsLists.getIdConnector(Integer.parseInt(sId[1]), "broker");
                        if (receiver != null) {
                            receiver.buffer.clear();
                            receiver.buffer.put(mes.getBytes());
                            receiver.buffer.flip();
                            receiver.rW = "w";
                            client.buffer.clear();
                            client.buffer.flip();
                            try{
                                receiver.client.write(receiver.buffer, receiver, receiver.handler);
                            } catch (Exception e) {
                                System.out.print("...");
                            }
                            
                        }
                        else {
                            try{
                                client.client.write(client.buffer, client, this);
                            } catch (Exception e) {
                                System.out.print("...");
                            }
                            
                        }
                        try{
                            client.client.write(client.buffer, client, this);
                        } catch (Exception e) {
                            System.out.print("...");
                        }
                        
                    }
                } else {
                
                    receiver = connectorsLists.getIdConnector(client.id, client.connector);
                        
                        receiver.rW = "w";
                        receiver.buffer.clear();
                        receiver.buffer.put(mes.getBytes());
                        receiver.buffer.flip();
                         client.rW = "w";
                         client.buffer.clear();
                         client.buffer.put(mes.getBytes());
                         client.buffer.flip();
                         try{
                        receiver.client.write(receiver.buffer, receiver, receiver.handler);
                        } catch (Exception e) {
                            System.out.print("...");
                        }

                }
            }else {
                client.rW = "r";
                client.buffer.clear();
                try{
                client.client.read(client.buffer, client, this);
                } catch (Exception e) {
                    System.out.print("...");
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