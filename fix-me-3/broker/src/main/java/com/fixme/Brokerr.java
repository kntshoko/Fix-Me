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
    private int r = 0, t = 0;
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

    public   void Send(){
        String serverResponse = null;
        try {
            while (r == 0){
					serverResponse = input.readLine();
					System.out.println(serverResponse);
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
		Brokerr broke = new Brokerr();
		broke.start();
		broke.Send();

	}
}