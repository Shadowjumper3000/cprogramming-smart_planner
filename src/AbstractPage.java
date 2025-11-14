import javax.swing.*;
import java.util.HashMap;

public class AbstractPage {
    JFrame frame;
    JButton button1;
    JButton button2;
    JButton undoBackButton;
    JTextField field1;
    JTextField field2;
    JPasswordField passwordField1;
    JPasswordField passwordField2;
    JLabel label1;
    JLabel label2;
    JLabel messageLabel;
    String userID;

    HashMap<String, String> loginInfo;

    int shift = 101;

    //https://github.com/thomascoe/java-encryption/blob/master/CaesarCipher.java
    public static String encrypt(String original, int shift) {
        String encrypted = "";
        for (int i = 0; i < original.length(); i++) {
            int c = original.charAt(i) + shift;
            if (c > 126) {
                c -= 95;
            } else if (c < 32) {
                c += 95;
            }
            encrypted += (char) c;
        }
        return encrypted;
    }
    public static String decrypt(String encrypted, int shift) {
        return encrypt(encrypted, -shift);
    }
}
