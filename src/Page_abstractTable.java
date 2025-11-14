import javax.swing.*;
import java.awt.*;

//table class which allows multiple Jtables to be in one window
public class Page_abstractTable extends JPanel{
    JTable jt;

    public Page_abstractTable(String[] headers, String[][] contents){

        jt = new JTable(contents, headers);

        jt.setPreferredScrollableViewportSize(new Dimension(450,100));
        jt.setFillsViewportHeight(true);

        JScrollPane jps = new JScrollPane(jt); //adds scroll bar to the table
        add(jps);
    }
    public JTable tableExtract(){
        return jt;
    }
}
