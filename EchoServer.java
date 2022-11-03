// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.*;
import client.ChatClient;
import ocsf.server.*;


/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
	
  final private String key="loginKey";
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //I add
  ChatClient client;
  
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
    
    try{
    	listen(); //Start listening for connections
    }
    catch (Exception ex){
    	System.out.println("ERROR - Could not listen for clients!");
    }
    
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
	  System.out.println("Message received: " + msg + " from " + client);
	    String msgStr=(String)msg;
	    if(msgStr.startsWith("#login")) {
	    	String loginID="";
	    	client.setInfo(key, loginID);
	    }
	    else {
	    	this.sendToAllClients(msg);
	    }
	    
	    
	  String message = msg.toString();
	  if (message.startsWith("#")) {
		  String[] params = message.substring(1).split(" ");
		  if (params[0].equalsIgnoreCase("login") && params.length > 1) {
			  if (client.getInfo("username") == null) {
				  client.setInfo("username", params[1]);
			  } else {
				  try {
					  client.sendToClient(("Your username has already been set!"));
				  } catch (IOException e) {
					  //error sending
				  }
			  }
		  }
	  	} else {
	  		if (client.getInfo("username") == null) {
	  			try {
	  				client.sendToClient(("Please set a username before messaging the server!"));
	  				client.close();
	  			} catch (IOException e) {
	  				e.printStackTrace();
	  			}
	  		} else {
	  			System.out.println("Message received: " + msg + " from " +
	  					client.getInfo("username"));
	  			this.sendToAllClients((client.getInfo("username") + " > " + message));
	  		}
	  	}
  }
  
  
  
  /*if you want to read the information saved in the client
  we do client.getId(key) for return the login id*/
  
  
  
  protected void kickAllClients(){
	  Thread [] clientThreadList = getClientConnections();
	  for(int i=0; i<clientThreadList.length; i++){
		  try{
			  ((ConnectionToClient)clientThreadList[i]).close();
		  }
		  catch (Exception ex){
			  System.err.println("Error when disconnecting clients");
		  }
	  }
  }
  
  
  protected boolean isClosed(){
	  if(getNumberOfClients()==0 && !isListening()){
		  return true;
	  }
	  return false;
	  }
    
  //message from server user
  public void handleMessageFromServer(Object msg) {
	  System.out.println("SERVER MSG: " + msg);
	  this.sendToAllClients(msg);
  }
  
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  
  //I add
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("Client connected: "+client.toString());
  }
  
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println("Client disconnected: "+client.toString());
  }
  
  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
	  System.out.println("Client error: "+client.toString()+"exception"+exception.toString());
  }
  
//we create a method that implements the different commands for the server
  public void handleMessageFromServerConsole(String message) {
	  if (message.startsWith("#")) {
		  String[] parameters = message.split(" ");
		  String command = parameters[0];
		  switch (command) {
		  	case "#quit":
		  		//closes the server and then exits it
		  		try {
		  			this.close();
		  		} catch (IOException e) {
		  			System.exit(1);
		  		}
		  		System.exit(0);
		  		break;
		  	case "#stop":
		  		this.stopListening();
		  		break;
		  	case "#close":
		  		try {
		  			this.close();
		  		} catch (IOException e) {
		  			//error closing
		  		}
		  		break;
		  	case "#setport":
		  		if (!this.isListening() && this.getNumberOfClients() < 1) {
		  			super.setPort(Integer.parseInt(parameters[1]));
		  			System.out.println("Port set to " +
		  					Integer.parseInt(parameters[1]));
		  		} else {
		  			System.out.println("Can't do that now. Server is connected.");
		  		}
		  		break;
		  	case "#start":
		  		if (!this.isListening()) {
		  			try {
		  				this.listen();
		  			} catch (IOException e) {
		  				//error listening for clients
		  			}
		  		} else {
		  			System.out.println("We are already started and listening for clients!.");
		  		}
		  		break;
		  	case "#getport":
		  		System.out.println("Current port is " + this.getPort());
		  		break;
		  	default:
		  		System.out.println("Invalid command: '" + command+ "'");
		  		break;
		  }
	  } else {
		  this.sendToAllClients(message);
	  	}
  }

  
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
}
//End of EchoServer class
