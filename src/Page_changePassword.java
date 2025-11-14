import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class Page_changePassword extends AbstractPage implements ActionListener {

    //video about editing records
    //https://www.youtube.com/watch?v=TpyRKom0X_s


    private Scanner x;

    Page_changePassword(HashMap<String, String> logInFoOriginal, String userID){

        //initialization
        frame = new JFrame();
        button1 = new JButton("Change");
        button2 = new JButton("Reset");
        undoBackButton = new JButton("Cancel");
        passwordField1 = new JPasswordField();
        passwordField2 = new JPasswordField();
        label1 = new JLabel("New Password 1");
        label2 = new JLabel("New Password 2");
        messageLabel = new JLabel();

        //keeping the ID persistent
        this.userID = userID;

        //making login info global
        loginInfo = logInFoOriginal;

        //user components
        label1.setBounds(50,100,75,25);
        passwordField1.setBounds(125,100,200,25);

        label2.setBounds(50,150,75,25);
        passwordField2.setBounds(125,150,200,25);

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
        frame.add(passwordField1);
        frame.add(label2);
        frame.add(passwordField2);
        frame.add(button1);
        frame.add(button2);
        frame.add(undoBackButton);
        frame.add(messageLabel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,420);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //clears the fields
        if(e.getSource()==button2){
            passwordField1.setText("");
            passwordField2.setText("");
        }
        //redirects to main page
        if (e.getSource() == undoBackButton){
            frame.dispose(); //removes the login frame
            Page_main welcomePage = new Page_main(this.userID);
        }
        //edits the record
        if(e.getSource() == button1){
            //stores the necessary data
            String password1 = String.valueOf(passwordField1.getPassword());
            String password2 = String.valueOf(passwordField2.getPassword());
            String newID = this.userID;
            String editTerm = this.userID;
//            String filePath = "src\\login_info.csv";
            String filePath = "login_info.csv";

            //does the necessary checks
            if(!password1.equals(password2)){
                messageLabel.setForeground(Color.red);
                messageLabel.setText("Passwords do not match");
            } else if(password1.length()<5){
                messageLabel.setForeground(Color.red);
                messageLabel.setText("Password too short (5 characters minimum)");
            }  else {
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

                    //reads the file by decrypting it and checking for the command term which is the user id and once its found it stores the new encrypted password and the old encrypted user id into a new file
                    //the old file containing the old password is replaced and deleted
                    while(x.hasNext()){
                        ID = x.next();
                        password = x.next();
                        if(decrypt(ID, shift).equals(editTerm)){
                            pw.println(encrypt(newID, shift)+","+encrypt(password1, shift));
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
                    JOptionPane.showMessageDialog(null, "An error occurred");
                } finally {
                    //redirects to main page
                    frame.dispose(); //removes the login frame
                    Page_main welcomePage = new Page_main(this.userID);
                }
            }
        }
    }
}
