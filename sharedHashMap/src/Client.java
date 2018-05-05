import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {

//    String user_name = System.getProperty("user.name", "n/a");
    private final DistributedHashMap dHM;

    Client() {
        dHM = new DistributedHashMap("clutch2");
    }

    void start() throws Exception {
        dHM.init();
        eventLoop();
        dHM.closeChannel();
    }

    private void eventLoop() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                System.out.println("c-containsKey, g-get, p-put, r-remove");
                System.out.print("> ");
                System.out.flush();
                String line = in.readLine().toLowerCase();
                validateOperation(line);
                if (line.startsWith("quit") || line.startsWith("exit")) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void validateOperation(String line) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        switch (line) {
            case "c": {
                System.out.println("Enter key to find");
                System.out.print("> ");
                String key = in.readLine().toLowerCase();
                boolean contains = dHM.containsKey(key);
                System.out.println(contains);
                break;
            }
            case "p": {
                System.out.println("Enter a key");
                System.out.print("> ");
                String key = in.readLine().toLowerCase();
                System.out.println("Enter a value");
                System.out.print("> ");
                String value = in.readLine().toLowerCase();
                String previousValue = dHM.put(key, value);
                if (previousValue != null)
                    System.out.println(previousValue);
                break;
            }
            case "g": {
                System.out.println("Enter a key to get value");
                System.out.print("> ");
                String key = in.readLine().toLowerCase();
                String previousValue = dHM.get(key);
                if (previousValue != null)
                    System.out.println(dHM.get(key));
                break;
            }
            case "r": {
                System.out.println("Enter a key to remove value");
                System.out.print("> ");
                String key = in.readLine().toLowerCase();
                String prevValue = dHM.remove(key);
                if (prevValue != null)
                    System.out.println(prevValue);
                break;
            }
        }
    }


}
