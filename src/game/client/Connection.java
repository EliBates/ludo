package game.client;

import java.io.*;
import java.net.Socket;

/**
 * @author Eli
 * Handles the connecction to the game server
 */

public class Connection extends Thread {

    //The client instance
    private Client client;

    //Reader/Writer for sending and receiving data to server
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    //determines if the client connection should be killed off
    private boolean killThread;

    public Connection(Client client, String ip, int port) {
        this.client = client;
        client.setConnection(this);

        try {
            Socket socket = new Socket(ip, port);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
            System.out.println("Attempting to connect client... " + ip + ":" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an update to the server (such as a mouse click/ dice roll)
     * @param update the update message
     */
    public void sendUpdate(String update) {
        printWriter.println(update);
        printWriter.flush();
    }

    //kill the connection
    public void dispose(){
        killThread = true;
    }

    @Override
    public void run() {
        String feedback;
        try {
            while (((feedback = bufferedReader.readLine()) != null) && !killThread) {
                if (client != null)
                    client.receiveUpdate(feedback); // listens for updates and sends to the client instance
                else
                    System.out.println("Client is null.. But here is the message anyways: " + feedback);
                Thread.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
