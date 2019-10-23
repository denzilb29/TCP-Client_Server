import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	Socket requestSocket;           //socket connect to the server
	ObjectOutputStream out;         //stream write to the socket
 	ObjectInputStream in;          //stream read from the socket
	String message;                //message send to the server
	String MESSAGE;     
	String command;           //capitalized message read from the server

	FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    //Socket sock = connection;
    InputStream is = null;
    //InputStream is = requestSocket.getInputStream();

    FileInputStream fis = null;
   	BufferedInputStream bis = null;
    OutputStream os = null;
    //Socket sock = requestSocket;

    private String sourceDirectory = "C:\\Users\\denzi\\Desktop\\MFT\\Server\\";
	private int fileCount = 0;
	private FileEvent fileEvent = null;

  	public static int FILE_SIZE = 60222386; 
  	public static String FILE_TO_RECEIVED = "";  
	public static String FILE_TO_SEND = ""; 
       

	public void Client() {}

	public void run(String ip, int port)
	{
		try{
			//create a socket to connect to the server
			requestSocket = new Socket(ip, port);
			System.out.println("Connected to " + ip + " in port" + port);
			// initialize inputStream and outputStream
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			//get Input from standard input
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			while(true)
			{
				System.out.print("Hello, please input a sentence: ");
				//read a sentence from the standard input
				message = bufferedReader.readLine();


				String[] arr = new String[3];
				arr = message.split(" ");
				FILE_TO_SEND =System.getProperty("user.dir") + File.separator + arr[1];  
				FILE_TO_RECEIVED = System.getProperty("user.dir") + File.separator + arr[1];
				
				sendMessage(message);
				System.out.println("1."+message);


				if(arr[0].compareTo("upload") == 0){

					if(arr[1].compareTo("c1.txt")!=0){
						
						System.out.println("Enter the correct filename");
						


						}
						else{
							//System.out.println("Hello : In else ");
					b(message,requestSocket); // function call for upload	
				}
						}
					
				else if(arr[0].compareTo("get") == 0){
					
						
						
						if(arr[1].compareTo("s.txt")!=0){
						
						System.out.println("Enter the correct filename");
						


						}
						else{
							//System.out.println("Hello : In else ");
					a(message,requestSocket); // function call for download
						}
					
				}
				else if(arr[0].compareTo("dir") == 0){

					locateFiles(); // dir
				} else if(arr[0].compareTo("ftp") == 0){

					System.out.println("Client already connected");
					continue;
					//locateFiles(); // dir
				}


				else{
				 	System.out.println("You entered a wrong commands");
				}


			}
		}
		catch (ConnectException e) {
    			System.err.println("Connection refused. You need to initiate a server first.");
		} 
		// catch ( ClassNotFoundException e ) {
  //           		System.err.println("Class not found");
  //       	} 
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//Close connections
			try{
				//in.close();
				//out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}

	public void a(String msg, Socket connection) throws IOException{
		//System.out.println("In a");
	int bytesRead;
    int current = 0;
    // FileOutputStream fos = null;
    // BufferedOutputStream bos = null;

    try {
      // receive file
      byte [] mybytearray  = new byte [FILE_SIZE];
            //System.out.println("In Try Before IS");

      InputStream is = connection.getInputStream();

            //System.out.println("In Try After IS");

      fos = new FileOutputStream(FILE_TO_RECEIVED);
      bos = new BufferedOutputStream(fos);

                  //System.out.println("In Try Before bytesRead");

      bytesRead = is.read(mybytearray,0,mybytearray.length);

                  //System.out.println("In Try After bytesRead");

      current = bytesRead;

     // System.out.println("In Try Before Do");

      do {
      		//System.out.println(" In a() do ");
         bytesRead =
            is.read(mybytearray, current, (mybytearray.length-current));
         if(bytesRead >= 0) current += bytesRead;
      } while(bytesRead > -1);

      bos.write(mybytearray, 0 , current);
      bos.flush();
      System.out.println("File " + FILE_TO_RECEIVED
          + " downloaded (" + current + " bytes read)");
    }

    finally {
      if (fos != null) fos.close();
      if (bos != null) bos.close();
      //if (sock != null) sock.close();
    }
	}
	//send a message to the output stream

	public  void b(String msg, Socket requestSocket) throws IOException{
    
    //System.out.println("In B");
	//FileInputStream fis = null;
 	//BufferedInputStream bis = null;
 	//OutputStream os = null;
    Socket sock = requestSocket;
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
          System.out.println("Done uploading.");
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        finally {
          if (bis != null) bis.close();
          if (os != null) os.close();
        }
      
    

    }


    public void locateFiles() {
		File srcDir = new File(sourceDirectory);
		if (!srcDir.isDirectory()) {
			System.out.println("Source directory is not valid ..Exiting the client");
			System.exit(0);

		}
		
		File[] files = srcDir.listFiles();
		fileCount = files.length;
		if (fileCount == 0) {
			System.out.println("Empty directory ..Exiting the client");
			System.exit(0);
			
		}

		for (int i = 0; i < fileCount; i++) { 
			System.out.println("Found " + files[i].getAbsolutePath()); 
			
		} 
	} 


	void sendMessage(String msg)
	{
		try{
			//stream write the message
			out.writeObject(msg);
			out.flush();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	//main method
	public static void main(String args[])
	{
		

		System.out.println("Enter the username and password");
		Scanner input = new Scanner(System.in);
		
		for(;;){
				if(input.next().compareTo("c1") == 0 || input.next().compareTo("123") == 0){
					//break;
					System.out.println("Logged in");
					break;
				}	

				System.out.println("Wrong creds");

		}

		

		Client client = new Client();
		System.out.println("Enter the ftpclient<IP port> command");
		//Scanner input = new Scanner(System.in);
		input = new Scanner(System.in);
		String command = input.next();
		String ip = input.next();
		int port = Integer.parseInt(input.next());

		for(;;){
				
				if(command.compareTo("ftp")==0 && ip.compareTo("localhost")==0 && port == 8000){ 
			
					//System.out.println("Wrong command entered.");
					break;
				}	

				System.out.println("You enetered wrong command / IP/ port. Enter details again ");
				input = new Scanner(System.in);
				command = input.next();
				ip = input.next();
				port = Integer.parseInt(input.next());

		}

		
		client.run(ip,port);
	}

}
