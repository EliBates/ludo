package game.server.io;

import game.server.GameServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener extends Thread {

    private GameServer server;

    private ServerSocket serverSocket;

    private int port;

    public Listener(GameServer server, int port) {
        this.server = server;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);

            System.out.println("Server listening on port " + port);

            Socket socket;
            while (true) {
                 socket = serverSocket.accept();
                 Connection connection = new Connection(socket, server);

                 connection.start();
                 server.addConnection(connection);

                 System.out.println("Got a new conn");

                 sleep(200);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
