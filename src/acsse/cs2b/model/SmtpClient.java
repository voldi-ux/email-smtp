package acsse.cs2b.model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Base64;


/**
 * The smtp client program used to connect and interact with the smtp server
 * @author Voldi Muyumba (222031434)
 * */
public class SmtpClient implements ISmtp {
     private boolean connected = false;
     private int port;
     private String host;
     private Socket smtpServer;
     private BufferedReader reader;
     private PrintWriter writer;
     
     /**
      * default constructor called if you don't have the host's information
      */
     public SmtpClient() {
       	 
     }
     
     
     /**
      * the underlying server will connect the provided host and port
      * @param host the host 
      * @param port the port number to connect to
       *@throws ConnectingError thrown if the client can not connect to the server 
      * */
     public SmtpClient(String host, int port) throws ConnectingError {
    	 connect(host, port);
     }
     
     /** 
      * connects to the smtpServer
      * @param host the host to connect to
      * @param port the port number to connect to
      * @throws ConnectingError thrown if the client can not connect to the server
      * */
	@Override
	public void connect(String host, int port) throws ConnectingError {
	   try {
		   if(!connected) {
			   smtpServer = new Socket(host, port);
			   writer = new PrintWriter(smtpServer.getOutputStream(), true);
			   reader = new BufferedReader(new InputStreamReader(smtpServer.getInputStream()));
	           connected = true; // if the code reach this point we assume that we have connected successfully.
			   
				 System.out.println(reader.readLine());
			     System.out.println(sendSmptCommnad("HELO ", host)); // initiating a session
		   } 
		   
	   } catch (UnknownHostException e) {
		   throw new ConnectingError("could not connect to the smtp service");
	   } catch (IOException e) {
		   throw new ConnectingError("could not connect to the smtp service");
	}
		
	}

	/** 
	 * Sends a command to the SMTP server
	 * @param command the command 
	 * @param data the payload of the command
	 * @return int a number indicating whether the command was successful or not
	 * */
	@Override
	public int sendSmptCommnad(String command, String data) throws NumberFormatException {
		if(connected && writer != null) {
			 writer.println(command + data);
			 return Integer.parseInt(readResCode(reader)); // reading the response code after each and every command
		} 
		
		// -1 to indicate that something went wrong on client side
		return -1;
	}
	
	/** 
	 * Sends a command to the smtp server
	 * @param command the command 
	 * @return int a number indicating whether the command was successful or not
	 * */
	@Override
	public int sendSmptCommnad(String command) {
		if(connected && writer != null) {
			System.out.println(command);
			 writer.println(command);
			 return Integer.parseInt(readResCode(reader)); // reading the response code after each and every command
		} 
		
		// -1 to indicate that something went wrong on client side
		return -1;
	}
	
  
	/** 
	 * reads the response code from the socket stream
	 * @param reader the stream to read from
	 * @return String response code
	 * */
   private String readResCode(BufferedReader reader) {
	   StringBuffer res = new StringBuffer();
	     int value;
	     try {
			while( (value = reader.read()) != -1 ) {
				 if((char)value == ' ') {
					 
					 reader.readLine(); // reading and discarding the remaining bytes
					
					 break;
				 }
				 res.append((char)value);		 
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
	     
	     
	   return res.toString();
   }

   /**
    * return the status of the socket ie if it is connected or not
    * @return boolean
    * 
    * */
@Override
public boolean getConnected() {
	return connected;
}



/**
 * sends an email to the smtp server
 * @param senderName the sender
 * @param recieverName the person receiving the email
 * @param subject the subject of the email
 * @param data the email content or body
 * @return boolean indicating whether the email was sent or not
 * */
@Override
public  boolean sendMail(String senderName, String recieverName, String subject, String data) {
	
	   //sending command to the server to initiate the sending of the email
	   sendSmptCommnad("MAIL FROM:<", senderName + "@csc2b.uj.ac.za>");
	   sendSmptCommnad("RCPT TO:<", recieverName + "@csc2b.uj.ac.za>");
	   sendSmptCommnad("DATA");
	   
	   //writing the email to the server (these are not commands)
	   writer.println("From: " + senderName + "@csc2b.uj.ac.za" ); 
	   writer.println("To: " + recieverName + "@csc2b.uj.ac.za" );
	   writer.println("Subject: " + subject);
	   writer.println();
	   writer.println(data);
	   writer.println(".");
	   
	    if( Integer.parseInt(readResCode(reader)) == 250) {
	    	sendSmptCommnad("QUIT"); // close the session
	    	connected   = false;
	    	freeResources(); // release all the resources
	    	return true;
	    }
	
	   return false;
}


private void freeResources( ) {
		try {
			if(smtpServer != null && smtpServer.isConnected()) {
			    smtpServer.close();
			}
			if(writer != null) 
				writer.close();
			if(reader != null)
				reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}


@Override
public boolean sendMail(String senderName, String recieverName, String subject, String data, File attachment) {
	//sending command to the server to initiate the sending of the email
	   sendSmptCommnad("MAIL FROM:<", senderName + "@csc2b.uj.ac.za>");
	   sendSmptCommnad("RCPT TO:<", recieverName + "@csc2b.uj.ac.za>");
	   sendSmptCommnad("DATA");
	   
	   //writing the email to the server (these are not commands)
	   writer.println("From: " + senderName + "@csc2b.uj.ac.za" ); 
	   writer.println("To: " + recieverName + "@csc2b.uj.ac.za" );
	   writer.println("Subject: " + subject);
	   writer.println("MIME-Version: 1.0");
       writer.println("Content-Type: multipart/mixed; boundary=mime-boundary");
	   // Start the message body
       writer.println();
       writer.println("--mime-boundary");
       writer.println("Content-Type: text/plain; charset=UTF-8");
       writer.println();
       writer.println(data);
       writer.println();
       addAttachment(attachment); // attach the file to the email in mime
	   writer.println("--mime-boundary--"); // end of the email
	   writer.println(".");
	   
	    if( Integer.parseInt(readResCode(reader)) == 250) {
	    	sendSmptCommnad("QUIT"); // close the session
	    	connected   = false;
	    	freeResources(); // release all the resources
	    	return true;
	    }
	return false;
}

  private void addAttachment(File attachment) {
	  writer.println("--mime-boundary");
      writer.println("Content-Type: application/octet-stream; name=" + attachment.getName());
      writer.println("Content-Disposition: attachment; filename=" + attachment.getName());
      writer.println("Content-Transfer-Encoding: base64");
      
      writer.println();      
      try (FileInputStream reader = new FileInputStream(attachment)) {
          String line;
          byte[] buffer = new byte[1024];
          int byteRead;
          while ((byteRead = reader.read(buffer)) != -1) {
              byte[] encodedLine = Base64.getEncoder().encode(Arrays.copyOfRange(buffer, 0,byteRead));
              writer.println(new String(encodedLine));
          }
      } catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	
      writer.println();
  }
  }


}
