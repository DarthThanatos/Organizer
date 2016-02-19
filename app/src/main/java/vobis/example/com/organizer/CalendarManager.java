package vobis.example.com.organizer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarManager {

    public static Integer getYear(String date){
        String[] dateParts = date.split(" ");
        return Integer.parseInt(dateParts[5]);
    }

    public static String getMonthAndYear(String date){
        String [] dateParts = date.split(" ");
        return dateParts[1] + " " + dateParts[5];
    }

    public static String parseDate(String dateToParse){
        DateFormat originalFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date date = originalFormat.parse(dateToParse);
            return targetFormat.format(date);

        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
