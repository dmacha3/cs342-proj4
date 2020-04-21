
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;
/*
 * Clicker: A: I really get it    B: No idea what you are talking about
 * C: kind of following
 */

public class Server {

    int count = 1;
    ArrayList<ClientThread> players = new ArrayList<ClientThread>();
    TheServer server;
    private Consumer<Serializable> callback;

    Server(Consumer<Serializable> call) {

        callback = call;
        server = new TheServer();
        server.start();
    }

    public class TheServer extends Thread {

        public void run() {

            try (ServerSocket mysocket = new ServerSocket(5555);) {
                System.out.println("Server is waiting for a client!");

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

        WordGuessInfo wordGuessInfo;

        ClientThread(Socket s, int count) {
            this.connection = s;
            this.playerId = count;

            this.animalCategory = new Animal();
            this.countryCategory = new Country();
            this.superheroCategory = new Superhero();

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

                    // Update view
                    callback.accept("client: " + count + " sent: " + data);

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

    }// end of client thread
}
