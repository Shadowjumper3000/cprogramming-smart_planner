import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateValidatorUsingDateFormat implements DateValidator {
    private String dateFormat;

    public DateValidatorUsingDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public boolean isValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);

        //checks if the date is in the correct format or if its valid and returns a boolean answer
        try {
            sdf.parse(dateStr);
            System.out.println("the date is in the correct format");
        } catch (ParseException e) {
            System.out.println("the date isnt in the correct format");
            System.out.println(e);
            return false;
        }
        return true;
    }
}
