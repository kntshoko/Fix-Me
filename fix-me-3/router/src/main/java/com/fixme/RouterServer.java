package com.fixme;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.*;

public class RouterServer{


    public void runServers() {

        ExecutorService serve = Executors.newCachedThreadPool();
        serve.submit(new Servers(5000));
        serve.submit(new Servers(5001));
        serve.shutdown();
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.Buffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import com.fixme.controlers.TimeMessage;
import org.apache.maven.plugins.dependency.utils.markers.MarkerHandler;*/

/**
 * RouterServer
 */
/*public class RouterServer {
	private int ports[] = new int[]{5000, 5001};
	private static int bID = 100000;
	private static int mID = 100000;
	HashMap<Integer,SocketChannel> markets;
	HashMap<Integer,SocketChannel> brokers;


	public RouterServer() {
		this.markets = new HashMap<Integer, SocketChannel>();
		this.brokers = new HashMap<Integer, SocketChannel>();
	}

	public void newRouterServer() {
		try {
			Selector selector = Selector.open();

			for (int port : ports) {
				ServerSocketChannel ssChannel = ServerSocketChannel.open();
				ssChannel.configureBlocking(false);
				ServerSocket sSocket = ssChannel.socket();
				sSocket.bind(new InetSocketAddress(port));
				ssChannel.register(selector, SelectionKey.OP_ACCEPT);
			}

			System.out.println("Accepting Connections on Ports\n BROKER [5000] ... MARKET[5001] \nRouting server is now running...");
			while (true) {
				if (selector.select() > 0) {
					performIO(selector);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void performIO(Selector s) {
		Iterator<SelectionKey> skIterator = s.selectedKeys().iterator();

		while (skIterator.hasNext()) {
			try {
				SelectionKey sKey = skIterator.next();
				if (sKey.isAcceptable()) {
					acceptConnection(sKey, s);
				} else if (sKey.isReadable()) {
					readWriteClient(sKey, s);
				} else {
					skIterator.remove();
				}
			} catch (IOException e) {
				skIterator.remove();
				return;
			}

			skIterator.remove();
		}
	}

	public void acceptConnection(SelectionKey sKey, Selector s) throws IOException {
		ServerSocketChannel ssChannel = (ServerSocketChannel) sKey.channel();
		SocketChannel sChannel = ssChannel.accept();
		sChannel.configureBlocking(false);
		sChannel.register(s, SelectionKey.OP_READ);


		switch (sChannel.socket().getLocalPort()) {
			case 5000: {

				TimeMessage.print("Broker connection!!!");
				break;
			}
			case 5001:
				TimeMessage.print("Market connection!!!");
				break;
		}
	}

	public void readWriteClient(SelectionKey sKey, Selector s) throws IOException {
		SocketChannel sChannel = (SocketChannel) sKey.channel();

		switch (sChannel.socket().getLocalPort()) {
			case 5000:
				try {
					String j = sChannel.getRemoteAddress().toString();
					String jj[] = j.split(":");
					j = jj[1];
					BrokerHandler(sChannel,Integer.parseInt(j));
				} catch (IOException e) {
					e.printStackTrace();
				}

				sChannel.register(s, SelectionKey.OP_READ);

				break;
			case 5001:{
				try {
					String j = sChannel.getRemoteAddress().toString();
					String jj[] = j.split(":");
					j = jj[1];
					MarketHandler(sChannel,Integer.parseInt(j));
				} catch (IOException e) {
					e.printStackTrace();
				}
				sChannel.register(s, SelectionKey.OP_READ);
				break;}
		}
	}

	private void BrokerHandler( SocketChannel sChannel, int id) throws IOException {

		ByteBuffer cBuffer = ByteBuffer.allocate(1000);
		((Buffer) cBuffer).flip();
		((Buffer) cBuffer).clear();

		brokers.put(id,sChannel);
		String responds = null;
		try {

			String tt = Integer.toString(id);

			int in = sChannel.read(cBuffer);
			String s;

			if(in > 0) {
				s = new String(cBuffer.array());

				s = s.trim().toLowerCase();

				if(s.equals("start")){
					responds = "";
					responds = "id:" + tt+":";


					cBuffer.put(responds.getBytes());
					cBuffer.flip();
					sChannel.write(cBuffer);

				}
				else if (s.length() > 0){

				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private void MarketHandler( SocketChannel sChannel, int id) {
		ByteBuffer cBuffer = ByteBuffer.allocate(1000);
		((Buffer) cBuffer).flip();
		((Buffer) cBuffer).clear();

		markets.put(id, sChannel);
		String responds = null;
		try {

			String tt = Integer.toString(id);

			int in = sChannel.read(cBuffer);
			String s;

			if (in > 0) {
				s = new String(cBuffer.array());

				s = s.trim().toLowerCase();

				if (s.equals("start")) {
					responds = "";
					responds = "id:" + tt + ":";


					cBuffer.put(responds.getBytes());
					cBuffer.flip();
					sChannel.write(cBuffer);

				} else if (s.length() > 0) {

				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private  void BrokerMessageHandler(int id, String s){

		SocketChannel socketChannel = markets.get(100000);
		ByteBuffer bBuffer = ByteBuffer.allocate(1000);
		((Buffer) bBuffer).flip();
		((Buffer) bBuffer).clear();

		bBuffer.put(s.getBytes());
		bBuffer.flip();
		try {
			socketChannel.write(bBuffer);
			bBuffer.clear();
			bBuffer.flip();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	private  void MarketMessageHandler(int id, String s){

		SocketChannel socketChannel = brokers.get(100000);
		ByteBuffer bBuffer = ByteBuffer.allocate(1000);
		((Buffer) bBuffer).flip();
		((Buffer) bBuffer).clear();

		bBuffer.put(s.getBytes());
		bBuffer.flip();
		try {
			socketChannel.write(bBuffer);
			bBuffer.clear();
			bBuffer.flip();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	private void Push(SocketChannel sChannel, String responds){
		ByteBuffer cBuffer = ByteBuffer.allocate(1000);
		((Buffer) cBuffer).flip();
		((Buffer) cBuffer).clear();


	}
}*/