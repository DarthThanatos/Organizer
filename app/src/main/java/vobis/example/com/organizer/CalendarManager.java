package vobis.example.com.organizer;

public class CalendarManager {

    public static Integer getYear(String date){
        String[] dateParts = date.split(" ");
        return Integer.parseInt(dateParts[5]);
    }

    public static String getMonthAndYear(String date){
        String [] dateParts = date.split(" ");
        return dateParts[1] + " " + dateParts[5];
    }

}
