package acsse.cs2b.gui;

import java.io.File;

import acsse.cs2b.model.ConnectingError;
import acsse.cs2b.model.ISmtp;
import acsse.cs2b.model.SmtpClient;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;


/**
 * the smtp client UI for interacting with the smtp server
 * @author Voldi Muyumba (222031434)
 * 
 * */
public class SmtpClientUI extends GridPane {
	
	//smtp server
   private ISmtp smtpClient;
   //texts
   private Text msg  = new Text();
   private Text connectionStatus;
   private Text attachmentName = new Text();
   
   //buttons
   private Button connectBtn = new Button("Connect");
   private Button sendBtn = new Button("Send mail");
   private Button attachementBtn = new Button("Add attatchment");
   //text areas
   private TextArea senderInput = new TextArea();
   private TextArea recipientInput = new TextArea();
   private TextArea subjectInput = new TextArea();
   private TextArea portInput = new TextArea();
   private TextArea hostInput = new TextArea();
   private TextArea bodyInput = new TextArea();
   
   //labels
   private Label senderLabel = new Label("Sender Name");
   private Label recipientLabel = new Label("Recipient Name");
   private Label subjectLabel = new Label("Subject");
   
   //file handlers
   File attachment;
   FileChooser fileChooser = new FileChooser();
   //default constructor
   
   /**
    * starts the ui with a default client
    *  */
   public SmtpClientUI() {
	   this(new SmtpClient()); 
   }
   
   /** 
    * a construtor to specify your own smtp server that implements ISmtp
    * @param client the smtp client we want to work with
    * */
   public SmtpClientUI(ISmtp client) {
	   
	    smtpClient = client;
	    connectionStatus = new Text(client != null && client.getConnected() ? "connected" : "not connected");
	    
	    fileChooser.setTitle("Select an attachment to send");
	    
	    hostInput.setPromptText("Enter host address or ip address");
	    hostInput.setPrefHeight(30);
	    hostInput.setPrefWidth(300);
	    
	    portInput.setPromptText("port");
	    portInput.setPrefHeight(30);
	    portInput.setPrefWidth(80);
	    
	    connectBtn.setPrefHeight(30);
	    connectBtn.setPrefWidth(100);
	    
	    sendBtn.setPrefHeight(30);
	    sendBtn.setPrefWidth(100);
	    
	    attachementBtn.setPrefHeight(30);
	    attachementBtn.setPrefWidth(150);
	
	    
	    add(hostInput, 0, 0);
	    add(portInput, 1,0);
	    add(connectBtn, 2,0);
	       
	    GridPane container = new GridPane();
		container.setVgap(10);
	 
	    add(connectionStatus, 3,0);
	    add(container, 0,2,4,1);
	    
	    senderInput.setPrefHeight(30);
	    senderInput.setPromptText("Sender Name");
	    container.add(senderLabel, 0,0);
	    container.add(senderInput, 1, 0,1,1);
	   
	    recipientInput.setPrefHeight(30);
	    recipientInput.setPromptText("Recipient name");
	    container.add(recipientLabel, 0,1);
	    container.add(recipientInput, 1, 1,1,1);
	   
	   subjectInput.setPrefHeight(30);
	   subjectInput.setPromptText("Email subject");
	   container.add(subjectLabel, 0,3);
	   container.add(subjectInput, 1, 3,1,1);
	   
	   bodyInput.setPromptText("Email body");
	   container.add(bodyInput, 0,4,4,1);
	   
	   add(msg,0,5);
	   add(sendBtn, 2,3);
	   add(attachmentName, 0, 4);
	   add(attachementBtn, 0,3);
	   
	   setPadding(new Insets(10,10,10,10));
	   setHgap(10);
	   setVgap(10);
	   
	   
	   
	   //attaching event listeners to the two buttons
	   connectBtn.setOnAction(e -> {
		   try {
			   String host =  hostInput.getText();
			   int port =Integer.parseInt(portInput.getText());
			   smtpClient.connect(host, port);
			   connectionStatus.setText("connected");
			   msg.setText("");
		   } catch(ConnectingError err) {
			   msg.setText(err.getMessage());  
		   } catch(Exception err) {
			  msg.setText("could not connect check your inputed text and try again");
		   }
	   });
	   
	   attachementBtn.setOnAction(e -> {
		   chooseFile();
	   });
	   sendBtn.setOnAction( e -> {
		   sendMail();
	   } );
	   
   }
   
   private void chooseFile() {
	 attachment = fileChooser.showOpenDialog(null);
	 if(attachment != null) {
		 attachmentName.setText("Attachment: " + attachment.getName());
	 }
	
}

/**
    * sends an email to the smtp server by delegating the work to the underlying smtp client
    * */
   private void sendMail() {
	   String senderName = senderInput.getText().strip();
	   String recieverName = recipientInput.getText().strip();
	   String subject = subjectInput.getText().strip();
	   String data = bodyInput.getText().strip();
	   
	   if(!smtpClient.getConnected()) {
		   msg.setText("connect first before sending an email");
		   return;
	   }
	   
	   if(senderName.length() > 0 && recieverName.length() > 0 && data.length() > 0) {
		   msg.setText("");
		   if(attachment == null ?  smtpClient.sendMail(senderName, recieverName, subject, data): smtpClient.sendMail(senderName, recieverName, subject, data, attachment)) { //delegation
			   //if email is sent then we clear the fields 
			   senderInput.setText("");
			   recipientInput.setText("");
			   subjectInput.setText("");
			   bodyInput.setText("");
			   connectionStatus.setText("not connected");
			   attachmentName.setText("");
			   attachment  = null;
		   } else {
			   //if the email was not sent
			   msg.setText("Could not send your email please try again leter");
		   }
	   } else {
		   msg.setText("make sure you have filled in all the required fields");
	   }
   }
   
}
