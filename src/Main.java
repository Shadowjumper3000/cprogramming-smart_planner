import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        //checks if preliminary files are already present and if not then it creates those files

        //login_info file
        try {
            File myObj = new File("login_info.csv");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //planner file
        try {
            File myObj = new File("planner.csv");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //time_table file
        try {
            File myObj = new File("time_table.csv");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());

                //writes the preliminary information into the files
                try{

                    FileWriter fileWriter = new FileWriter("time_table.csv");

                    for(int i = 0; i<6; i++){
                        //null is a hidden last element of the array which is never displayed but allows the user to delete all other elements
                        //without causing the timetable model to crash
                        fileWriter.write("00:00,,,,,,null,\n");
                    }

                    fileWriter.close();

                } catch (IOException e){
                    System.out.println("an error during writing occured");
                    e.printStackTrace();
                }

            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //time_table2 file
        try {
            File myObj = new File("time_table2.csv");
            if (myObj.createNewFile()) {

                System.out.println("File created: " + myObj.getName());

                //writes the preliminary information into the files
                try{

                    FileWriter fileWriter = new FileWriter("time_table2.csv");

                    for(int i = 0; i<6; i++){
                        //null is a hidden last element of the array which is never displayed but allows the user to delete all other elements
                        //without causing the timetable model to crash
                        fileWriter.write("00:00,,,,,,null,\n");
                    }

                    fileWriter.close();

                } catch (IOException e){
                    System.out.println("an error during writing occurred");
                    e.printStackTrace();
                }

            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //opens the landing page
        Page_land page = new Page_land();

    }
}


