
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;

public class Server {

    ServerSocket mysocket;
    int count = 1;
    ArrayList<ClientThread> players = new ArrayList<ClientThread>();
    TheServer server;
    private Consumer<Serializable> callback;

    Server(Consumer<Serializable> call, ServerSocket socket) {
        this.mysocket = socket;
        callback = call;
        server = new TheServer();
        server.start();
    }

    public class TheServer extends Thread {

        public void run() {

            try{
                callback.accept("Server is waiting for a players on port number " + mysocket.getLocalPort());

                while (true) {
                    ClientThread c = new ClientThread(mysocket.accept(), count);
                    callback.accept("client has connected to server: " + "client #" + count);
                    players.add(c);
                    c.start();
                    count++;

                }
            } // end of try
            catch (Exception e) {
                callback.accept("Server socket did not launch");
            }
        }// end of while
    }

    class ClientThread extends Thread {

        Socket connection;
        int playerId;
        ObjectInputStream in;
        ObjectOutputStream out;

        // Data for game
        Animal animalCategory;
        Country countryCategory;
        Superhero superheroCategory;

        Category currentCategory;

        WordGuessInfo wordGuessInfo;

        ClientThread(Socket s, int count) {
            this.connection = s;
            this.playerId = count;

            this.animalCategory = new Animal();
            this.countryCategory = new Country();
            this.superheroCategory = new Superhero();

            this.currentCategory = null;

            this.wordGuessInfo = new WordGuessInfo();
        }

        public void updatePlayer() {

            ClientThread playerThread = players.get(playerId);
            try {
                playerThread.out.writeObject(this.wordGuessInfo);
            } catch (Exception e) {
            }

        }

        public void run() {

            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            } catch (Exception e) {
                System.out.println("Streams not open");
            }

            this.wordGuessInfo.connected = true;

            // Set porccessCode to 1 indicating that player has successfully conntected.
            this.wordGuessInfo.processCode = 1;

            // Update view saying that new player has been conntected...
            // TODO....

            // Send initial message to player saying that has been successfully conntected.
            this.updatePlayer();

            while (true) {
                try {
                    WordGuessInfo data = (WordGuessInfo) in.readObject();

                    this.wordGuessInfo = data;

                    this.proccessWordGuessInfo();

                } catch (Exception e) {

                    // Update view with message......
                    // Todo

                    // callback.accept(
                    // "OOOOPPs...Something wrong with the socket from client: " + count +
                    // "....closing down!");
                    // updateClients("Client #" + count + " has left the server!");

                    // Remove player
                    players.remove(this);
                    break;
                }
            }
        }// end of run

        public void proccessWordGuessInfo() {

            switch (this.wordGuessInfo.processCode) {
                // 1 indicates that some category has been choosen
                // get random word from category
                case 1:
                    if (this.wordGuessInfo.currentCategory.equals("country")) {
                        this.animalCategory.setCurrentWord();
                        this.currentCategory = this.countryCategory;
                    } else if (this.wordGuessInfo.currentCategory.equals("animal")) {
                        this.animalCategory.setCurrentWord();
                        this.currentCategory = this.animalCategory;
                    } else if (this.wordGuessInfo.currentCategory.equals("superhero")) {
                        this.superheroCategory.setCurrentWord();
                        this.currentCategory = this.superheroCategory;
                    }

                    this.wordGuessInfo.indexesToUpdate = new ArrayList<Integer>();
                    this.wordGuessInfo.currentWordLength = this.currentCategory.getCurrentWordLenght();
                    this.wordGuessInfo.numberOfInvalidGuesses = 0;

                    // Set proccess code to 2 indicating that word has been choosen
                    this.wordGuessInfo.processCode = 2;
                    this.updatePlayer();

                    break;

                // Case 2 is for checking guessed letter in the word.
                case 2:
                    if (this.currentCategory.checkLetter(this.wordGuessInfo.guessedLetter)) {
                        this.wordGuessInfo.wasLetterGuessed = true;
                        this.wordGuessInfo.processCode = 3;
                        this.currentCategory.copyIndexesToUpdateForCurrentGuess(this.wordGuessInfo.indexesToUpdate);

                        this.checkIfCategoryGuessed();
                        this.checkIfGameWon();
                    } else {
                        this.wordGuessInfo.wasLetterGuessed = false;
                        this.wordGuessInfo.numberOfInvalidGuesses = this.currentCategory.getNumberOfInvalidGuesses();
                        this.checkIfCategoryFailed();
                        this.checkIfGameOver();

                        this.wordGuessInfo.processCode = 4;
                    }

                    this.updatePlayer();

                    break;
            }
        }

        public void checkIfGameWon() {

            // Check if every category was guessed
            if (this.animalCategory.isCategoryGuessed() == true && this.superheroCategory.isCategoryGuessed() == true
                    && this.countryCategory.isCategoryGuessed() == true) {
                this.wordGuessInfo.gameWon = true;

                // Set processCode to 5 indicating that game was won
                this.wordGuessInfo.processCode = 5;
            }
        }

        public void checkIfCategoryGuessed() {
            if (this.currentCategory.isCategoryGuessed() == true) {
                this.wordGuessInfo.animalCategoryPassed = this.animalCategory.isCategoryGuessed();
                this.wordGuessInfo.superheroCategoryPassed = this.superheroCategory.isCategoryGuessed();
                this.wordGuessInfo.countryCategoryPassed = this.countryCategory.isCategoryGuessed();

                this.currentCategory.resetForNextGame();
                this.currentCategory = null;

                // Set processCode to 6 indicating that category was guessed.
                this.wordGuessInfo.processCode = 6;
            }
        }

        public void checkIfCategoryFailed() {
            if (this.currentCategory.getCurrentWord() == null) {
                // Set processCode to 7 indicating that category was not guessed
                this.wordGuessInfo.processCode = 7;
                this.currentCategory.resetForNextGame();
                this.currentCategory = null;
            }
        }

        public void checkIfGameOver() {

            if (superheroCategory.categoryFailed() == true || this.countryCategory.categoryFailed() == true
                    || this.animalCategory.categoryFailed() == true) {

                // Set processCode to 8 indicating that game is over
                this.wordGuessInfo.processCode = 8;
            }

        }

    }// end of client thread
}
