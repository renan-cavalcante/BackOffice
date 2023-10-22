package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
			ScrollPane scrollPane = loader.load();
			Scene mainScene = new Scene(scrollPane);
			
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Loja");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}