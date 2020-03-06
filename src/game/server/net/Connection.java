package game.server.net;

import game.server.GameServer;

import java.io.*;
import java.net.Socket;

/**
 * @author Eli
 * A basic thread containing a Connection with the client
 */

public class Connection extends Thread {

    private GameServer server;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Connection(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;

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
            while (!socket.isClosed() && (message = reader.readLine()) != null) {
                server.processPacket(this, message);
            }
            socket.close();
        } catch (IOException e) {
            dispose();
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to the client
     * @param message the message to send
     */
    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    /**
     * closes the connection
     */
    public void dispose() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
