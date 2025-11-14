//https://www.youtube.com/watch?v=HgkBvwgciB4&t=387s

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Page_land extends AbstractPage implements ActionListener {

    Page_land(){

        //initialization
        frame = new JFrame();
        button1 = new JButton("Sign up");
        button2 = new JButton("Log in");

        //button1 settings
        button1.setBounds(100,200,200,40);
        button1.setFocusable(false);
        button1.addActionListener(this);
        frame.add(button1);

        //button2 settings
        button2.setBounds(100, 100, 200, 40);
        button2.setFocusable(false);
        button2.addActionListener(this);
        frame.add(button2);

        //enabling or disabling buttons based on if login_info.csv file containing data
        try {
//            FileReader reader = new FileReader("src\\login_info.csv");
            FileReader reader = new FileReader("login_info.csv");

            int data = reader.read();

            System.out.println(data + " -data read from login_info.csv");

            if (data > 0){
                button1.setEnabled(false);
            } else {
                button2.setEnabled(false);
            }

            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,420);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == button1){
            frame.dispose(); //disposes of the launch page

            //does the necesary steps to make signUp work
            IDandPassword iDandPassword = new IDandPassword();
            Page_signUp signUp = new Page_signUp(iDandPassword.getLoginInfo());
        }else if(e.getSource() == button2){
            frame.dispose(); //disposes of the launch page

            //does the necessary steps to make the login work
            IDandPassword iDandPassword = new IDandPassword();
            Page_logIn logIn = new Page_logIn(iDandPassword.getLoginInfo());
        }

    }
}
