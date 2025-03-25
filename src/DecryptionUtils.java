import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DecryptionUtils {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY = "0123456789abcdef"; // Must match encryption key
    private static final String IV = "abcdef9876543210"; // Must match encryption IV
    private static final String filePath = System.getProperty("user.home") + "/Downloads/";

    private static SecretKey getSecretKey() {
        return new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "AES");
    }

    private static IvParameterSpec getIv() {
        return new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
    }

    public static String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), getIv());
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData.trim());
            return new String(cipher.doFinal(decodedBytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Error decrypting data: " + e.getMessage());
            return "";
        }
    }

    public static void decryptFile(String logFileName) {
        File file = new File(filePath + logFileName);

        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String outputFilePath = filePath + logFileName.replace(".json", "_decrypted.json");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                reader.lines().forEach(line -> {
                    String decryptedText = decrypt(line);
                    try {
                        System.out.println(decryptedText);
                        writer.write(decryptedText);
                        writer.newLine();
                    } catch (IOException e) {
                        System.err.println("Error writing to file: " + e.getMessage());
                    }
                });
                System.out.println("Decrypted data written to: " + outputFilePath);
            } catch (IOException e) {
                System.err.println("Error creating output file: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}