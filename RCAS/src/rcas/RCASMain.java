package rcas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RCASMain extends Application {

	public static void main(String[] args) {
		launch(RCASMain.class);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(RCASMain.class.getResource("view/RCASMainView.fxml"));

		BorderPane mainPane = (BorderPane) fxmlLoader.load();

		primaryStage.setScene(new Scene(mainPane));
		primaryStage.setTitle("M120 Race Car Analysis Studio");
		primaryStage.setResizable(false);

		primaryStage.centerOnScreen();

		primaryStage.show();


	}

}
