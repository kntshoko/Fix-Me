package com.fixme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Broker  extends Thread{

	private  static  final  String SERVER_IP = "127.0.0.1";
	private  static  final int SERVER_PORT = 5000;
	private BufferedReader input;
	private BufferedReader keyboard;
	private PrintWriter out;
	private Socket socket = null;
	private int r = 0, id = 0;
	public Broker() throws IOException {

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
			out.println("Router");
			out.println("");
			out.println("");
			while (r == 0){

				serverResponse = input.readLine();
				if(serverResponse.length() > 1   && (serverResponse.contains("35=sell") || serverResponse.contains("35=buy" )) == false)
					System.out.println("Router response : "+ serverResponse);
				if(serverResponse.contains("Router assigns")){
					String sp[] = serverResponse.split(":");
					id = Integer.parseInt(sp[1].trim());
					System.out.println(id);
					System.out.println(id);
				}
				out.println("");

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void Send(){
		try {
			
			int t = 0;
			while (true){
				t=0;
				String command = null;
				String input;
				String msgType = null;
				System.out.println("Enter buy OR sell OR quit To exit");
				input = keyboard.readLine();
				if(input.equals("quit")) break;
				if(input.equalsIgnoreCase("buy")){
					input = "|54=1";
					msgType = "buy";
					t++;
				}
				else if(input.equalsIgnoreCase("sell")){
					input = "|54=2";
					msgType = "sell";
					t++;
				}
				if(t == 1){
					command =  "|49=" + id;
					System.out.println("Enter Receiver ID");
					command = command + "|56="+ keyboard.readLine();
					command = command + input;
					System.out.println("Enter Instrument");
					command = command + "|37="+ keyboard.readLine().toLowerCase();
					int i= 0;
					while (i == 0) {
						System.out.println("Enter Instrument Quantity");
						String num = null;
						num = keyboard.readLine();
						if(isNumber(num) == 1){
						command = command + "|38="+ num ;
						i++;
						}
					}
					i = 0;
					while (i == 0) {
						System.out.println("Enter Instrument Price");
						String num = null;
						num = keyboard.readLine();
						if(isNumber(num) == 1){
						command = command + "|44="+ num ;
						i++;
						}
					}
					int len = command.length();
					int checkSum = len % 256;
					command = "|35="+ msgType + command;
					command = "|9="+ len + command;
					if(checkSum> 99)
						command = command +"|10=" + checkSum;
					else
						command = command + "|10=0" + checkSum;
					command = "8=FIX.4.2"+command;
					out.println(command);
					}
			}
			r = 1;
			socket.close();
			System.exit(0);} catch (IOException  e) {
			e.printStackTrace();
		} 
	}
	public static void main(String[] args) throws IOException {
		Broker broke = new Broker();
		broke.start();
		broke.Send();

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