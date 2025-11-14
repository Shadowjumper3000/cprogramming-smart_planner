import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Page_timeTable extends AbstractPage implements ActionListener {

    String[] columns;
    String[][] data;
    String[][] data2;

    Page_abstractTable tableWeek1;
    Page_abstractTable tableWeek2;

    Page_timeTable(String userID){
        //initialization
        frame = new JFrame();
        button1 = new JButton("Save");
        undoBackButton = new JButton("Back");
        columns = new String[]{"Time","Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        //makes user id persistant
        this.userID = userID;

        //loading in the array with persistent data, so it can be used in the displayed tables
        //downloading data
//        data = dataDownload("src\\time_table.csv");
        data = dataDownload("time_table.csv");
        System.out.println(Arrays.deepToString(data) + " --3");

//        data2 = dataDownload("src\\time_table2.csv");
        data2 = dataDownload("time_table2.csv");
        System.out.println(Arrays.deepToString(data2) + " --4");



        //Gui components
        button1.setSize(100,25);
        button1.setFocusable(false);
        button1.addActionListener(this);

        undoBackButton.setSize(100,25);
        undoBackButton.setFocusable(false);
        undoBackButton.addActionListener(this);

        //setting up the tables from the abstractJtable class and loading them up from data read fromm the time_table.csv files
        tableWeek1 = new Page_abstractTable(columns, data);
        tableWeek2 = new Page_abstractTable(columns, data2);


        //frame settings
        frame.setSize(500,500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.setResizable(false);
        frame.add(tableWeek1);
        frame.add(tableWeek2);
        frame.add(button1);
        frame.add(undoBackButton);

    }

    //downloads data from a csv file into an array
    public static String[][] dataDownload(String file){
        java.util.List<java.util.List<String>> list = new ArrayList<java.util.List<String>>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        try {
            line = br.readLine();
            System.out.println(line + " -br.readLine()");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] headers = line.split(",");
        for(String header: headers) {
            List<String> subList = new ArrayList<String>();
            subList.add(header);
            list.add(subList);
            System.out.println(header + " - header");
            System.out.println(subList + " - subList");
        }
        while(true) {
            try {
                if ((line = br.readLine()) == null) break;
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
        System.out.println(rows + " - rows");
        System.out.println(cols + " - columns");

        String[][] array2D = new String[rows][cols];
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                array2D[row][col] = list.get(row).get(col);
            }
        }

        System.out.println(Arrays.deepToString(array2D) + " --2");

        //turns the array into a compatible format

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
        //the nCol+1 should incorporate the hidden null in the .csv file and prevent errors
        String[][] tableData = new String[nRow][nCol + 1];
        for (int i = 0 ; i < nRow ; i++)
            for (int j = 0 ; j < nCol; j++)
                tableData[i][j] = (String) dtm.getValueAt(i,j);
        System.out.println(Arrays.deepToString(tableData) + " -getTableDataMethod returned table");
        return tableData;
    }
    //bubble sorts the tables by their times aided by the 24-hour format of the times
    public static String[][] sortTableTime(String[][] table){

        int n = table.length;
        boolean swapped = true;
        while ((n>0)&&(swapped = true)){
            swapped = true;
            n--;
            for(int j = 0; j<n; j++){
                int intTemp1 = Integer.parseInt(table[j][0].replace(":", ""));
                int intTemp2 = Integer.parseInt(table[j+1][0].replace(":", ""));
                if(intTemp1>intTemp2){
                    //causes the bubbles sort to also move the contents of the days
                    for(int k = 0; k<table[j].length; k++){
                        String strTemp = table[j][k];
                        table[j][k] = table[j+1][k];
                        table[j+1][k] = strTemp;
                    }
                }
            }
        }
        System.out.println(Arrays.deepToString(table));
        return table;
    }

    //button actions
    @Override
    public void actionPerformed(ActionEvent e) {
        //back button
        if(e.getSource() == undoBackButton){
            frame.dispose(); //removes the frame
            Page_main page = new Page_main(userID);
        }

        //save button
        if(e.getSource() == button1){
            //arrays which contain the extracted information from the two tables
            String[][] testArray1 = getTableData(tableWeek1.tableExtract());
            String[][] testArray2 = getTableData(tableWeek2.tableExtract());

            //output for testing purposes
            System.out.println();
            System.out.println(Arrays.deepToString(testArray1) + " --table 1");
            System.out.println(Arrays.deepToString(testArray2) + " --table 2");

            int badIndex = 0;

            //time validation check for  timetable 1
            for(int i = 0; i<testArray1.length; i++){
                if(!(testArray1[i][0].matches("\\d{2}:\\d{2}"))){
                    badIndex++;
                }
            }

            //time validation check for timetable 2
            for(int i = 0; i<testArray2.length; i++){
                if(!(testArray2[i][0].matches("\\d{2}:\\d{2}"))){
                    badIndex++;
                }
            }

            //consequences of detected errors in the validation process
            if(badIndex>0){
                //displays error message
                JOptionPane.showMessageDialog(null, "Wrong time format(00:00) at " + badIndex + " places");
            } else {
                //creates sorted tables by time and stores them into arrays
                String[][] editedTable1 = sortTableTime(testArray1);
                String[][] editedTable2 = sortTableTime(testArray2);

                //overwrites the time_table.csv with the new information
                try {
//                FileWriter writer = new FileWriter("src\\time_table.csv");
                    FileWriter writer = new FileWriter("time_table.csv");

                    for(int i = 0; i < editedTable1.length; i++){
                        for(int j = 0; j< editedTable1[i].length; j++){
                            writer.write(editedTable1[i][j]+",");
                        }
                        writer.write("\n");
                    }

                    writer.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                //overwrites time_table2.csv with new information
                try {
//                FileWriter writer = new FileWriter("src\\time_table2.csv");
                    FileWriter writer = new FileWriter("time_table2.csv");

                    for(int i = 0; i < editedTable2.length; i++){
                        for(int j = 0; j< editedTable2[i].length; j++){
                            writer.write(editedTable2[i][j]+",");
                        }
                        writer.write("\n");
                    }

                    writer.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            //refreshes the page
            frame.dispose();
            Page_timeTable timeTable = new Page_timeTable(userID);
        }
    }
}
