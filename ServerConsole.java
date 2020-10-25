import java.io.IOException;
import java.util.Scanner;

import client.ChatClient;
import common.ChatIF;

public class ServerConsole implements ChatIF{
		  
	  /**
	   * Scanner to read from the console
	   */
	  Scanner fromConsole; 
	  EchoServer sv;
	  //Constructors ****************************************************

	  /**
	   * Constructs an instance of the ClientConsole UI.
	   *
	   * @param host The host to connect to.
	   * @param port The port to connect on.
	   */
	  public ServerConsole(EchoServer sv) 
	  {
	    this.sv = sv;
	    
	    // Create scanner object to read from console
	    fromConsole = new Scanner(System.in); 
	    // Create scanner object to read from console
	    fromConsole = new Scanner(System.in); 
	  }

	@Override
	public void display(String message) {
		System.out.println("> " + message);
	}
	
	//Instance methods ************************************************
	  
	  /**
	   * This method waits for input from the console.  Once it is 
	   * received, it sends it to the client's message handler.
	   */
	  public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        if (message.charAt(0) == '#') {
	        	switch (message.split(" ")[0]) {
	        	case "#quit":
	        		System.exit(0);
	        		break;
	        	case "#stop":
	        		sv.stopListening();
	        		break;
	        	case "#close":
	        		sv.close();
	        		break;
	        	case "#start":
	        		sv.listen();
	        		break;
	        	case "#getport" :
	        		display(""+ sv.getPort());
	        		break;
	        	case "#setport":
	        		sv.setPort(Integer.parseInt(message.split(" ")[1]));
	        		display("Port set to: " + message.split(" ")[1]);
	        		break;
	        	default:
	        		display("No such command exists!");
	        	}
	        }else {
	        	sv.sendToAllClients("SERVER MSG> " + message);
	        	display("SERVER MSG> " + message);
	        }
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }

}
