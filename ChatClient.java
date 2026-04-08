import java.io.*;
import java.net.*;
import java.util.Scanner;
public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Connected to the chat server!");
            new Thread(new IncomingReader(socket)).start();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            System.out.print("Enter your name to start: ");
            String name = scanner.nextLine();

            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                out.println(name + ": " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class IncomingReader implements Runnable {
    private Socket socket;

    public IncomingReader(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println(serverMessage);
            }
        } catch (IOException e) {
            System.out.println("Connection to server lost.");
        }
    }
}
