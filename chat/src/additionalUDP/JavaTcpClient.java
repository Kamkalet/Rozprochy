package additionalUDP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class JavaTcpClient {

    public static void main(String[] args) throws IOException {

        System.out.println("Chat Client");
        String hostName = "localhost";
        int portNumber = 12345;
        Socket socket;
        socket = new Socket(hostName, portNumber);
        System.out.println("Server port " + socket.getPort());
        System.out.println("My port " +socket.getLocalPort());
        new ResponseThread(socket).start();

        MulticastSocket multicastSocket = new MulticastSocket(5555);
        InetAddress group = InetAddress.getByName("230.0.0.0");
        multicastSocket.joinGroup(group);
        new MulticastResponseThread(multicastSocket).start();


        DatagramSocket udpSocket = new  DatagramSocket(socket.getLocalPort());
        new UDPResponseThread( udpSocket).start();


        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try {
            // create socket

            while(!socket.isClosed()){

                String line = buffer.readLine();
                if(line.equals("U")){
                    byte[] sendBuffer = "BYTES".getBytes();
                    InetAddress address = InetAddress.getByName("localhost");
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, portNumber);
                    udpSocket.send(sendPacket);
                } else if(line.equals("M")){
                    byte[] sendBuffer = "BYTES".getBytes();
                    InetAddress address = InetAddress.getByName("230.0.0.0");
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, 5555);
                    multicastSocket.send(sendPacket);
                }
                else {
                    out.println(line);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null){
                socket.close();
            }
        }
    }

    public static class UDPResponseThread extends Thread {

        private DatagramSocket socket;

        UDPResponseThread(DatagramSocket clientSocket) {
            this.socket = clientSocket;
        }

        public void run() {

            try {

                while(true){
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    socket.receive(receivePacket);
                    String response = new String(receivePacket.getData());
                    System.out.println(response);
                }

            } catch (IOException e) {
                System.out.println("Server closed");
                //e.printStackTrace();
            } finally {
                socket.close();
            }

        }

    }

    public static class MulticastResponseThread extends Thread {

        private MulticastSocket socket;

        MulticastResponseThread(MulticastSocket clientSocket) {
            this.socket = clientSocket;
        }

        public void run() {

            try {

                while(true){
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    socket.receive(receivePacket);
                    String response = new String(receivePacket.getData());
                    System.out.println(response);
                }

            } catch (IOException e) {

            } finally {
                socket.close();
            }

        }

    }

    public static class ResponseThread extends Thread {

        private Socket socket;

        ResponseThread(Socket clientSocket) {
            this.socket = clientSocket;
        }

        public void run() {

            try {

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String response;
                while(!socket.isClosed()){
                    response = in.readLine();
                    System.out.println(response);
                }


            } catch (Exception e) {
                System.out.println("Server closed");
                //e.printStackTrace();
            } finally {
                if (socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }

}
