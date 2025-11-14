import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Page_planner extends AbstractPage implements ActionListener {


    //GUI components
    JTable table;

    JButton updateButton;

    JTextField taskNumberField;
    JTextField descriptionField;

    JLabel taskNumberLabel = new JLabel("Task ID");
    JLabel descriptionLabel = new JLabel("Description");

    //table components
    String[] columns = new String[]{"Task ID.","Date", "Time", "Description"};
    String[][] data;
    Object[] row;


    Page_planner(String userID){
        //initialization
        frame = new JFrame();
        //GUI components
        table = new JTable();

        button1 = new JButton("Add");
        button2 = new JButton("Delete");
        updateButton = new JButton("Update");
        undoBackButton = new JButton("Back");

        field1 = new JTextField(); //date field
        field2 = new JTextField(); //time field
        taskNumberField = new JTextField();
        descriptionField = new JTextField();

        label1 = new JLabel("Date"); //date label
        label2 = new JLabel("Time"); //time label
        taskNumberLabel = new JLabel("Task ID");
        descriptionLabel = new JLabel("Description");

        //table components
        columns = new String[]{"Task ID.","Date", "Time", "Description"};

        //makes the user id persistent
        this.userID = userID;

        //creates a model for the table and sets the column names
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);

        //sets the model on the table
        table.setModel(model);

        // Change A JTable Background Color, Font Size, Font Color, Row Height
        table.setBackground(Color.LIGHT_GRAY);
        table.setForeground(Color.black);
        Font font = new Font("",1,22);
        table.setFont(font);
        table.setRowHeight(30);

        //GUI components layout
        taskNumberField.setBounds(20, 220, 100, 25);
        field1.setBounds(20, 250, 100, 25);
        field2.setBounds(20, 280, 100, 25);
        descriptionField.setBounds(20, 310, 100, 25);

        taskNumberLabel.setBounds(150, 220, 100, 25);
        label1.setBounds(150, 250, 100, 25);
        label2.setBounds(150, 280, 100, 25);
        descriptionLabel.setBounds(150, 310, 100, 25);

        button1.setBounds(250, 220, 100, 25);
        button1.setFocusable(false);

        updateButton.setBounds(250, 265, 100, 25);
        updateButton.setFocusable(false);

        button2.setBounds(250, 310, 100, 25);
        button2.setFocusable(false);

        undoBackButton.setBounds(400, 265,100,25);
        undoBackButton.setFocusable(false);
        undoBackButton.addActionListener(this);

        //creates JScrollPane
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(0, 0, 880, 200);

        //frame settings
        frame.setSize(900,400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.setResizable(false);

        frame.add(pane);
        frame.add(button1);
        frame.add(updateButton);
        frame.add(button2);
        frame.add(undoBackButton);
        frame.add(taskNumberField);
        frame.add(field1);
        frame.add(field2);
        frame.add(descriptionField);
        frame.add(taskNumberLabel);
        frame.add(label1);
        frame.add(label2);
        frame.add(descriptionLabel);

        //creates an array of objects to set the data for the rows
        row = new Object[4];

        //adds the persistent information into the table

        try{
//            this.data = dataDownload("src\\planner.csv");
            this.data = dataDownload("planner.csv");

            if(data.length > 0){

                for(int i = 0; i<data.length; i++){
                    for(int j = 0; j<data[i].length; j++){
                        row[j] = data[i][j];
                    }
                    model.addRow(row);
                }
            }
        } catch (Exception e){
            System.out.println("An error occurred while downloading from the persistent file, cause = file could be empty");
        }


        //button actions


        // button add row
        button1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {

                //checks if the date is valid or in the correct format
                DateValidator validator2 = new DateValidatorUsingDateFormat("MM/dd/yyyy");
                boolean answer = validator2.isValid(field1.getText());
                System.out.println(answer + " = is date valid?");

                // validation checks:
                // if the fields are filled out
                // if the date is in the correct format using the boolean output from the previous validation check
                // if timme is in the correct format
                if(Objects.equals(taskNumberField.getText(), "") || Objects.equals(field1.getText(), "") || Objects.equals(field2.getText(), "") || Objects.equals(descriptionField.getText(), "")){
                    JOptionPane.showMessageDialog(null, "field(s) empty");
                } else if(!answer){
                    JOptionPane.showMessageDialog(null, "Wrong date format(MM/dd/yyyy)");
                }else if(!(field2.getText().matches("\\d{2}:\\d{2}"))){
                    JOptionPane.showMessageDialog(null, "Wrong time format(00:00)");
                }else {
                    row[0] = taskNumberField.getText();
                    row[1] = field1.getText();
                    row[2] = field2.getText();
                    row[3] = descriptionField.getText();

                    // add row to the model
                    model.addRow(row);

                    //saves the file
//                saveData(table, "src\\planner.csv");
                    saveData(table, "planner.csv");
                }
            }
        });

        // button remove row
        button2.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {

                // i = the index of the selected row
                int i = table.getSelectedRow();
                if(i >= 0){
                    // remove a row from jtable
                    model.removeRow(i);
                }
                else{
                    System.out.println("Delete Error");
                }

//                saveData(table, "src\\planner.csv");
                saveData(table, "planner.csv");
            }
        });

        // get selected row data From table to textfields
        table.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e){

                // i = the index of the selected row
                int i = table.getSelectedRow();

                taskNumberField.setText(model.getValueAt(i, 0).toString());
                field1.setText(model.getValueAt(i, 1).toString());
                field2.setText(model.getValueAt(i, 2).toString());
                descriptionField.setText(model.getValueAt(i, 3).toString());
            }
        });

        // button update row
        updateButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {

                // i = the index of the selected row
                int i = table.getSelectedRow();

                if(i >= 0)
                {
                    //checks if the date is valid or in the correct format
                    DateValidator validator2 = new DateValidatorUsingDateFormat("MM/dd/yyyy");
                    boolean answer = validator2.isValid(field1.getText());
                    System.out.println(answer + " = is date valid?");

                    // validation checks:
                    // if the fields are filled out
                    // if the date is in the correct format using the boolean output from the previous validation check
                    // if timme is in the correct format
                    if(Objects.equals(taskNumberField.getText(), "") || Objects.equals(field1.getText(), "") || Objects.equals(field2.getText(), "") || Objects.equals(descriptionField.getText(), "")){
                        JOptionPane.showMessageDialog(null, "field(s) empty");
                    } else if(!answer){
                        JOptionPane.showMessageDialog(null, "Wring date format(MM/dd/yyyy)");
                    }else if(!(field2.getText().matches("\\d{2}:\\d{2}"))){
                        JOptionPane.showMessageDialog(null, "Wring time format(00:00)");
                    } else {
                        model.setValueAt(taskNumberField.getText(), i, 0);
                        model.setValueAt(field1.getText(), i, 1);
                        model.setValueAt(field2.getText(), i, 2);
                        model.setValueAt(descriptionField.getText(), i, 3);
                    }
                }
                else{
                    System.out.println("Update Error");
                }
                //saves the file
//                saveData(table, "src\\planner.csv");
                saveData(table, "planner.csv");
            }
        });
    }

    //downloads data from a csv file into an array
    public static String[][] dataDownload(String file){
        java.util.List<java.util.List<String>> list = new ArrayList<List<String>>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] headers = line.split(",");
        for(String header: headers) {
            List<String> subList = new ArrayList<String>();
            subList.add(header);
            list.add(subList);
        }
        while(true) {
            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] elems = line.split(",");
            for(int i = 0; i < elems.length; i++) {
                list.get(i).add(elems[i]);
            }
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int rows = list.size();
        int cols = list.get(0).size();

        System.out.println(list + " --1");
        System.out.println(rows);
        System.out.println(cols);

        String[][] array2D = new String[rows][cols];
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                array2D[row][col] = list.get(row).get(col);
            }
        }

        System.out.println(Arrays.deepToString(array2D) + " --2");

        //rearranges the array into a compatible format

        String[][] improvedArray = new String[array2D[0].length][array2D.length];

        for(int i = 0; i < improvedArray.length; i++){
            int counter = 0;
            for (int j = 0; j<improvedArray[i].length; j++){
                improvedArray[i][j] = array2D[counter][i];
                counter++;
            }
        }

        System.out.println(Arrays.deepToString(improvedArray) + " --improved");

        return improvedArray;
    }

    //extracts data from the JTable in a form of a String array
    public static String[][] getTableData (JTable table) {
        TableModel dtm = table.getModel();
        int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
        String[][] tableData = new String[nRow][nCol];
        for (int i = 0 ; i < nRow ; i++)
            for (int j = 0 ; j < nCol ; j++)
                tableData[i][j] = (String) dtm.getValueAt(i,j);
        return tableData;
    }

    public void saveData(JTable table, String filePath){

        String[][] testArray = getTableData(table);

        try {
            FileWriter writer = new FileWriter(filePath);

            for(int i = 0; i < testArray.length; i++){
                for(int j = 0; j< testArray[i].length; j++){
                    writer.write(testArray[i][j]+",");
                }
                writer.write("\n");
            }

            writer.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //back button
        if(e.getSource() == undoBackButton){
            frame.dispose();
            Page_main page = new Page_main(userID);
        }
    }
}
