package com.fixme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class Market  extends Thread{

	private  static  final  String SERVER_IP = "127.0.0.1";
	private  static  final int SERVER_PORT = 5001;
	private BufferedReader input;
	private BufferedReader keyboard;
	private PrintWriter out;
	private Socket socket = null;
	private int r = 0;
	private  HashMap<String,  Integer> InstrumentList =
	new HashMap<String,  Integer>();
	public Market() throws IOException {

		try {
			socket = new Socket(SERVER_IP, SERVER_PORT);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			keyboard = new BufferedReader(new InputStreamReader(System.in));
			out = new PrintWriter(socket.getOutputStream(), true);

			InstrumentList.put("instrument1", 100);
			InstrumentList.put("instrument1", 100);
			InstrumentList.put("instrument1", 100);
			InstrumentList.put("instrument1", 100);
			InstrumentList.put("instrument1", 100);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public   void run(){
		String serverResponse = null;
		try {
			while (r == 0){

				serverResponse = input.readLine();
				if(serverResponse.length() > 1)
					System.out.println("Router response : "+ serverResponse);
				if(serverResponse.contains("8=FIX.4.2")){
					String message[] = serverResponse.split("\\|");
					String response = "|35=Exeuted"+"|"+message[4]+"|"+message[3]+"|"+message[5]+"|"+message[6]
					+"|"+message[7]+"|"+message[8];
					int messageLength = response.length();
					int checkSum = messageLength % 256;
					
					if(checkSum> 99)
						response = response +"|10=" + checkSum;
					else
						response = response + "|10=0" + checkSum;
					response = message[0]+"|9="+messageLength+"|"+response;
					out.println(response);
					out.println("");
				}
				out.println("");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void Send(){
		try {
			out.println("Router");
			out.println("");
			sleep(200);
			out.println("");
			while (true){
				String command = null;
				System.out.println("Enter quit To exit");
				command = keyboard.readLine();
				if(command.equals("quit")) break;
			}
			r = 1;
			socket.close();
			System.exit(0);} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args) throws IOException{
		Market broke = new Market();

		broke.start();

		broke.Send();

	}
}

