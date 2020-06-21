package graphique;

import application.Parse;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.*;

import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage){
		try {
			Parse.getDataFromCSVFile("data/tempanomaly_4x4grid.csv");
			Parent content = FXMLLoader.load(getClass().getResource("app.fxml"));
			primaryStage.setTitle("Temperature Anomaly");
			
			primaryStage.setScene(new Scene(content));
			primaryStage.show();
		}
		catch( Exception e) {
			e.printStackTrace();
		}
		
	}
}
