package game.server.io;

import java.io.*;
import java.net.Socket;

public class Connection extends Thread {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Connection(Socket socket) {
        this.socket = socket;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Client connected!");
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Server Recieved: " + message);
                sendMessage("Hello world from server!");
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }
}
