import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread {

    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;

    private Consumer<Serializable> callback;

    Client(Consumer<Serializable> call, Socket socket) {
        callback = call;
        socketClient = socket;
    }

    public void run() {
        try {
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                WordGuessInfo data = (WordGuessInfo) in.readObject();
                callback.accept(data);

            } catch (Exception e) {
                callback.accept("Failure to receive info");
                callback.accept("Server shut down");
                break;
            }
        }

    }

    public void send(WordGuessInfo data) {
        try {
            out.writeObject(data);
            out.reset();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
