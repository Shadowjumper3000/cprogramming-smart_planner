import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Page_main extends AbstractPage implements ActionListener {

    JMenuBar menuBar;
    JMenu profileMenu;
    JMenuItem changeProfileName;
    JMenuItem changeProfilePassword;

    Page_main(String userID){
        //initilization
        frame = new JFrame();
        button1 = new JButton("Planner");
        button2 = new JButton("Time Table");

        //to keep the user ID persistant
        this.userID = userID;

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

        //initializing menu bar the components
        menuBar = new JMenuBar();
        profileMenu = new JMenu(userID);
        changeProfileName = new JMenuItem("Change profile name");
        changeProfilePassword = new JMenuItem("Change profile password");
        changeProfileName.addActionListener(this);
        changeProfilePassword.addActionListener(this);

        //adding to the menu
        menuBar.add(profileMenu);
        profileMenu.add(changeProfileName);
        profileMenu.add(changeProfilePassword);

        //setting up the frame
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,420);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);

        //keyboard shortcut
        changeProfilePassword.setMnemonic(KeyEvent.VK_P);
        changeProfileName.setMnemonic(KeyEvent.VK_N);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == changeProfileName){
            IDandPassword loginInfo = new IDandPassword();
            Page_changeUsername page = new Page_changeUsername(this.userID, loginInfo.getLoginInfo());
            frame.dispose();
        }
        if(e.getSource() == changeProfilePassword){
            IDandPassword loginInfo = new IDandPassword();
            Page_changePassword page = new Page_changePassword(loginInfo.getLoginInfo(), this.userID);
            frame.dispose();
        }
        if(e.getSource() == button1){
            Page_planner page = new Page_planner(this.userID);
            frame.dispose();
        }
        if(e.getSource() == button2){
            Page_timeTable page = new Page_timeTable(userID);
            frame.dispose();
        }
    }
}
