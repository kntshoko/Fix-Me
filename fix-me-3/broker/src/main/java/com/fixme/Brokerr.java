package com.fixme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Brokerr  extends Thread{

	private  static  final  String SERVER_IP = "127.0.0.1";
	private  static  final int SERVER_PORT = 5000;
	private BufferedReader input;
	private BufferedReader keyboard;
	private PrintWriter out;
	private Socket socket = null;
	private int r = 0, id = 0;
	public Brokerr() throws IOException {

		try {
			socket = new Socket(SERVER_IP, SERVER_PORT);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			keyboard = new BufferedReader(new InputStreamReader(System.in));
			out = new PrintWriter(socket.getOutputStream(), true);




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
				if(serverResponse.contains("Router assigns")){
					String sp[] = serverResponse.split(":");
					id = Integer.parseInt(sp[1].trim());
				}

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
			int t = 0;
			while (true){
				t=0;
				String command = null;
				String input;
				System.out.println("Enter buy OR sell OR quit To exit");
				input = keyboard.readLine();
				if(input.equals("quit")) break;
				if(input.equalsIgnoreCase("buy")){
					input = "|54=1";
					t++;
				}
				else if(input.equalsIgnoreCase("sell")){
					input = "|54=2";
					t++;
				}
				if(t == 1){
					command =  "|49=" + id;
					System.out.println("Enter Receiver ID");
					command = command + "|56="+ keyboard.readLine();
					command = command + input;
					System.out.println("Enter Instrument");
					command = command + "|37="+ keyboard.readLine();
					System.out.println("Enter Instrument Quantity");
					command = command + "|38="+ keyboard.readLine();
					System.out.println("Enter Instrument Price");
					command = command + "|44="+ keyboard.readLine();
					int len = command.length();
					int checkSum = len % 256;
					command = "|35=" + command;
					command = "|9="+ len + command;
					if(checkSum> 99)
						command = command +"|10=" + checkSum;
					else
						command = command + "|10=0" + checkSum;
					command = "8=FIX.4.2"+command;
					out.println(command);
					out.println("");}
			}
			r = 1;
			socket.close();
			System.exit(0);} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args) throws IOException {
		Brokerr broke = new Brokerr();

		broke.start();

		broke.Send();

	}
}