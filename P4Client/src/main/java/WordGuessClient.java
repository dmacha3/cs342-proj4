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
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;




public class WordGuessClient extends Application {
	HashMap<String, Scene> sceneMap;
	ListView<String> listItems;
	Client clientConnection;
	boolean connected;
	Socket socket;
	Button startBtn, startClientBtn;
	Button catOneBtn,  catTwoBtn, catThreeBtn;
	Button sendGuessBtn;
	TextField portNumTF, ipTF, guessTF;
	Alert emptyPortError, emptyIPError, wrongPortOrIPError;



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Variables
		sceneMap = new HashMap<String, Scene>();
		listItems = new ListView<>();
		//Port Alert
		emptyPortError = new Alert(Alert.AlertType.INFORMATION);
		emptyPortError.setTitle("Incorrect Port Info");
		emptyPortError.setHeaderText(null);
		emptyPortError.setContentText("You have not entered a port.");

		//IP Alert
		emptyIPError = new Alert(Alert.AlertType.INFORMATION);
		emptyIPError.setTitle("Incorrect IP Address Info");
		emptyIPError.setHeaderText(null);
		emptyIPError.setContentText("You have not entered an IP Address.");

		//IP and Port Alert
		wrongPortOrIPError  = new Alert(Alert.AlertType.INFORMATION);
		wrongPortOrIPError.setTitle("Incorrect IP Address or Port Info");
		wrongPortOrIPError.setHeaderText(null);
		wrongPortOrIPError.setContentText("The IP Address or Port you have entered cannot be connected to.");

		sceneMap.put("Start", createStartScene());
		sceneMap.put("Port", createPortAndIPScene());
		sceneMap.put("Categories", createCategoryScene());
		sceneMap.put("Game", createGameScene());


		startBtn.setOnAction(e -> {
			primaryStage.setScene(sceneMap.get("Port"));
		});

		startClientBtn.setOnAction(e -> {
			if(portNumTF.getText().isEmpty()){
				emptyPortError.showAndWait();
			}
			if(ipTF.getText().isEmpty()){
				emptyIPError.showAndWait();
			}
			else{
				try{
					socket = new Socket(ipTF.getText(), Integer.parseInt(portNumTF.getText()));
					connected = true;
				}
				catch (Exception a){
					connected = false;
					wrongPortOrIPError.showAndWait();
					portNumTF.setText("");
					ipTF.setText("");
				}
				if(connected == true){
					clientConnection = new Client(data -> Platform.runLater(()-> listItems.getItems().add(data.toString())),socket);
					clientConnection.start();
					primaryStage.setScene(sceneMap.get("Categories"));
				}
			}
		});

		catOneBtn.setOnAction(e -> {
			primaryStage.setScene(sceneMap.get("Game"));
		});

		catTwoBtn.setOnAction(e -> {
			primaryStage.setScene(sceneMap.get("Game"));
		});

		catThreeBtn.setOnAction(e ->{
			primaryStage.setScene(sceneMap.get("Game"));
		});

