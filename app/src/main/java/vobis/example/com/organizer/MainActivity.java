package vobis.example.com.organizer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    Calendar calendar;
    int month;
    int year;
    View myView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar = Calendar.getInstance();
        month =  Calendar.MONTH - 1;
        year = getYear(calendar.getTime().toString());
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        setHeadLine(days);
        setMainTheme(days);
    }

    public void setMainTheme(int days){
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        if(myView!=null)mainLayout.removeView(myView);
        myView = new MyView(this,days,calendar);
        mainLayout.addView(myView);
    }

    public static Integer getYear(String date){
        String[] dateParts = date.split(" ");
        return Integer.parseInt(dateParts[5]);
    }

    public void setHeadLine(int days){
        TextView hello = (TextView) findViewById(R.id.text);
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        hello.setText("Today is : " + strDate);
    }

    public void activatePrev(View view){
        calendar.set(Calendar.MONTH,month - 1);
        if(month == 0) {
            calendar.set(Calendar.MONTH,11);
            calendar.set(Calendar.YEAR,year - 1);
            year --; month = 11;
        }
        else {
            calendar.set(Calendar.MONTH,month - 1);
            month --;
        }
        int days = calendar.getActualMaximum(Calendar.DATE);
        setMainTheme(days);
    }

    public void activateNext(View view){
        calendar.set(Calendar.MONTH,month + 1);
        if(month ==11) {
            calendar.set(Calendar.MONTH,0);
            calendar.set(Calendar.YEAR,year +1);
            year ++; month = 0;
        }
        else {
            calendar.set(Calendar.MONTH,month + 1);
            month ++;
        }
        int days = calendar.getActualMaximum(Calendar.DATE);
        setMainTheme(days);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
