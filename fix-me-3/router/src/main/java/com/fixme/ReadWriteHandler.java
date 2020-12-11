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
            int l = client.buffer.limit();
            byte[] bytes = new byte[ client.buffer.remaining()];
            client.buffer.duplicate().get(bytes);
            String mes = new String(bytes, StandardCharsets.UTF_8);
            if(l > 0) {
                if(mes.contains("message")){
                    receiver = connectorsLists.getIdConnector(100000, "market");
                    if (receiver != null){
                        receiver.buffer.clear();
                        receiver.buffer.put(mes.getBytes());
                        receiver.buffer.flip();
                        receiver.client.write(receiver.buffer, receiver, receiver.handler);
                        receiver.rW = "r";
                        client.rW = "r";
                    }
                    else  if(mes.contains("received")){
                        receiver = connectorsLists.getIdConnector(100000, "broker");
                        if (receiver != null){
                            receiver.buffer.clear();
                            receiver.buffer.put(mes.getBytes());
                            receiver.buffer.flip();
                            receiver.client.write(receiver.buffer, receiver, receiver.handler);
                            client.rW = "r";
                        }
                }
                    else {

                    }
                }
                System.out.println(client.connector + " id= "+ client.id + "  says :" + mes);
            }
            client.buffer.clear();
            client.buffer.put(mes.getBytes());
            client.buffer.flip();
            client.rW = "w";
            if(!client.connector.equals("market")){
                client.rW = "w";
                return;
            }
            client.mes = mes;
            client.buffer.clear();
        }else {
//            client.rW = "r";
//            client.buffer.clear();
//            client.client.read(client.buffer, client, this);
        }
    }

    @Override
    public void failed(Throwable exc, Client client) {

    }
}