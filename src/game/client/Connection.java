package game.client;

import java.io.*;
import java.net.Socket;

public class Connection extends Thread {

    private Client client;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
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

    public void sendUpdate(String update) {
        printWriter.println(update);
        printWriter.flush();
    }

    public void dispose(){
        killThread = true;
    }

    @Override
    public void run() {
        String feedback;
        try {
            while (((feedback = bufferedReader.readLine()) != null) && !killThread) {
                if (client != null)
                    client.recieveUpdate(feedback);
                else
                    System.out.println("Client is null.. But here is the message anyways: " + feedback);
                Thread.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
