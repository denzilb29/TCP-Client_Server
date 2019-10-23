import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;



public class Server {

	private static int sPort = 8000;   //The server will be listening on this port number
	public static String FILE_TO_SEND = "";  // you may change this
	public static String FILE_TO_RECEIVED = "";
	public static int FILE_SIZE = 6022386;
	
	public static void main(String[] args) throws Exception {
		System.out.println("The server is running."); 
        	ServerSocket listener = new ServerSocket(sPort);
		int clientNum = 1;
        	try {
            		while(true) {
                		new Handler(listener.accept(),clientNum).start();
				System.out.println("Client "  + clientNum + " is connected!");
				clientNum++;
            			}
        	} finally {
            		listener.close();
					System.out.println("Client "  + clientNum + " is disconnected!");

        	} 
 
    	}

	/**
     	* A handler thread class.  Handlers are spawned from the listening
     	* loop and are responsible for dealing with a single client's requests.
     	*/
    	private static class Handler extends Thread {
        	private String message;    //message received from the client
			private String MESSAGE;    //uppercase message send to the client
			private Socket connection;
        	private ObjectInputStream in;	//stream read from the socket
        	private ObjectOutputStream out;    //stream write to the socket
			private int no;		//The index number of the client
			

        	public Handler(Socket connection, int no) {
            		this.connection = connection;
	    		this.no = no;
        	}

        public void run() {
 		try{
			//initialize Input and Output streams
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			try{
				while(true)
				{
					//receive the message sent from the client
					message = (String)in.readObject();
					String[] arr = new String[2];
					arr = message.split(" ");
					System.out.println(arr[0] + " " + arr[1]);

					System.out.println("After continue");
					FILE_TO_SEND = System.getProperty("user.dir") + File.separator + arr[1];  
					FILE_TO_RECEIVED = System.getProperty("user.dir")+ File.separator + arr[1];
					System.out.println("Receive message: " + arr[0] + " from client " + no);

				if(arr[0].compareTo("upload") == 0){
					System.out.println("Starting to execute A");
					a(message,connection); // upload	
				}
				else if(arr[0].compareTo("get") == 0){
					b(message,connection); // download
				}
				else{
					System.out.println("Ignore");
				}

					
				}
			}
			catch(ClassNotFoundException classnot){
					System.err.println("Data received in unknown format");
			}
			catch(IOException io){
			}
			
		}
		catch(IOException ioException){
			ioException.printStackTrace();
			System.out.println("Disconnect with Client " + no);
			System.out.println("Done1.");

		}
		finally{
			//Close connections
			try{
				//in.close();
				//out.close();
				connection.close();
			}
			catch(IOException ioException){
				System.out.println("Disconnect with Client " + no);
				System.out.println("Done2.");
			}
		}
	}

	//send a message to the output stream
	public void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("Send message: " + msg + " to Client " + no);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}

    }

    static void b(String msg, Socket connection) throws IOException{
    	System.out.println("In B");
	
	FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;
    Socket sock = connection;
        try {
          // send file
          File myFile = new File (FILE_TO_SEND);
          byte [] mybytearray  = new byte [(int)myFile.length()];
          fis = new FileInputStream(myFile);
          bis = new BufferedInputStream(fis);
          bis.read(mybytearray,0,mybytearray.length);	
          os = sock.getOutputStream();
          System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
          os.write(mybytearray,0,mybytearray.length);
          os.flush();
          System.out.println("Done.");
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        finally {
          if (bis != null) bis.close();
          if (os != null) os.close();
        }
      
    

    }



    static void a(String msg, Socket connection) throws IOException{
    	System.out.println(FILE_TO_RECEIVED+"In a server");
    	      System.out.println("In A server side before bytesRead");

		int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    Socket sock = connection;
    try {
      // receive file
      byte [] mybytearray  = new byte [FILE_SIZE];
      InputStream is = sock.getInputStream();
      fos = new FileOutputStream(FILE_TO_RECEIVED);
      bos = new BufferedOutputStream(fos);

      bytesRead = is.read(mybytearray,0,mybytearray.length);
      System.out.println("In A server side after bytesRead");
      current = bytesRead;

      do {
         bytesRead =
            is.read(mybytearray, current, (mybytearray.length-current));
         if(bytesRead >= 0) current += bytesRead;
      } while(bytesRead > -1);

      //if(current>-1){
      	bos.write(mybytearray, 0 , current);
     	bos.flush();
      	System.out.println("File " + FILE_TO_RECEIVED+ " downloaded (" + current + " bytes read)");
      //}
    }
    finally {
      if (fos != null) fos.close();
      if (bos != null) bos.close();
    }
	}

}
