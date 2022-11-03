


import java.io.*;
import java.util.Scanner;
import client.*;
import common.ChatIF;



public class ServerConsole implements ChatIF {
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
		fromConsole = new Scanner(System.in); 
	}
	
	public void accept() {
		try {
			String message;
			while(true) {
				message = fromConsole.nextLine();
				server.handleMessageFromServer(message);
			}
		}
		catch (Exception ex){
	      System.out.println("Unexpected error while reading from console!");
	    }
	}

	@Override
	public void display(String message) {
		// TODO Auto-generated method stub
		System.out.println("> " + message);
	}



/**
 * This method is responsible for the creation of 
 * the server instance (there is no UI in this phase).
 *
 * @param args[0] The port number to listen on.  Defaults to 5555 
 *          if no argument is entered.
 */
	public static void main(String[] args) {
	  int port = 0; //Port to listen on
	
	  try
	  {
	    port = Integer.parseInt(args[0]); //Get port from command line
	  }
	  catch(Throwable t)
	  {
	    port = DEFAULT_PORT; //Set port to 5555
	  }
		
	  EchoServer sv = new EchoServer(port);
	  
	  try 
	  {
	    sv.listen(); //Start listening for connections
	  } 
	  catch (Exception ex) 
	  {
	    System.out.println("ERROR - Could not listen for clients!");
	  }
	}
}
