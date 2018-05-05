package chat;

import java.io.*;
import java.net.Socket;

public class JavaTcpClient {

    public static void main(String[] args) throws IOException {

        System.out.println("JAVA TCP CLIENT");
        String hostName = "localhost";
        int portNumber = 12345;
        Socket socket;
        socket = new Socket(hostName, portNumber);
        new ResponseThread(socket).start();
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try {
            // create socket

            while(!socket.isClosed()){

                String line = buffer.readLine();

                out.println(line);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null){
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
