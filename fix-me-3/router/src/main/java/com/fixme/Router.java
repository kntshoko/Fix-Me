package com.fixme;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Router {

	/*public void   Routers() throws IOException, ExecutionException, InterruptedException {

		AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
		InetSocketAddress hostAddress = new InetSocketAddress("localhost", 5000);
		serverChannel.bind(hostAddress);

		System.out.println("Server channel bound to port: " + hostAddress.getPort());
		System.out.println("Waiting for client to connect... ");

		serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
			@Override
			public void completed(AsynchronousSocketChannel result, Object attachment) {
				if(serverChannel.isOpen()){
					serverChannel.accept(null, this);
				}
				AsynchronousSocketChannel clientChannel = result;


			}

			@Override
			public void failed(Throwable exc, Object attachment) {

			}
		});

	}*/

	public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

		RouterServer route = new RouterServer();
		route.runServers();
	}

}


