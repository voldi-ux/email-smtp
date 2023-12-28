import acsse.cs2b.gui.SmtpClientUI;
import acsse.cs2b.model.SmtpClient;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {		
		SmtpClientUI clientUI = new SmtpClientUI();
         
		Scene scene = new Scene(clientUI, 600,500);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

}
