import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;

public class VPNServer {

    private static final int PORT = 5555;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("VPN Server is running on port " + PORT);

            // Generate AES key
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                new Thread(new ClientHandler(clientSocket, secretKey)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private SecretKey secretKey;

        public ClientHandler(Socket clientSocket, SecretKey secretKey) {
            this.clientSocket = clientSocket;
            this.secretKey = secretKey;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Send AES key to client
                String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
                out.println(encodedKey);

                String encryptedMessage;
                while ((encryptedMessage = in.readLine()) != null) {
                    String decryptedMessage = decrypt(encryptedMessage, secretKey);
                    System.out.println("Received (decrypted): " + decryptedMessage);
                }

                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        }
    }
}
