package com.fixme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Market  extends Thread{

	private  static  final  String SERVER_IP = "127.0.0.1";
	private  static  final int SERVER_PORT = 5001;
	private BufferedReader input;
	private BufferedReader keyboard;
	private PrintWriter out;
	private Socket socket = null;
	private int r = 0, t = 0;
	public Market() throws IOException {

		try {
			socket = new Socket(SERVER_IP, SERVER_PORT);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			keyboard = new BufferedReader(new InputStreamReader(System.in));
			out = new PrintWriter(socket.getOutputStream(), true);

			out.println("Router");
			out.println("");
			out.println("");
			out.println("");
			out.println("");


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
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void Send(){
		try {

			while (true){
				String command = null;
				System.out.println("Enter buy OR sell OR quit To exit");
				command = keyboard.readLine();
				if(command.equals("quit")) break;

				/*System.out.println("Enter Item name");
				command = command + "| "+ keyboard.readLine();

				System.out.println("Enter Item Price");
				command = command + "| "+ keyboard.readLine();
				System.out.println("Enter Receiver ID");
				command = command + "| "+ keyboard.readLine();*/
				out.println(command);

				out.println("");
			}
			r = 1;
			socket.close();
			System.exit(0);} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args) throws IOException {
		Market broke = new Market();
		broke.start();
		broke.Send();

	}
}
