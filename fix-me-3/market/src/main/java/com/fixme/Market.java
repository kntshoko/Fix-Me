package com.fixme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;

/**
 * Hello world!
 *
 */



public class Market  extends Thread{

	private  static  final  String SERVER_IP = "127.0.0.1";
	private  static  final int SERVER_PORT = 5001;
	private BufferedReader input;
	private BufferedReader keyboard;
	private PrintWriter out;
	private Socket socket = null;
	private int r = 0, t = 0;
	public Market(){

		try {
			socket = new Socket(SERVER_IP, SERVER_PORT);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			keyboard = new BufferedReader(new InputStreamReader(System.in));
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println("start");
			out.println("start");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public   void Send(){
		String serverResponse = null;
		try {
			while (r == 0){

				serverResponse = input.readLine();


				String[] g = serverResponse.split(":");
				if (g[0].equals("id"))
					System.out.println(g[1]);
				//	else
				//		System.out.println(serverResponse);


			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run(){
		try {

			while (true){
				System.out.println("> ");
				String command = null;

				command = keyboard.readLine();


				if(command.equals("quit")) break;;
				out.println(command);


			}
			r = 1;
			socket.close();
			System.exit(0);} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args) throws IOException {
		Market market = new Market();
		market.start();
		market.Send();

	}
}
