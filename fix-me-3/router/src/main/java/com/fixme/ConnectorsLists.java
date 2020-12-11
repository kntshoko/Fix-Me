package com.fixme;

import java.util.HashMap;

public class ConnectorsLists {
    HashMap<Integer,  Client> brokerConnectsList =
            new HashMap<Integer,  Client>();
    HashMap<Integer, Client> marketConnectsList =
            new HashMap<Integer,  Client>();
    public int id = 9999;
    public  void setCounter(String connector, int id,  Client channel){

        if(connector.equals("broker")){
            brokerConnectsList.put(id,channel);
        }else {
            marketConnectsList.put(id,channel);
        }

    }
    public Client getIdConnector(int id, String connector)
    {
        if(connector.equals("broker")){
            return brokerConnectsList.get(id);
        }else  if(connector.equals("market")) {
            return  marketConnectsList.get(id);
        }
        return null;
    }
}
