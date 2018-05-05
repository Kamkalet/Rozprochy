package additionalUDP;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Server {

    private static final int PORT = 12345;
    private int clientCounter = 1;
    private int clientIdCounter = 1;
    private Map<Integer, Socket> idToSocketMap = new ConcurrentHashMap<>();

    void startServer() {

        System.out.println("JAVA TCP SERVER");

        ServerSocket serverSocket = null;
        DatagramSocket udpSocket;

        try {

            serverSocket = new ServerSocket(PORT);
            udpSocket = new DatagramSocket(PORT);
            new UDPClientThread(udpSocket).start();
            while (true) {
                // accept client
                if(clientCounter < 4){
                    Socket clientSocket = serverSocket.accept();
                    System.out.println(clientSocket.getPort());
                    new ClientThread(clientSocket, clientIdCounter).start();
                    idToSocketMap.put(clientIdCounter, clientSocket);
                    System.out.println("Client number " + clientIdCounter + " connected.");
                    clientCounter++;
                    clientIdCounter++;
                }
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

    public class UDPClientThread extends Thread {

        private DatagramSocket socket;
        private int clientId;

        UDPClientThread(DatagramSocket clientSocket) {
            this.socket = clientSocket;
        }

        public void run() {
            try {
                byte[] receiveBuffer = new byte[1024];
                Arrays.fill(receiveBuffer, (byte) 0);
                while (true) {

                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    socket.receive(receivePacket);
                    String msg = new String(receivePacket.getData());
                    System.out.print("Message from " + receivePacket.getAddress() + " " + receivePacket.getPort());
                    System.out.println(msg);
                    synchronized (Server.this){
                        sendToOthers(receivePacket);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    socket.close();
                }
            }
        }

       void sendToOthers(DatagramPacket line) throws IOException {
            Map<Integer, Socket> clientsTarget = new HashMap<>(idToSocketMap);

            for (Object o : clientsTarget.values()) {
                Socket socket = (Socket) o;

                if(socket.getPort()!=line.getPort()){
                    byte[] sendBuffer = new String(line.getData()).getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length,
                            socket.getInetAddress()
                            , socket.getPort());
                    System.out.println("Sending UDP packet to " + socket.getInetAddress() + " " + socket.getPort());
                    this.socket.send(sendPacket);
                }

            }

        }
    }


    public class ClientThread extends Thread {

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
                        synchronized (Server.this) {
                            sendToOthers(line);
                        }

                    }
                } catch (IOException e) {
                    if (this.socket != null && !this.socket.isClosed()) {
                        try {
                            synchronized (Server.this) {
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

        public void sendToOthers(String line) throws IOException {
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