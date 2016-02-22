package vobis.example.com.organizer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public boolean currentBeforeEvent(String dateInString){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try{
            c.setTime(sdf.parse(parseDate(dateInString)));
            c.add(Calendar.DATE, 1);  // number of days to add
            Date currentDate = new Date();
            if(!currentDate.after( sdf.parse(sdf.format(c.getTime())))) return true;
            else return false;
        }catch(Exception e){};
        return false;
    }

    public static boolean curBefEvBase(String evDate){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = sdf.parse(evDate);
            Date currentDate = new Date();
            if(!currentDate.after( date) ) return true;
        }catch(Exception e){};
        return false;
    }

    public static boolean isDateValid(String date){
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
