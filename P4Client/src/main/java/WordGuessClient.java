import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;

import java.awt.*;
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
	Button catOneBtn, catTwoBtn, catThreeBtn;
	Button sendGuessBtn;
	TextField portNumTF, ipTF, guessTF;
	Alert emptyPortError, emptyIPError, wrongPortOrIPError, duplicateLetterError;
	ArrayList<String> letterColors;
	ArrayList<Button> lettersList;

	ArrayList<Button> currentWord;
	VBox letters; // letters remaining
	HBox word; // word on screen



	Text msg;




	Pane p;

	String passed = ""; // keeps track of which cateogires have been passed

	int totalRoundsWon = 0;

	boolean roundOver = false;

	ArrayList<Character> lettersGuessed; // stores the letters guessed per turn
	Text guessesLeft, guessesLeftNum;
	Text totalGuessed, totalGuessedNum;

	WordGuessInfo gameInfo;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	// feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Variables
		sceneMap = new HashMap<String, Scene>();
		listItems = new ListView<>();
		// Port Alert
		emptyPortError = new Alert(Alert.AlertType.INFORMATION);
		emptyPortError.setTitle("Incorrect Port Info");
		emptyPortError.setHeaderText(null);
		emptyPortError.setContentText("You have not entered a port.");

		// IP Alert
		emptyIPError = new Alert(Alert.AlertType.INFORMATION);
		emptyIPError.setTitle("Incorrect IP Address Info");
		emptyIPError.setHeaderText(null);
		emptyIPError.setContentText("You have not entered an IP Address.");

		// IP and Port Alert
		wrongPortOrIPError = new Alert(Alert.AlertType.INFORMATION);
		wrongPortOrIPError.setTitle("Incorrect IP Address or Port Info");
		wrongPortOrIPError.setHeaderText(null);
		wrongPortOrIPError.setContentText("The IP Address or Port you have entered cannot be connected to.");

		// duplicate letter chosen alert
		duplicateLetterError = new Alert(Alert.AlertType.INFORMATION);
		duplicateLetterError.setTitle("Duplicate letter picked");
		duplicateLetterError.setHeaderText(null);
		duplicateLetterError.setContentText("You have already chosen this letter");

		sceneMap.put("Start", createStartScene());
		sceneMap.put("Port", createPortAndIPScene());
		sceneMap.put("Categories", createCategoryScene());
		sceneMap.put("Game", createGameScene());

		guessTF.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.equals("") && !newValue.matches("\\sa-zA-Z*")) {

				String newText = newValue.replaceAll("[^\\sa-zA-Z]", "");
				guessTF.setText(newText);
				if (newText.length() < 2)
					return;

			}
			if (newValue.length() > 1) {
				String newText = newValue.substring(newValue.length() - 1);
				guessTF.setText(newText);
			}
		});

		sendGuessBtn.setOnAction(e -> {
			if (updateServer()) {
				// sendGuessBtn.setDisable(true); //disable sending more info until server
				// responds

				//updateWithNewInformation(); // to be called later if we read from server
			}
		});

		startBtn.setOnAction(e -> {
			primaryStage.setScene(sceneMap.get("Port"));
		});

		startClientBtn.setOnAction(e -> {
			if (portNumTF.getText().isEmpty()) {
				emptyPortError.showAndWait();
			}
			if (ipTF.getText().isEmpty()) {
				emptyIPError.showAndWait();
			} else {
				try {
					socket = new Socket(ipTF.getText(), Integer.parseInt(portNumTF.getText()));
					connected = true;
				} catch (Exception a) {
					connected = false;
					wrongPortOrIPError.showAndWait();
					portNumTF.setText("");
					ipTF.setText("");
				}
				if (connected == true) {
					clientConnection = new Client(
							data -> Platform.runLater(() ->
									{
										int previousTotal = totalRoundsWon;
										processData((WordGuessInfo) data);
										if (totalRoundsWon - previousTotal == 1 || roundOver) {
											primaryStage.setScene(sceneMap.get("Categories"));

										}

									}

							), socket);
					clientConnection.start();
					primaryStage.setScene(sceneMap.get("Categories"));
				}
			}
		});

		catOneBtn.setOnAction(e -> {
			this.choseCategory("animal");
			System.out.println("Chose animal category");
			primaryStage.setScene(sceneMap.get("Game"));
		});

		catTwoBtn.setOnAction(e -> {
			choseCategory("country");
			System.out.println("Chose country category");
			primaryStage.setScene(sceneMap.get("Game"));
		});

		catThreeBtn.setOnAction(e -> {
			this.choseCategory("superhero");
			System.out.println("Chose superhero category");
			primaryStage.setScene(sceneMap.get("Game"));
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

		// TODO Auto-generated method stub
		primaryStage.setTitle("(Client) Playing word guess!!!");
		primaryStage.setScene(sceneMap.get("Start"));
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
		primaryStage.show();
	}

//	public Scene createStartScene() {
//		BorderPane pane = new BorderPane();
//		pane.setPadding(new Insets(400, 0, 0, 0));
//		String style = "-fx-background-image: url('Images/bgWordsInMiddle.jpg'); " + "-fx-background-size: 100% 100%;";
//		pane.setStyle(style);
//		startBtn = new Button("Start");
//		startBtn.setFont(Font.font("Arial", 30));
//		VBox center = new VBox(startBtn);
//		center.setAlignment(Pos.TOP_CENTER);
//		pane.setCenter(center);
//		return new Scene(pane, 800, 600);
//	}
//
//	public Scene createPortAndIPScene() {
//		BorderPane pane = new BorderPane();
//		pane.setPadding(new Insets(70));
//		String style = "-fx-background-image: url('Images/bgWordsAtTop.jpg'); " + "-fx-background-size: 100% 100%;";
//		pane.setStyle(style);
//		portNumTF = new TextField();
//		ipTF = new TextField();
//		Text enterIPText = new Text("Enter an IP Address");
//		Text ipText = new Text(" IP Address:");
//		Text enterPortText = new Text("Enter a port number");
//		Text portText = new Text(" Port:");
//		enterIPText.setFont(Font.font("Arial", 30));
//		ipText.setFont(Font.font("Arial", 30));
//		enterPortText.setFont(Font.font("Arial", 30));
//		portText.setFont(Font.font("Arial", 30));
//		startClientBtn = new Button("Start Client");
//		startClientBtn.setFont(Font.font("Arial", 30));
//		HBox portInfo = new HBox(5, portText, portNumTF);
//		HBox ipInfo = new HBox(5, ipText, ipTF);
//		ipInfo.setAlignment(Pos.CENTER);
//		portInfo.setAlignment(Pos.CENTER);
//		VBox center = new VBox(10, enterIPText, ipInfo, enterPortText, portInfo, startClientBtn);
//		center.setAlignment(Pos.CENTER);
//		pane.setCenter(center);
//		return new Scene(pane, 800, 600);
//	}
//
//	public Scene createCategoryScene() {
//		BorderPane pane = new BorderPane();
//		pane.setPadding(new Insets(70));
//		String style = "-fx-background-image: url('Images/bgWordsAtTop.jpg'); " + "-fx-background-size: 100% 100%;";
//		pane.setStyle(style);
//		Text pickCatText = new Text("Please pick a category");
//		pickCatText.setFont(Font.font("Arial", 30));
//		catOneBtn = new Button("Animals");
//		catTwoBtn = new Button("Countries");
//		catThreeBtn = new Button("Super Heroes");
//		catOneBtn.setFont(Font.font("Arial", 30));
//		catTwoBtn.setFont(Font.font("Arial", 30));
//		catThreeBtn.setFont(Font.font("Arial", 30));
//		HBox categories = new HBox(20, catOneBtn, catTwoBtn, catThreeBtn);
//		categories.setAlignment(Pos.CENTER);
//		VBox center = new VBox(10, pickCatText, categories);
//		center.setAlignment(Pos.CENTER);
//		pane.setCenter(center);
//		return new Scene(pane, 800, 600);
//	}

	public Scene createStartScene() {
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(400,0,0,0));
		String style = "-fx-background-image: url('Images/bgWordsInMiddle.jpg'); "  +
				"-fx-background-size: 100% 100%;";
		pane.setStyle(style);
		VBox dummy = new VBox();
		startBtn = new Button("Start");
		startBtn.setStyle("    -fx-font-family: \"Helvetica\";\n" +
				"    -fx-font-size: 30px;\n" +
				"    -fx-text-fill: #ffffff;\n" +
				" -fx-background-color: green;\n" +
				"    -fx-font-weight: bold;\n");
		VBox center = new VBox(50, dummy, startBtn);
		center.setAlignment(Pos.TOP_CENTER);
		pane.setCenter(center);
		return new Scene(pane, 1280, 720);
	}

	public Scene createPortAndIPScene(){
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		String style = "-fx-background-image: url('Images/bgWordsAtTop.jpg'); "  +
				"-fx-background-size: 100% 100%;";
		pane.setStyle(style);


		portNumTF = new TextField();
		ipTF = new TextField();
		Text ipText = new Text(" IP Address:");
		Text portText = new Text(" Port:");
		ipText.setFont(Font.font("Arial", 30));
		portText.setStyle("    -fx-font-family: \"Helvetica\";\n" +
				"    -fx-font-size: 20px;\n" +
				"    -fx-fill: #ffffff;\n" +
				"    -fx-font-weight: bold;\n");

		ipText.setStyle("    -fx-font-family: \"Helvetica\";\n" +
				"    -fx-font-size: 20px;\n" +
				"    -fx-fill: white;\n" +
				"    -fx-font-weight: bold;\n");


		startClientBtn = new Button("Connect");
		startClientBtn.setStyle("    -fx-font-family: \"Helvetica\";\n" +
				"    -fx-font-size: 30px;\n" +
				"    -fx-text-fill: #ffffff;\n" +
				" -fx-background-color: green;\n" +
				"    -fx-font-weight: bold;\n");
		HBox portInfo = new HBox(5, portText, portNumTF);
		HBox ipInfo = new HBox(5, ipText, ipTF);

		VBox txts = new VBox(5, ipText, portText);
		VBox tfs = new VBox(5, ipTF, portNumTF);

		HBox startDetails = new HBox(txts, tfs);
		startDetails.setAlignment(Pos.CENTER);

		VBox center = new VBox(10, startDetails, startClientBtn);
		center.setAlignment(Pos.CENTER);
		pane.setCenter(center);
		return new Scene(pane, 1280, 720);
	}

	public Scene createCategoryScene(){
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		String style = "-fx-background-image: url('Images/bgWordsAtTop.jpg'); "  +
				"-fx-background-size: 100% 100%;";
		pane.setStyle(style);
		Text pickCatText = new Text("Please pick a category");
		pickCatText.setStyle("    -fx-font-family: \"Helvetica\";\n" +
				"    -fx-font-size: 24px;\n" +
				"    -fx-fill: white;\n" +
				"    -fx-font-weight: bold;\n");

		catOneBtn = new Button("Animals");
		catTwoBtn = new Button("Countries");
		catThreeBtn = new Button("Superheroes");
		catOneBtn.setMinSize(400, 75);
		catOneBtn.setMaxSize(400, 75);
		catTwoBtn.setMinSize(400, 75);
		catTwoBtn.setMaxSize(400, 75);
		catThreeBtn.setMinSize(400, 75);
		catThreeBtn.setMaxSize(400, 75);



		catOneBtn.setStyle("    -fx-font-family: \"Helvetica\";\n" +
				"    -fx-font-size: 40px;\n" +
				"    -fx-text-fill: #ffffff;\n" +
				" -fx-background-color: green;\n" +
				"    -fx-font-weight: bold;\n");
		catTwoBtn.setStyle("    -fx-font-family: \"Helvetica\";\n" +
				"    -fx-font-size: 40px;\n" +
				"    -fx-text-fill: #ffffff;\n" +
				" -fx-background-color: green;\n" +
				"    -fx-font-weight: bold;\n");
		catThreeBtn.setStyle("    -fx-font-family: \"Helvetica\";\n" +
				"    -fx-font-size: 40px;\n" +
				"    -fx-text-fill: #ffffff;\n" +
				" -fx-background-color: green;\n" +
				"    -fx-font-weight: bold;\n");
		VBox categories = new VBox(20, catOneBtn, catTwoBtn, catThreeBtn);
		categories.setAlignment(Pos.CENTER);


		VBox center = new VBox(10, pickCatText, categories);

		msg = new Text("");
		VBox v = new VBox(msg);
		v.setAlignment(Pos.CENTER);

		msg.setStyle("    -fx-font-family: \"Helvetica\";\n" +
				"    -fx-font-size: 20px;\n" +
				"    -fx-fill: white;\n" +
				"    -fx-font-weight: bold;\n");
		center.setAlignment(Pos.CENTER);
		pane.setCenter(center);

		pane.setBottom(v);
		return new Scene(pane, 1280, 720);
	}

	// creates letter objects for game
	public VBox createLetterObjects() {
		VBox main = null;
		HBox row1, row2;

		row1 = new HBox();
		row2 = new HBox();
		for (int i = 0; i < 13; i++) {
			Button b = new Button(getLetter(i));
			b.setMaxSize(48, 48);
			b.setMinSize(48, 48);
			b.setStyle("    -fx-font-family: \"Helvetica\";\n" + "    -fx-font-size: 20px;\n"
					+ "    -fx-font-weight: bold;\n" + "    -fx-text-fill: #FFFFFF;\n" + " -fx-background-color:#"
					+ letterColors.get(i));
			lettersList.add(b);
			row1.getChildren().add(lettersList.get(i));
		}

		for (int i = 13; i < 26; i++) {
			Button b = new Button(getLetter(i));
			b.setMaxSize(48, 48);
			b.setMinSize(48, 48);
			b.setStyle("    -fx-font-family: \"Helvetica\";\n" + "    -fx-font-size: 20px;\n"
					+ "    -fx-font-weight: bold;\n" + "    -fx-text-fill: #FFFFFF;\n" + " -fx-background-color:#"
					+ letterColors.get(i));
			lettersList.add(b);
			row2.getChildren().add(lettersList.get(i));
		}

		main = new VBox(10, row1, row2);
		main.setAlignment(Pos.CENTER);
		main.setMaxWidth(48 * 13);
		main.setMinWidth(48 * 13);
		main.setStyle("-fx-background-color: rgb(32, 17, 16)");
		return main;
	}

	public String getLetter(int i) {
		int ascii = 97 + i; // 'a' + 0...26
		return (Character.toString((char) ascii));
	}

	public Scene createGameScene() {
		// BorderPane pane = new BorderPane();
		p = new Pane();
		String style = "-fx-background-image: url('Images/bgSuperF.png'); " + "-fx-background-size: 100% 100%;";
		// pane.setStyle(style);
		p.setStyle(style);

		lettersGuessed = new ArrayList<>(); // new letters to guess
		lettersList = new ArrayList<>();

		/// to interact with the server ///

		// send button -> sends text in textfield to the server
		sendGuessBtn = new Button("Make Guess");

		guessTF = new TextField();
		VBox guessBox = new VBox(10, guessTF, sendGuessBtn);
		guessBox.setStyle("-fx-background-color: rgb(32, 17, 16)");
		// guessBox.setAlignment(Pos.CENTER);
		guessBox.setMaxHeight(262);
		guessBox.setMinHeight(262);
		guessBox.setMaxWidth(447);
		guessBox.setMinWidth(447);

		letterColors = new ArrayList<>();

		for (int i = 0; i < 26; i++) {
			letterColors.add("1a211c");
		}

		/// to display num of guesses left ///
		guessesLeft = new Text("Incorrect guesses left: ");
		guessesLeftNum = new Text("6");

		guessesLeft.setStyle("    -fx-font-family: \"Helvetica\";\n" + "    -fx-font-size: 30px;\n"
				+ "    -fx-fill: #FFFFFF;\n" +

				"    -fx-font-weight: bold;\n");

		guessesLeftNum.setStyle("    -fx-font-family: \"Helvetica\";\n" + "    -fx-font-size: 30px;\n"
				+ "    -fx-fill: #FFFFFF;\n" + "    -fx-font-weight: bold;\n");

		letters = createLetterObjects();

		HBox numLeft = new HBox(5, guessesLeft, guessesLeftNum);
		numLeft.setAlignment(Pos.CENTER);
		numLeft.setMinHeight(59);
		numLeft.setMaxWidth(646);
		numLeft.setMinWidth(646);
		numLeft.setMaxHeight(59);
		HBox centerPiece = new HBox(50, guessBox, numLeft);
		p.getChildren().add(guessBox);
		p.getChildren().add(numLeft);
		p.getChildren().add(letters);

		int wordLength = 5; // temp

		word = createNewWord(wordLength);
		word.setAlignment(Pos.CENTER);
		p.getChildren().add(word);

		int wordX = getXCoord(wordLength);
		int wordY = 166;

		word.setLayoutX(wordX);
		word.setLayoutY(wordY);

		guessBox.setLayoutX(62);
		guessBox.setLayoutY(400);
		guessBox.setAlignment(Pos.CENTER);
		guessTF.setMinHeight(150);
		guessTF.setMinWidth(150);
		guessTF.setMaxWidth(200);
		guessTF.setAlignment(Pos.CENTER);
		String gS = "-fx-background-color: rgb(12, 4, 4); -fx-font-size: 72px; -fx-text-fill: #FFFFFF;";
		guessTF.setStyle(gS);
		sendGuessBtn.setStyle(
				"    -fx-font-family: \"Helvetica\";\n" + "    -fx-font-size: 20px;\n" + "    -fx-font-weight: bold;\n"
						+ "    -fx-text-fill: #FFFFFF;\n" + " -fx-background-color: rgb(23, 74, 55);");

		numLeft.setLayoutY(413);
		numLeft.setLayoutX(568);
		numLeft.setStyle("    -fx-font-family: \"Helvetica\";\n" + "    -fx-font-size: 30px;\n"
				+ "    -fx-font-weight: bold;\n" + " -fx-background-color: rgb(23, 74, 55);");

		letters.setLayoutY(534);
		letters.setLayoutX(579);

		totalGuessed = new Text("Words guessed: ");
		totalGuessedNum = new Text("0");


		totalGuessed.setStyle("    -fx-font-family: \"Helvetica\";\n" + "    -fx-font-size: 30px;\n"
				+ "    -fx-fill: #FFFFFF;\n" +

				"    -fx-font-weight: bold;\n");

		totalGuessedNum.setStyle("    -fx-font-family: \"Helvetica\";\n" + "    -fx-font-size: 30px;\n"
				+ "    -fx-fill: #FFFFFF;\n" +

				"    -fx-font-weight: bold;\n");

		HBox total = new HBox(5, totalGuessed, totalGuessedNum);
		total.setMinSize(200, 30);
		total.setAlignment(Pos.CENTER);

		p.getChildren().add(total);
		total.setLayoutY(670);
		total.setLayoutX(480);

		return new Scene(p, 1280, 720);
	}

	public boolean isFormatted(String text) {

		if (text.length() != 1) {
			return false; // too long, should only be one letter
		}

		char first = text.charAt(0);
		if (!Character.isAlphabetic(first) || !Character.isUpperCase(first)) {
			return false;
		}

		return true;
	}

	// helper function
	// is called each time the make guess button is pressed and makes appropricate
	// deicision based on
	// text in text field
	public boolean updateServer() {

		String val = guessTF.getText();
		guessTF.setText(""); // reset textfield regardless of outcome

		if (val.matches("\\sa-zA-Z*")) {
			System.out.println("not matching, invalid");
			// display error message to user that they provided in valid input
			return false;
		}

		if (val.equals("")) {
			System.out.println("got empty string!");
			return false;
		}


		if (lettersGuessed.contains(val.charAt(0))) {
			duplicateLetterError.showAndWait();
			return false;
		}

		// here we know the input is valid and unique

		System.out.println("sending the server letter " + val);
		lettersGuessed.add(val.charAt(0));

		// Send request to server
		gameInfo.guessedLetter = val.charAt(0);
		gameInfo.processCode = 2;
		clientConnection.send(gameInfo);

		return true;
	}

	// helper function that is called when server returns results
	public void updateWithNewInformation(boolean correct) {

		for (Character c : lettersGuessed) {
			updateLetter(c); // update each letter to be not visible
		}

		guessesLeftNum.setText(String.valueOf(6 - gameInfo.numberOfInvalidGuesses));

		if (correct) {
			for (int i: gameInfo.indexesToUpdate) {
				displayLetter(i, gameInfo.guessedLetter);
			}
		} else {
			System.out.println("user has 1 less guess for a new total of " + guessesLeftNum.getText());
		}


		System.out.println("");
	}

	// at the start of each round this creates a new object that is displayed on
	// screen
	public HBox createNewWord(int length) {

		word = new HBox();
		currentWord = new ArrayList<>();

		int h = 1240 / length;

		if (h > 128) {
			h = 128; // update height if it gets too tall
		}
		int w = 1240 / length;

		if (w > 128) {
			w = 128; // update width if it gets too big
		}

		for (int i = 0; i < length; i++) {
			Button b = new Button("-");
			b.setMaxSize(w, h);
			b.setMinSize(w, h);
			b.setStyle("    -fx-font-family: \"Helvetica\";\n" + "    -fx-font-size: 40px;\n"
					+ "    -fx-font-weight: bold;\n" + "    -fx-text-fill: #FFFFFF;\n"
					+ " -fx-background-color:rgb(32, 17, 16)");

			currentWord.add(b);
			word.getChildren().add(currentWord.get(i));
		}

		word.setMaxHeight(h);
		word.setMinHeight(h);
		word.setMinWidth(w * length);
		word.setMaxWidth(w * length);

		return word;

	}

	// updates the remaining letters to remove it from the list of remaining letters
	public void updateLetter(char c) {

		int index = c - 97; // c - 'a' is the right index
		letterColors.set(index, "#0c0404");
		lettersList.get(index).setStyle("-fx-background-color: #0c0404; -fx-text-fill: #0c0404");
	}

	public void resetLetter(char c) {
		int index = c - 97; // c - 'a' is the right index
		lettersList.get(index).setStyle("    -fx-font-family: \"Helvetica\";\n" + "    -fx-font-size: 20px;\n"
				+ "    -fx-font-weight: bold;\n" + "    -fx-text-fill: #FFFFFF;\n" + " -fx-background-color:#1a211c");
	}

	// to adjust dynamically to the size of the word, we must reposition our 'word'
	// vector on the screen
	// this helper returns the right x coordinate for a word of the given length
	public int getXCoord(int length) {
		int w = 1240 / length;

		int x;

		if (w > 128) {
			w = 128; // update width if it gets too big
		}

		x = (1280 / 2) - ((length * w) / 2);

		return x;
	}

	// when we receive word from the server that the user guessed correctly, we get
	// an
	// index to update and the letter to update with (we never send the whole word)
	// ... this function updates that letter for us
	public void displayLetter(int pos, char c) {

		String s = Character.toString(c);
		currentWord.get(pos).setText(s);
	}

	public void processData(WordGuessInfo data) {

		this.gameInfo = data;
		switch (gameInfo.processCode) {

			case 1: // Simple message that connection was succesfull
				System.out.println("-- Connection was successful");
				break;

			case 2: // Returns currentword length
				System.out.println("-- Received new word of length " + gameInfo.currentWordLength);
				roundOver = false; // started new round
				makeWord(gameInfo.currentWordLength);
				break;

			case 3: // letter guessed correctly
				System.out.println("-- Letter guessed correctly");
				updateWithNewInformation(true);
				break;

			case 4: // Letter was not guessed correctly
				System.out.println("--letter was not guessed correctly");
				updateWithNewInformation(false);
				break;
			case 5: // Whole game was won

				System.out.println("WON WHOLE GAME === ADD NEW SCREEN");
				msg.setText("You won the game");
				break;


			case 6: // User guessed the word
				updateCategories();
				System.out.println("--word guessed! passed categories: " + passed);
				msg.setText("You guessed the last word correctly!");
				resetUI();


				break;
			// User did not guess the word after 6 invalid guesses
			case 7:
				System.out.println("--word was not guessed. passed categories: " + passed);
				roundOver = true;
				msg.setText("You did not guess the word correctly");
				resetUI();
				break;
			// Game is over, user did not guess word in one of three categories 3 times
			case 8:
				System.out.println("LOST WHOLE GAME === ADD NEW SCREEN");
				;

				break;
			default:
				break;
		}

	}

	public void choseCategory(String category) {

		gameInfo.currentCategory = category;
		gameInfo.processCode = 1;

		String style = "-fx-background-image: url('Images/actualfinal" + category + ".png'); " + "-fx-background-size: 100% 100%;";
		p.setStyle(style); // update background of screen to corresponding cat

		this.clientConnection.send(gameInfo);
	}


	// creates word object on screen
	public void makeWord(int length) {
		p.getChildren().remove(word);
		word = createNewWord(length);
		p.getChildren().add(word);

		int wordX = getXCoord(length);
		int wordY = 166;

		word.setLayoutX(wordX);
		word.setLayoutY(wordY);
	}

	public void updateCategories() {

		if (gameInfo.animalCategoryPassed) {
			passed += "animal, ";
			catOneBtn.setDisable(true);
		}

		if (gameInfo.countryCategoryPassed) {
			passed += "country, ";
			catTwoBtn.setDisable(true);
		}

		if (gameInfo.superheroCategoryPassed) {
			passed += "superhero, ";
			catThreeBtn.setDisable(true);
		}

		totalRoundsWon++;

	}


	// called at the start of each new round
	public void resetUI() {


		lettersGuessed = new ArrayList<>();

		for (int i = 0; i < 26; i++) {
			letterColors.add("1a211c");
			resetLetter((char) (i + 97));
		}

		guessesLeftNum.setText("6"); // new set of guesses
		totalGuessedNum.setText(String.valueOf(totalRoundsWon));


	}
}
