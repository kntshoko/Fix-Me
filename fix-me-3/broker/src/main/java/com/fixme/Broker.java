package com.fixme;


import java.util.concurrent.ExecutionException;

public  class Broker{

	public static void main(String[] args) {
		BrokerConnect broker = new BrokerConnect();
		try {
			broker.connect();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

}