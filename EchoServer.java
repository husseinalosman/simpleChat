// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


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
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client){
    System.out.println("Message received: " + msg + " from " + client);
    String msgStr=(String)msg;
    if(msgStr.startsWith("#login")) {
    	String loginID="";
    	client.setInfo(key, loginID);
    }
    else {
    	this.sendToAllClients(msg);
    }
  }
  
  /*if you want to read the information saved in the client
  we do client.getId(key) for return the login id*/
  
    
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
