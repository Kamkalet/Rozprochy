package chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    private static final int PORT = 12345;
    private static int clientCounter = 1;
    private static int clientIdCounter = 1;
    private static Map<Integer, Socket> idToSocketMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {

        System.out.println("JAVA TCP SERVER");

        ServerSocket serverSocket = null;

        try {

            serverSocket = new ServerSocket(PORT);

            while (true) {

                // accept client
                Socket clientSocket = serverSocket.accept();

                new ClientThread(clientSocket, clientIdCounter).start();
                idToSocketMap.put(clientIdCounter, clientSocket);
                System.out.println("Client number " + clientIdCounter + " connected.");
                clientCounter++;
                clientIdCounter++;

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class ClientThread extends Thread {

        private Socket socket;
        private int clientId;

        ClientThread(Socket clientSocket, int clientId) {
            this.socket = clientSocket;
            this.clientId = clientId;
        }

        public void run() {
            InputStream inp;
            BufferedReader brinp;

            try {
                inp = socket.getInputStream();
                brinp = new BufferedReader(new InputStreamReader(inp));

            } catch (IOException e) {
                return;
            }
            String line;
            while (true) {
                try {
                    line = brinp.readLine();
                    if ((line == null)) {
                        socket.close();
                        return;
                    } else {
                        System.out.println(line);
                        sendToOthers(line);
                    }
                } catch (IOException e) {
                    if (this.socket != null && !this.socket.isClosed()) {
                        try {
                            synchronized (this){
                                this.socket.close();
                                idToSocketMap.remove(clientId);
                                clientCounter--;
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            return;
                        }
                    }
                }
            }
        }

        public synchronized void sendToOthers(String line) throws IOException {
            Map<Integer, Socket> clientsTarget = new HashMap<>(idToSocketMap);
            clientsTarget.remove(clientId);

            DataOutputStream out;

            for (Object o : clientsTarget.values()) {
                Socket socket = (Socket) o;

                out = new DataOutputStream(socket.getOutputStream());
                out.writeBytes("Client num " + clientId + ":" + line + "\n\r");
                out.flush();

            }

        }


    }

}
