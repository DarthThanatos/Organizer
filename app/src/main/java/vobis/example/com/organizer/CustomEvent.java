package vobis.example.com.organizer;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static vobis.example.com.organizer.CalendarManager.parseDate;

public class CustomEvent extends Subscriber{

    EditText name;
    EditText desc;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ev);
        initView();
    }

    public void addEvent(View view){
        if((name.getText().toString()).equals("")) Toast.makeText(this,"You should type the name of your event",Toast.LENGTH_LONG).show();
        else{
            String nameOFEvent = name.getText().toString();
            String descOfEvent = desc.getText().toString();
            final SharedPreferences memory = getSharedPreferences("DB", MODE_PRIVATE);
            final SharedPreferences.Editor editor = memory.edit();
            String typesAtDate = memory.getString(parseDate(getDate()), "");
            editor.putString(parseDate(getDate()),typesAtDate.replace("Custom Event\n","") + "Custom Event\n" );
            String eventsInType = memory.getString(parseDate(getDate()) + "Custom Event", "");
            editor.putString(parseDate(getDate()) + "Custom Event", eventsInType.replace(nameOFEvent + "\n", "") + nameOFEvent + "\n");
            editor.putString(parseDate(getDate())+"Custom Event" + nameOFEvent,descOfEvent);
            editor.commit();
            final SharedPreferences calendar = getSharedPreferences("calendar", MODE_PRIVATE);
            final SharedPreferences.Editor calendarEditor = calendar.edit();
            ColorManager color = new ColorManager();
            calendarEditor.putString(parseDate(getDate()),color.colorsMap.get("Custom Event"));
            calendarEditor.commit();
            Toast.makeText(this,"You added your own event",Toast.LENGTH_SHORT).show();
        }
    }

    public void initView(){
        TextView date = (TextView) findViewById(R.id.date);
        name = (EditText) findViewById(R.id.name);
        desc = (EditText) findViewById(R.id.desc);
        date.setText("Creating event on " + getDate());
        setCurrentTimeOnView();
    }

    private TimePicker timePicker1;
    private int hour;
    private int minute;

    public void setCurrentTimeOnView() {
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        final Calendar c = Calendar.getInstance();
        hour = 12;
        minute = 0;
        timePicker1.setCurrentHour(hour);
        timePicker1.setCurrentMinute(minute);

    }

    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;
                    timePicker1.setCurrentHour(hour);
                    timePicker1.setCurrentMinute(minute);
                }
            };

    public String parseTimeUnit(int unit){
        String parsedUnit;
        if(unit < 10) parsedUnit = "0" + unit; else parsedUnit = Integer.toString(unit);
        return parsedUnit;
    }

    public void setAlert(View view){
        String nameOFEvent = name.getText().toString();
        String descOfEvent = desc.getText().toString();
        String parsedMinute = parseTimeUnit(timePicker1.getCurrentMinute());
        String parsedHour = parseTimeUnit(timePicker1.getCurrentHour());
        String alertDate = parseDate(getDate()) + " " + parsedHour + ":" + parsedMinute;
        if(!nameOFEvent.equals("")){
            if (currentBeforeEvent()){
                SharedPreferences memory = getSharedPreferences("Notifiers", MODE_PRIVATE);
                SharedPreferences.Editor editor = memory.edit();
                boolean alreadyCreated = memory.getBoolean(alertDate, false);
                Toast.makeText(this, "You may subscribe this event",Toast.LENGTH_LONG).show();
                final AsyncForAlert async = new AsyncForAlert(CustomEvent.this,alertDate,nameOFEvent,descOfEvent);
                if(!alreadyCreated){
                    runOnUiThread(new Runnable(){
                        public void run(){
                            async.execute();
                        }
                    });
                    editor.putBoolean(alertDate, true);
                    editor.commit();
                    Toast.makeText(context,"Created notification",Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(context,"You've already created notification at this time",Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, "Cannot set this notifier, date already passed",Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(this, "Fill the name gap!",Toast.LENGTH_LONG).show();
    }

    public boolean currentBeforeEvent(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try{
            c.setTime(sdf.parse(parseDate(getDate())));
            c.add(Calendar.DATE, 1);  // number of days to add
            Date currentDate = new Date();
            if(!currentDate.after( sdf.parse(sdf.format(c.getTime())))) return true;
            else return false;
        }catch(Exception e){};
        return false;
    }
}
