// Gabrielle Capulong and Parth Vora - Software Methodology Fall 2020 
// Song Library Project - Assignment 1 Due October 2nd 2020

package songlib.app;

import java.util.*;
import java.util.Comparator;
import java.util.Scanner;
import java.io.File;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ButtonBar;
import javafx.stage.Stage;
import songlib.view.Entry;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;
import javafx.collections.*;

public class SongLib extends Application {

	@Override
	public void start(Stage primaryStage) 
	throws Exception {
		
		// launch GUI from songlib.fxml
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/songlib/view/songlib.fxml"));
		GridPane root = (GridPane)loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Song Library Viewer");
		primaryStage.setResizable(false);
		primaryStage.show();
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.jpg")));
		
	}


	public static void main(String[] args) {
		
		launch(args);
		
	}
	
}


