import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;

public class Page_signUp extends AbstractPage implements ActionListener {
    //video about editing csv files
    //https://www.youtube.com/watch?v=lp0xQXUEw-k

    //video about editing records
    //https://www.youtube.com/watch?v=TpyRKom0X_s


    JLabel newUserIDlabel;

    Page_signUp(HashMap<String, String> logInFoOriginal){

        //initialization
        frame = new JFrame();
        button1 = new JButton("Sign Up");
        button2 = new JButton("Reset");
        undoBackButton = new JButton("Cancel");
        field1 = new JTextField();
        passwordField1 = new JPasswordField();
        passwordField2 = new JPasswordField();
        label1 = new JLabel("Password");
        label2 = new JLabel("Password 2");
        newUserIDlabel  = new JLabel("New user ID");
        messageLabel = new JLabel();

        //makes hash table global
        loginInfo = logInFoOriginal;

        //user components
        newUserIDlabel.setBounds(50,100,75,25);
        field1.setBounds(125,100,200,25);

        label1.setBounds(50,150,75,25);
        passwordField1.setBounds(125,150,200,25);

        label2.setBounds(50,200,75,25);
        passwordField2.setBounds(125,200,200,25);

        button1.setBounds(125,250,100,25);
        button1.setFocusable(false);
        button1.addActionListener(this);

        button2.setBounds(225,250,100,25);
        button2.setFocusable(false);
        button2.addActionListener(this);

        undoBackButton.setBounds(175,300,100,25);
        undoBackButton.setFocusable(false);
        undoBackButton.addActionListener(this);

        messageLabel.setBounds(125,350,250,35);
        messageLabel.setFont(new Font(null,Font.ITALIC,12));

        //frame settings
        frame.add(newUserIDlabel);
        frame.add(field1);
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
            passwordField2.setText("");
        }
        //cancel button
        if(e.getSource() == undoBackButton){
            frame.dispose(); //removes the login frame
            Page_land page = new Page_land();
        }

        if(e.getSource() == button1){

            //stores the necessary data
            String userID = field1.getText();
            String password1 = String.valueOf(passwordField1.getPassword());
            String password2 = String.valueOf(passwordField2.getPassword());

            if(userID.length() < 5 || password1.length()<5){
                JOptionPane.showMessageDialog(null, "Password or username is too short (5 characters minimum)");
            } else {
                if(password1.equals(password2)){
                    if(loginInfo.containsKey(userID)){
                        JOptionPane.showMessageDialog(null, "Username already exists");
                    } else {
                        //adds new sign up information to the persistent file

//                        String filePath = "src\\login_info.csv";
                        String filePath = "login_info.csv";

                        try{
                            //initializing the writing mechanisms
                            //true means that we want to append, not overwrite
                            FileWriter fw = new FileWriter(filePath, true);
                            //makes sure writing is more efficient?
                            BufferedWriter bw = new BufferedWriter(fw);
                            PrintWriter pw = new PrintWriter(bw);

//                            pw.println(userID+","+password1);
                            //writes encrypted data into login file
                            pw.println(encrypt(userID, shift)+","+encrypt(password1, shift));

                            //makes sure data is written to the file
                            pw.flush();
                            pw.close();

                            JOptionPane.showMessageDialog(null, "Information saved");
                        }
                        catch (Exception i){
                            JOptionPane.showMessageDialog(null, "Information failed to save");
                        }
                        //notifies about successful sing up
                        messageLabel.setForeground(Color.green);
                        messageLabel.setText("Sign up successful");

                        //redirects to main page
                        frame.dispose(); //removes the login frame
                        Page_main welcomePage = new Page_main(userID);
                    }
                }
                else{
                    messageLabel.setForeground(Color.red);
                    messageLabel.setText("Passwords do not match");
                }
            }
        }

    }
}
