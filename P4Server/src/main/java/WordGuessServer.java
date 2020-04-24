import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;

import java.net.ServerSocket;
import java.util.HashMap;

public class WordGuessServer extends Application {
	Button startBtn, startServerBtn;
	TextField portNumTF;
	ListView<String> listItems;
	Server serverConnection;
	HashMap<String, Scene> sceneMap;
	Alert emptyPortError, wrongPortError;
	ServerSocket socket;
	boolean connected;



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	// feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		//Variables
		sceneMap = new HashMap<String, Scene>();
		listItems = new ListView<String>();
		//Alerts
		emptyPortError = new Alert(Alert.AlertType.INFORMATION);
		emptyPortError.setTitle("Incorrect Port Info");
		emptyPortError.setHeaderText(null);
		emptyPortError.setContentText("You have not entered a port.");
		wrongPortError = new Alert(Alert.AlertType.INFORMATION);
		wrongPortError.setTitle("Incorrect Port Info");
		wrongPortError.setHeaderText(null);
		wrongPortError.setContentText("The port you have entered cannot be used to host at.");

		//Add scenes to the map
		sceneMap.put("Start", createStartScene());
		sceneMap.put("Port", createPortScene());
		sceneMap.put("Game", createGameScene());

		startBtn.setOnAction(e -> {
			primaryStage.setScene(sceneMap.get("Port"));
		});

		startServerBtn.setOnAction(e-> {
			if(portNumTF.getText().isEmpty()){
				emptyPortError.showAndWait();
			}
			else {
				try{
					socket = new ServerSocket(Integer.parseInt(portNumTF.getText()));
					connected = true;
				}
				catch (Exception a) {
					connected = false;
					wrongPortError.showAndWait();
					portNumTF.setText("");
				}

				if(connected == true){
					serverConnection = new Server(data -> {
						Platform.runLater(() -> {
							listItems.getItems().add(data.toString());});
					}, socket);
					primaryStage.setScene(sceneMap.get("Game"));
				}
			}
		});



		portNumTF.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				portNumTF.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});



		primaryStage.setTitle("(Server) Playing word guess!!!");
		primaryStage.setScene(sceneMap.get("Start"));
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
		primaryStage.show();
	}


	public Scene createStartScene() {
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(325,0,0,0));
		String style = "-fx-background-image: url('Images/bgWordsInMiddle.jpg'); "  +
				"-fx-background-size: 100% 100%;";
		pane.setStyle(style);
		startBtn = new Button("Start");
		startBtn.setFont(Font.font("Arial", 30));
		VBox center = new VBox(startBtn);
		center.setAlignment(Pos.TOP_CENTER);
		pane.setCenter(center);
		return new Scene(pane, 700, 500);
	}

	public Scene createPortScene(){
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		String style = "-fx-background-image: url('Images/bgWordsAtTop.jpg'); "  +
				"-fx-background-size: 100% 100%;";
		pane.setStyle(style);
		portNumTF = new TextField();
		Text enterPortText = new Text("Enter a port number to host at");
		Text portText = new Text(" Port:");
		enterPortText.setFont(Font.font("Arial", 30));
		portText.setFont(Font.font("Arial", 30));
		startServerBtn = new Button("Start Server");
		startServerBtn.setFont(Font.font("Arial", 30));
		HBox portInfo = new HBox(5, portText, portNumTF);
		portInfo.setAlignment(Pos.BOTTOM_CENTER);
		VBox center = new VBox(10, enterPortText,portInfo, startServerBtn);
		center.setAlignment(Pos.CENTER);
		pane.setCenter(center);
		return new Scene(pane, 700, 500);
	}

	public Scene createGameScene(){
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(120,70,70,70));
		String style = "-fx-background-image: url('Images/bgWordsAtTop.jpg'); "  +
				"-fx-background-size: 100% 100%;";
		pane.setStyle(style);
		pane.setCenter(listItems);
		return new Scene(pane, 700, 500);
	}

}
