import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class Page_changeUsername extends AbstractPage implements ActionListener {

    //video about editing records
    //https://www.youtube.com/watch?v=TpyRKom0X_s

    String oldUserName;

    private Scanner x;

    Page_changeUsername(String oldUserName, HashMap<String, String> logInFoOriginal){

        //initialization
        frame = new JFrame();
        button1 = new JButton("Change");
        button2 = new JButton("Reset");
        undoBackButton = new JButton("Cancel");
        label1 = new JLabel("New user name");
        field1 = new JTextField();
        passwordField1 = new JPasswordField();
        label2 = new JLabel("Password");
        messageLabel = new JLabel();

        //remembers the old username
        this.oldUserName = oldUserName;

        //makes hash map global
        loginInfo = logInFoOriginal;

        //user components
        label1.setBounds(25,75,200,25);
        field1.setBounds(125,75,200,25);

        label2.setBounds(25,150,200,25);
        passwordField1.setBounds(125,150,200,25);

        button1.setBounds(125,250,100,25);
        button1.setFocusable(false);
        button1.addActionListener(this);

        button2.setBounds(225,250,100,25);
        button2.setFocusable(false);
        button2.addActionListener(this);

        undoBackButton.setBounds(175,350,100,25);
        undoBackButton.setFocusable(false);
        undoBackButton.addActionListener(this);

        messageLabel.setBounds(50,280,250,35);
        messageLabel.setFont(new Font(null,Font.ITALIC,12));

        //frame settings
        frame.add(label1);
        frame.add(field1);
        frame.add(label2);
        frame.add(passwordField1);
        frame.add(button1);
        frame.add(button2);
        frame.add(undoBackButton);
        frame.add(messageLabel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,420);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //clears the fields
        if(e.getSource()==button2){
            field1.setText("");
            passwordField1.setText("");
        }
        //redirects to main page
        if (e.getSource() == undoBackButton){
            frame.dispose(); //removes the login frame
            Page_main welcomePage = new Page_main(this.oldUserName);
        }

        //edits the record
        if(e.getSource() == button1){
            //stores the necessary data
            String passwordInput = String.valueOf(passwordField1.getPassword());
            String newID = field1.getText();
            String editTerm = oldUserName;
//            String filePath = "src\\login_info.csv";
            String filePath = "login_info.csv";

            //does the necessary checks
            if(newID.length()<5){
                messageLabel.setForeground(Color.red);
                messageLabel.setText("User name too short (5 characters minimum)");
            } else if(loginInfo.containsKey(newID)){
                messageLabel.setForeground(Color.red);
                messageLabel.setText("Username already exists");
            } else if(loginInfo.get(oldUserName).equals(passwordInput)){
                //carries out the storing

                //necessary data
                String tempFile = "temp.txt";
                File oldFile = new File(filePath);
                File newFile = new File(tempFile);
                String ID = "";
                String password = "";

                try{
                    //read write mechanisms
                    FileWriter fw = new FileWriter(tempFile, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter pw = new PrintWriter(bw);
                    x = new Scanner(new File(filePath));
                    x.useDelimiter("[,\n]");

                    //reads the file by decrypting it and checking for the command term which is the user id and once its found it stores the new encrypted userID and the old encrypted password into a new file
                    //the old file containing the old password is replaced and deleted
                    while(x.hasNext()){
                        ID = x.next();
                        password = x.next();
                        if(decrypt(ID, shift).equals(editTerm)){
                            pw.println(encrypt(newID, shift)+","+encrypt(passwordInput, shift));
                        } else {
                            pw.println(encrypt(ID, shift)+","+encrypt(password, shift));
                        }
                    }
                    x.close();
                    pw.flush();
                    pw.close();
                    oldFile.delete();
                    File dump = new File(filePath);
                    newFile.renameTo(dump);

                }
                catch(Exception i){
                    System.out.println("Error");
                    messageLabel.setForeground(Color.red);
                    messageLabel.setText("Error has occurred");
                } finally {
                    //redirects to main page
                    frame.dispose(); //removes the login frame
                    Page_main welcomePage = new Page_main(newID);
                }
            } else {
                messageLabel.setForeground(Color.red);
                messageLabel.setText("Incorrect password");
            }

        }

    }
}
