// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  boolean isloggedin;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String loginid, String host, int port) 
  {
    try 
    {
      client= new ChatClient(loginid, host, port, this);
      isloggedin = true;
      
      
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
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
        if(message.charAt(0) == '#'){
          if(message.equals("#quit")){
            client.quit();
          }else if(message.equals("#logoff")){
            client.closeConnection();
            isloggedin = false;
          }else if(message.charAt(1) == 's' && message.charAt(4) == 'h' && message.charAt(5) == 'o' && message.charAt(6) == 's' && message.charAt(7) == 't'){
            if(!client.isConnected()){
              
              boolean space = false;
              boolean carrot = false;
              
              for(int i = 0; i < message.length(); i++){
                if(message.charAt(i) == ' '){
                  space = true;
                }
                if(message.charAt(i) == '<'){
                  carrot = true;
                }
              }

              if(space && carrot){
                String[] tmp = message.split("<");
                String host = tmp[1];
                client.setHost(host);
                host = host.replaceAll(">", "");
                System.out.println("Host set to: " + host);
              }else if(space && !carrot){
                String[] tmp = message.split(" ");
                String host = tmp[1];
                client.setHost(host);
                System.out.println("Host set to: " + host);
              }else if(!space && carrot){
                String[] tmp = message.split("<");
                String host = tmp[1];
                client.setHost(host);
                host = host.replaceAll(">", "");
                System.out.println("Host set to: " + host);
              }else{
                System.out.println("Invalid syntax, try \"#sethost hostName\" or \"#sethost<hostName>\" or \"#sethost <hostName>\"");
              }
            }else{
              System.out.println("Error, can not change host while logged in.");
            }
          }else if(message.charAt(1) == 's' && message.charAt(4) == 'p' && message.charAt(5) == 'o' && message.charAt(6) == 'r' && message.charAt(7) == 't'){
            if(!client.isConnected()){
              try{
                message = message.replaceAll("<", "");
                message = message.replaceAll(">", "");
                message = message.replaceAll(" ", "");
                message = message.replaceAll("#setport", "");
                int port = Integer.parseInt(message);
                client.setPort(port);
                System.out.println("Port set to: " + port);
              }catch (Exception e){
                System.out.println("Invalid syntax, try \"#setport portNumber\" or \"#setport<portNumber>\" or \"#setport <portNumber>\"");
              }
            }else{
              System.out.println("Error, can not change port while logged in.");
            }
          }else if(message.charAt(1) == 'l' && message.charAt(2) == 'o' && message.charAt(3) == 'g' && message.charAt(4) == 'i' && message.charAt(5) == 'n'){
            if(isloggedin){
              System.out.println("You must logoff first!");
            }else{
              message = message.replaceAll("#login", "");
              message = message.replaceAll(" ", "");
              
              if(message.equals("")){
                System.out.println("Error, username missing!");
              }else{
                client.loginid = message;
                client.openConnection();
                client.sendToServer("#login <" + message +">");
              }
            }
          }else if(message.equals("#gethost")){
            System.out.println(client.getHost());
          }else if(message.equals("#getport")){
            System.out.println(client.getPort());
          }else{
            System.out.println("Invalid command please input only: ");
            System.out.println("#quit");
            System.out.println("#logoff");
            System.out.println("#login <username>");
            System.out.println("#sethost <hostName>");
            System.out.println("#setport <portName>");
            System.out.println("#gethost");
            System.out.println("#getport");
          }
        }else{
          client.handleMessageFromClientUI(message);
        }
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println(message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String loginid = "";
    String host = "";
    int port = 0;
    try{
      if(args[0] == null){
        System.out.println("ERROR - No login ID specified.  Connection aborted.");
        System.exit(0);   
      }
    }catch (ArrayIndexOutOfBoundsException e){
      System.out.println("ERROR - No login ID specified.  Connection aborted.");
      System.exit(0);
    }
    
    try
    {

      loginid = args[0];
      System.out.println("login id: " + loginid);
      
      if(args[1] == null){
        System.out.println("The port was not specified, now using defult port: 5555");
        ClientConsole chat= new ClientConsole(loginid, host, DEFAULT_PORT);
        chat.accept();  //Wait for console data
      }else{
        host = args[1];
        System.out.println("host: " + args[1]);
        System.out.println("port: " + args[2]);
        port = Integer.parseInt(args[2]);
        ClientConsole chat= new ClientConsole(loginid, host, port);
        chat.accept();  //Wait for console data
      }
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      System.out.println("No host nor port was specified, new using localhost and defult port: 5555");
      host = "localhost";
      ClientConsole chat= new ClientConsole(loginid, host, DEFAULT_PORT);
      chat.accept();  //Wait for console data
    }
  }
}
//End of ConsoleChat class