		portNumTF.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				portNumTF.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});

		guessTF.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\w*")) {
				guessTF.setText(newValue.replaceAll("[^\\w]", ""));
			}
		});

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});


		// TODO Auto-generated method stub
		primaryStage.setTitle("(Client) Playing word guess!!!");
		primaryStage.setScene(sceneMap.get("Start"));
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
		primaryStage.show();
	}



	public Scene createStartScene() {
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(400,0,0,0));
		String style = "-fx-background-image: url('Images/bgWordsInMiddle.jpg'); "  +
				"-fx-background-size: 100% 100%;";
		pane.setStyle(style);
		startBtn = new Button("Start");
		startBtn.setFont(Font.font("Arial", 30));
		VBox center = new VBox(startBtn);
		center.setAlignment(Pos.TOP_CENTER);
		pane.setCenter(center);
		return new Scene(pane, 800, 600);
	}

	public Scene createPortAndIPScene(){
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		String style = "-fx-background-image: url('Images/bgWordsAtTop.jpg'); "  +
				"-fx-background-size: 100% 100%;";
		pane.setStyle(style);
		portNumTF = new TextField();
		ipTF = new TextField();
		Text enterIPText = new Text("Enter an IP Address");
		Text ipText = new Text(" IP Address:");
		Text enterPortText = new Text("Enter a port number");
		Text portText = new Text(" Port:");
		enterIPText.setFont(Font.font("Arial", 30));
		ipText.setFont(Font.font("Arial", 30));
		enterPortText.setFont(Font.font("Arial", 30));
		portText.setFont(Font.font("Arial", 30));
		startClientBtn = new Button("Start Client");
		startClientBtn.setFont(Font.font("Arial", 30));
		HBox portInfo = new HBox(5, portText, portNumTF);
		HBox ipInfo = new HBox(5, ipText, ipTF);
		ipInfo.setAlignment(Pos.CENTER);
		portInfo.setAlignment(Pos.CENTER);
		VBox center = new VBox(10, enterIPText, ipInfo, enterPortText ,portInfo, startClientBtn);
		center.setAlignment(Pos.CENTER);
		pane.setCenter(center);
		return new Scene(pane, 800, 600);
	}

	public Scene createCategoryScene(){
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		String style = "-fx-background-image: url('Images/bgWordsAtTop.jpg'); "  +
				"-fx-background-size: 100% 100%;";
		pane.setStyle(style);
		Text pickCatText = new Text("Please pick a category");
		pickCatText.setFont(Font.font("Arial", 30));
		catOneBtn = new Button("Animals");
		catTwoBtn = new Button("Countries");
		catThreeBtn = new Button("Super Heroes");
		catOneBtn.setFont(Font.font("Arial", 30));
		catTwoBtn.setFont(Font.font("Arial", 30));
		catThreeBtn.setFont(Font.font("Arial", 30));
		HBox categories = new HBox(20, catOneBtn, catTwoBtn, catThreeBtn);
		categories.setAlignment(Pos.CENTER);
		VBox center = new VBox(10, pickCatText, categories);
		center.setAlignment(Pos.CENTER);
		pane.setCenter(center);
		return new Scene(pane, 800, 600);
	}

	public Scene createGameScene(){
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		String style = "-fx-background-image: url('Images/bgNoWords.jpg'); "  +
				"-fx-background-size: 100% 100%;";
		pane.setStyle(style);

		//Text and Buttons
		Text makeGuessText = new Text("Please make a guess");
		Text lettersGuessedInfoText = new Text("Letters Guessed Correct");
		Text allLettersGuessedInfoText = new Text("All Letters Guessed");
		Text guessesLeft = new Text("Guesses Left");
		Text guessLeftNum = new Text("6");
		Text categoryCurrText = new Text("Current Category");
		Text catChose = new Text("Animals");
		sendGuessBtn = new Button("Make Guess");
		guessTF = new TextField();

		//Setting fonts
		makeGuessText.setFont(Font.font("Arial", 30));
		lettersGuessedInfoText.setFont(Font.font("Arial", 30));
		allLettersGuessedInfoText.setFont(Font.font("Arial", 30));
		guessesLeft.setFont(Font.font("Arial", 30));
		guessLeftNum.setFont(Font.font("Arial", 30));
		categoryCurrText.setFont(Font.font("Arial", 30));
		catChose.setFont(Font.font("Arial", 30));
		sendGuessBtn.setFont(Font.font("Arial", 30));

		//Arraylist for letters guessed and all letters
		ArrayList<Text> lettersGuessedCorrectTextArr = new ArrayList<>();
		ArrayList<Text> guessAllTextArr = new ArrayList<>();


		//Hard code for letters guessed correctly
		for(int i = 0; i < 6; i++){
			lettersGuessedCorrectTextArr.add(new Text("-"));
		}

		for(int i = 0; i < lettersGuessedCorrectTextArr.size(); i++){
			lettersGuessedCorrectTextArr.get(i).setFont(Font.font("Arial", 30));
		}

		HBox guessCorrect = new HBox(5);
		for(int i = 0; i <  lettersGuessedCorrectTextArr.size(); i++){
			guessCorrect.getChildren().add(lettersGuessedCorrectTextArr.get(i));
		}
		guessCorrect.setAlignment(Pos.CENTER);

		//Hard code for all letters
		for(int i = 0; i < 26; i++){
			guessAllTextArr.add(new Text(Character.toString((char)(i+65))));
		}
		for(int i =0; i < guessAllTextArr.size(); i++){
			guessAllTextArr.get(i).setFont(Font.font("Arial", 24));
		}
		//All letters in hboxes
		HBox firstLine = new HBox(5, guessAllTextArr.get(0),guessAllTextArr.get(1),guessAllTextArr.get(2),guessAllTextArr.get(3),guessAllTextArr.get(4));
		HBox secondLine = new HBox(5, guessAllTextArr.get(5),guessAllTextArr.get(6),guessAllTextArr.get(7),guessAllTextArr.get(8),guessAllTextArr.get(9));
		HBox thirdLine = new HBox(5, guessAllTextArr.get(10),guessAllTextArr.get(11),guessAllTextArr.get(12),guessAllTextArr.get(13),guessAllTextArr.get(14));
		HBox fourthLine = new HBox(5, guessAllTextArr.get(15),guessAllTextArr.get(16),guessAllTextArr.get(17),guessAllTextArr.get(18),guessAllTextArr.get(19));
		HBox fifthLine = new HBox(5, guessAllTextArr.get(20),guessAllTextArr.get(21),guessAllTextArr.get(22),guessAllTextArr.get(23),guessAllTextArr.get(24));
		HBox sixthLine = new HBox(5, guessAllTextArr.get(25));

		//Vboxes for the top info
		VBox leftTop = new VBox(5, categoryCurrText, catChose);
		leftTop.setAlignment(Pos.CENTER);
		VBox rightTop = new VBox(10, guessesLeft, guessLeftNum);
		rightTop.setAlignment(Pos.CENTER);
		VBox middleTop = new VBox(10, lettersGuessedInfoText, guessCorrect);
		middleTop.setAlignment(Pos.CENTER);
		HBox top = new HBox(30, leftTop, middleTop, rightTop);
		top.setAlignment(Pos.CENTER);

		//Vboxes for all letters
		VBox left = new VBox(5,allLettersGuessedInfoText, firstLine,secondLine,thirdLine,fourthLine,fifthLine,sixthLine);
		left.setAlignment(Pos.CENTER);
		//Vbox for center
		VBox center = new VBox(5, makeGuessText, guessTF, sendGuessBtn);
		center.setAlignment(Pos.CENTER);



		pane.setLeft(left);
		pane.setTop(top);
		pane.setCenter(center);

		return new Scene(pane, 800, 600);
	}

}
