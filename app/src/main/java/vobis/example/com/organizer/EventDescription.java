package vobis.example.com.organizer;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static vobis.example.com.organizer.CalendarManager.curBefEvBase;
import static vobis.example.com.organizer.CalendarManager.parseDate;

public class EventDescription extends ChildOfSubsriber {

    String key = "EventDescription";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ev_desc);
        initView();
        Connectic connectic = new Connectic(this,key,dayDate+getType()+getName());
        String link = "https://raw.githubusercontent.com/DarthThanatos/OrganizerDB/master/DB%20date-type-event/" + dayDate + "/" + getType() + "/" + getName();
        //Toast.makeText(this,dayDate+getType()+getName(),Toast.LENGTH_LONG).show();
        if(connectic.checkInternetConenction()) {
            connectic.downloadDatabase(link);
        }
    }

    public String getName(){
        String message = getIntent().getStringExtra("name");
        final SharedPreferences memory = getSharedPreferences("EventDescription", MODE_PRIVATE);
        final SharedPreferences.Editor editor = memory.edit();
        if (message != null){
            editor.putString("name",message);
            editor.commit();
        }
        else message = memory.getString("name",null);
        return message;
    }

    @Override
    public void setView(){
        TextView desc = (TextView) findViewById(R.id.desc);
        final SharedPreferences memory = getSharedPreferences(key, MODE_PRIVATE);
        String eventsFile = memory.getString(dayDate+getType()+getName(),"");
        //Toast.makeText(this,dayDate+getType()+getName(),Toast.LENGTH_LONG).show();
        desc.setText("Description of the event:\n" + eventsFile);
        eventDesc = eventsFile;
    }

    String eventDesc;

    public void subscribeEvent(View view){
        if(currentBeforeEvent()) {
            Toast.makeText(this, "You subscribe this event", Toast.LENGTH_SHORT).show();
            final SharedPreferences memory = getSharedPreferences("DB", MODE_PRIVATE);
            final SharedPreferences.Editor editor = memory.edit();
            String typesAtDate = memory.getString(parseDate(getDate()), "");
            editor.putString(parseDate(getDate()), typesAtDate.replace(getType() + "\n", "") + getType() + "\n");
            String eventsInType = memory.getString(parseDate(getDate()) + getType(), "");
            editor.putString(parseDate(getDate()) + getType(), eventsInType.replace(getName() + "\n", "") + getName() + "\n");
            editor.putString(parseDate(getDate()) + getType() + getName().replace(".txt", ""), eventDesc);
            editor.commit();
            final SharedPreferences calendar = getSharedPreferences("calendar", MODE_PRIVATE);
            final SharedPreferences.Editor calendarEditor = calendar.edit();
            ColorManager color = new ColorManager();
            calendarEditor.putString(parseDate(getDate()), color.colorsMap.get(getType()));
            calendarEditor.commit();
        }
        else Toast.makeText(this, "You cannot subscribe this event, its date passed", Toast.LENGTH_SHORT).show();
    }

    void initView(){
        TextView date = (TextView) findViewById(R.id.date);
        TextView type = (TextView) findViewById(R.id.type);
        TextView name = (TextView) findViewById(R.id.name);
        date.setText("Date of the event: "  + getDate());
        type.setText("Category: " + getType());
        name.setText("Event name: " + getName().replace(".txt", ""));
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
        String parsedMinute = parseTimeUnit(timePicker1.getCurrentMinute());
        String parsedHour = parseTimeUnit(timePicker1.getCurrentHour());
        String alertDate = parseDate(getDate()) + " " + parsedHour + ":" + parsedMinute;
        Toast.makeText(this,alertDate,Toast.LENGTH_SHORT).show();
        if(!getName().equals("")){
            if (curBefEvBase(alertDate)){
                SharedPreferences memory = getSharedPreferences("Notifiers", MODE_PRIVATE);
                SharedPreferences.Editor editor = memory.edit();
                boolean alreadyCreated = memory.getBoolean(alertDate, false);
                Toast.makeText(this, "You may subscribe this event",Toast.LENGTH_LONG).show();
                final AsyncForAlert async = new AsyncForAlert(EventDescription.this,alertDate,getName().replace(".txt",""),eventDesc);
                if(!alreadyCreated){
                    setAlarm(alertDate);
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
        }catch(Exception e){}
        return false;
    }

    public void setAlarm(String dateInString){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = sdf.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String nameOFEvent = getIntent().getStringExtra("name").replace(".txt","");
        Calendar calendar = sdf.getCalendar();
        Intent myIntent = new Intent(EventDescription.this, MyReceiver.class);
        myIntent.putExtra("type",getType());
        myIntent.putExtra("name", nameOFEvent);
        myIntent.putExtra("date", dateInString);
        final int _id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(EventDescription.this, _id, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }
}
