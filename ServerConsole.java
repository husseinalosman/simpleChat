


import java.io.*;
import java.util.Scanner;
import client.*;
import common.ChatIF;



public class ServerConsole implements ChatIF{
	final public static int DEFAULT_PORT = 5555;
	
	EchoServer server;
	
	Scanner fromConsole;
	
	public ServerConsole(int port){
		try{
			server= new EchoServer(port);
		}
		catch(Exception exception){
			System.out.println("Error: Can't setup server!"+ " Terminating server.");
			System.exit(1);
		}
	}
	
	public void accept() {
		try {
			String message;
			while(true) {
				message = fromConsole.nextLine();
			}
		}
		catch (Exception ex){
	      System.out.println("Unexpected error while reading from console!");
	    }
	}

	@Override
	public void display(String message) {
		// TODO Auto-generated method stub
		
	}

}
