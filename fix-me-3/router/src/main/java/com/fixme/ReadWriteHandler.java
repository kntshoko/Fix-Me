package com.fixme;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.Map;

class ReadWriteHandler implements CompletionHandler<Integer, Client> {

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
            System.out.println("aaaa");
            client.buffer.flip();
            int l = client.buffer.limit();
            byte[] bytes = new byte[ client.buffer.remaining()];
            client.buffer.duplicate().get(bytes);
            String mes = new String(bytes, StandardCharsets.UTF_8);

            System.out.println(mes);
            System.out.print(l);

            client.buffer.clear();
            client.buffer.put(mes.getBytes());
            client.buffer.flip();

            client.client.write(client.buffer, client, this);
            client.rW = "w";
            if(!client.connector.equals("market") && mes.contains("received")){

                client.rW = "w";
                return;
            }
            client.mes = mes;
            client.buffer.clear();
            //client.client.read(client.buffer, client, this);

        }else {
            client.rW = "r";
            client.buffer.clear();
            client.client.read(client.buffer, client, this);
        }


    }

    @Override
    public void failed(Throwable exc, Client client) {

    }
}