package acsse.cs2b.model;

import java.io.File;

/**
 * An smtp interface
 * */
public interface ISmtp {
	  /** 
     * connects to the smtpServer
     * @param host the host to connect to
     * @param port the port number to connect to
     * @throws ConnectingError thrown if the client can not connect to the server
     * */
   void connect(String host , int port) throws ConnectingError;
   /** 
	 * Sends a command to the SMTP server
	 * @param command the command 
	 * @param data the payload of the command
	 * @return int a number indicating whether the command was successful or not
	 * */
   int sendSmptCommnad(String command, String data);
   /** 
	 * Sends a command to the smtp server
	 * @param command the command 
	 * @return int a number indicating whether the command was successful or not
	 * */
   int sendSmptCommnad(String command);
   /**
    * return the status of the socket ie if it is connected or not
    * @return boolean
    * 
    * */
   boolean getConnected();
   /**
    * sends an email to the smtp server
    * @param senderName the sender
    * @param recieverName the person receiving the email
    * @param subject the subject of the email
    * @param data the email content or body
    * @return boolean indicating whether the email was sent or not
    * */
   boolean sendMail(String senderName, String recieverName, String subject, String data);
   
   /**
    * sends an email to the smtp server
    * @param senderName the sender
    * @param recieverName the person receiving the email
    * @param subject the subject of the email
    * @param data the email content or body
    * @param attachment the file to attach to the email
    * @return boolean indicating whether the email was sent or not
    * */
   boolean sendMail(String senderName, String recieverName, String subject, String data, File attachment);
}
