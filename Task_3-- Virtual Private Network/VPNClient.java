import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class VPNClient {

    private static final String SERVER_IP = "127.0.0.1"; // Replace with your server IP
    private static final int PORT = 5555;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, PORT);
            System.out.println("Connected to VPN Server");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Receive AES key from server
            String encodedKey = in.readLine();
            SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(encodedKey), "AES");

            String message;
            while (true) {
                System.out.print("Enter message: ");
                message = userInput.readLine();
                String encryptedMessage = encrypt(message, secretKey);
                out.println(encryptedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String encrypt(String plainText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}

