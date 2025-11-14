import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class IDandPassword extends AbstractPage{

    //initlazing a hashmap called logininfo containing two strings
    HashMap<String, String> logininfo = new HashMap<String, String>();

    IDandPassword(){

        //loads the information from the persistent file into the abstract file

        //stores the location of the file in the string variable
//        String file = "src\\login_info.csv";
        String file = "login_info.csv";

        //
        BufferedReader reader = null;
        String line = "";

        try{
            //finishes initilazing the BufferReader which will read the file through a new FileReader
            reader = new BufferedReader(new FileReader(file));

            //reads one line of the file
            line = reader.readLine();

            //string stored in row will be split at commas
            String[] row = line.split(",");

            //error finder
            System.out.println(Arrays.toString(row) + " -row");
            System.out.println(row.length + " -row.length()");

            //stores the decrypted username and password into the hash table which is then used by the rest of the code
            logininfo.put(decrypt(row[0], shift), decrypt(row[1], shift));

            //error finder
            System.out.println(logininfo+" -logininfo");


        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //function which will return the login in info but is also protected for privacy
    protected HashMap getLoginInfo(){

        return logininfo;
    }

}
