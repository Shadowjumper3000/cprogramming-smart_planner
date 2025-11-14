import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

//https://www.youtube.com/watch?v=Hiv3gwJC5kw

public class Page_logIn extends AbstractPage implements ActionListener {


    Page_logIn(HashMap<String, String> logInFoOriginal){

        //initialization of Frame elements
        frame = new JFrame();
        button1 = new JButton("Login");
        button2 = new JButton("Reset");
        undoBackButton = new JButton("Cancel");
        field1 = new JTextField();
        passwordField1 = new JPasswordField(); //the cool dots field
        label1 = new JLabel("userID");
        label2 = new JLabel("Password");
        messageLabel = new JLabel();


        //saves the hash map into a local one that is accessible to other functios within the class
        loginInfo = logInFoOriginal;

        //user components
        label1.setBounds(50,100,75,25);
        label2.setBounds(50,150,75,25);

        messageLabel.setBounds(125,300,250,35);
        messageLabel.setFont(new Font(null,Font.ITALIC,25));

        field1.setBounds(125, 100, 200, 25);
        passwordField1.setBounds(125, 150, 200,25);

        button1.setBounds(125,200,100,25);
        button1.setFocusable(false);
        button1.addActionListener(this);

        button2.setBounds(225,200,100,25);
        button2.setFocusable(false);
        button2.addActionListener(this);

        undoBackButton.setBounds(175,250,100,25);
        undoBackButton.setFocusable(false);
        undoBackButton.addActionListener(this);


        //design of the frame
        frame.add(label1);
        frame.add(label2);
        frame.add(messageLabel);
        frame.add(field1);
        frame.add(passwordField1);
        frame.add(button1);
        frame.add(button2);
        frame.add(undoBackButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
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

        //cancel button
        if(e.getSource() == undoBackButton){
            frame.dispose(); //removes the login frame
            Page_land page = new Page_land();
        }

        if(e.getSource() == button1){
            String userID = field1.getText();
            String password = String.valueOf(passwordField1.getPassword()); //gets the password and converts it to a string due to it being a password field

            //fetches the user id and checks if the associated password matches the one stored in the hash map
            if(loginInfo.containsKey(userID)){
                if(loginInfo.get(userID).equals(password)){
                    messageLabel.setForeground(Color.green);
                    messageLabel.setText("Login successful");
                    frame.dispose(); //removes the login frame
                    Page_main welcomePage = new Page_main(userID);
                }
                //when password associated with username is incorrect
                else{
                    messageLabel.setForeground(Color.red);
                    messageLabel.setText("Wrong password");
                }
            }
            //when username is invalid
            else {
                messageLabel.setForeground(Color.red);
                messageLabel.setText("Username not found");
            }
        }

    }

}
