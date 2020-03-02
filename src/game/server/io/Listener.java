package game.server.io;

import game.server.GameServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener extends Thread {

    private GameServer server;

    public ServerSocket serverSocket;

    private int port;

    public boolean isRunning = true;

    Socket socket=null;

    public Listener(GameServer server, int port) {
        this.server = server;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);

            System.out.println("Server listening on port " + port);

            while (isRunning) {
                if(!serverSocket.isClosed()) {
                    socket = serverSocket.accept();
                    Connection connection = new Connection(socket, server);
                    if (server.acceptingNewConnections) {
                        connection.start();
                        server.addConnection(connection);
                    }
                    sleep(200);
                }
            }
        } catch (IOException | InterruptedException e) {
            //e.printStackTrace();
        }
    }

    public void dispose() {
        try {
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isRunning=false;
    }
}
