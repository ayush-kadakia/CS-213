/*
 * Names: Allen Estrada(are55), Ayush Kadakia(aak227)
 */
package app;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import view.ListController;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class SongLib extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		// TODO Auto-generated method stub
				FXMLLoader loader = new FXMLLoader();   
				loader.setLocation(getClass().getResource("/view/SongLib.fxml"));
				AnchorPane root = (AnchorPane)loader.load();
				ListController listController = 
						loader.getController();
				listController.start(primaryStage);

				Scene scene = new Scene(root);
				primaryStage.setTitle("Song Library");
				primaryStage.setScene(scene);
				primaryStage.show(); 
	}
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void stop() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("songs.txt"))) {
			for(Song s: ListController.obsList) {
				bw.write(s.toCSV());
				bw.write("\n");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
