package game.client;

import java.io.*;
import java.net.Socket;

public class ClientConnection extends Thread {

    private Client client;
    private String ip;
    private int port;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private InputStream inputStream;
    private OutputStream outputStream;

    public ClientConnection(Client client, String ip, int port) {
        this.client = client;
        this.ip = ip;
        this.port = port;

        try {
            Socket socket = new Socket(ip, port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
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

    @Override
    public void run() {
        String feedback;
        try {
            sendUpdate("HEY! I'm the client!");
            while ((feedback = bufferedReader.readLine()) != null) {
                client.recieveUpdate(feedback);
                Thread.sleep(100);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
