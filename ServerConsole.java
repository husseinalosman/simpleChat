import java.util.Scanner;

import client.*;
import common.*;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class ServerConsole implements ChatIF {

  final public static int DEFAULT_PORT = 5555;
  
  EchoServer server;
  
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ServerConsole(int port) 
  {
    try 
    {
      server = new EchoServer(port);
      
    } 
    catch(Exception exception) 
    {
      System.out.println("Error: Can't setup server!"
                + " Terminating server.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  public void accept() 
  {
    try
    {

      String message;

      while (true){
        message = fromConsole.nextLine();

        try{
            if(message.charAt(0) == '#'){
                if(message.equals("#quit")){
                    System.out.println("Closing the server.");
                    System.exit(0);
                    break;
                }else if(message.equals("#stop")){
                    server.stopListening();
                    server.sendToAllClients("WARNING - The server has stopped listening for connections.");
                }else if(message.equals("#close")){
                    server.close();
                }else if(message.charAt(1) == 's' && message.charAt(4) == 'p' && message.charAt(5) == 'o' && message.charAt(6) == 'r' && message.charAt(7) == 't'){
                    if(!server.isListening() && (server.getNumberOfClients() == 0)){
                        try{
                            message = message.replaceAll("<", "");
                            message = message.replaceAll(">", "");
                            message = message.replaceAll(" ", "");
                            message = message.replaceAll("#setport", "");
                            int port = Integer.parseInt(message);
                            server.setPort(port);
                            System.out.println("The host has been changed to " + port);
                        }catch (Exception e){
                        System.out.println("Invalid syntax, try \"#setport portNumber\" or \"#setport<portNumber>\" or \"#setport <portNumber>\"");
                        }
                    }else{
                        System.out.println("Error, can not change the port when the server is not closed.");
                    }
                }else if(message.equals("#start")){
                    if(!server.isListening() && (server.getNumberOfClients() == 0)){
                        server.listen();
                    }else{
                        System.out.println("Error, the server is already running.");
                    }
                }else if(message.equals("#getport")){
                    System.out.println("The port number is: " + server.getPort());
                }else{
                    System.out.println("Invalid command please input only: ");
                    System.out.println("#quit");
                    System.out.println("#stop");
                    System.out.println("#close");
                    System.out.println("#start");
                    System.out.println("#setport <portName>");
                    System.out.println("#getport");
                }
            }else{
                server.handleMessageFromServerUI(message);
            }
        }catch (Exception e){
            System.out.println("Fatal error has occured! Closing server.");
            System.exit(0);
            break;
        }
        
         
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  public void listen(){
    try 
    {
      server.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
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
    System.out.println("SERVER MSG> " + message);
  }
}
