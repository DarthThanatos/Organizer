package vobis.example.com.organizer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static vobis.example.com.organizer.CalendarManager.curBefEvBase;
import static vobis.example.com.organizer.CalendarManager.parseDate;

public class Browser_EventDesc extends ChildOfBrowser{
    String key = "EventDescription_Browser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ev_desc);
        initView();
        String date =  getIntent().getStringExtra("date");
        String name = getIntent().getStringExtra("name");
        String event = getIntent().getStringExtra("event");
        Connectic connectic = new Connectic(this,key,getType()+name+date);
        String link = "https://raw.githubusercontent.com/DarthThanatos/OrganizerDB/master/DB%20type-event-date/"+getType().replace(" ","%20")+"/" + event.replace(" ","%20")+".txt";
        //Toast.makeText(this,link,Toast.LENGTH_LONG).show();
        if(connectic.checkInternetConenction()) {
            connectic.downloadDatabase(link);
        }
    }

    @Override
    public void setView(){
        TextView desc = (TextView) findViewById(R.id.desc);
        String name = getIntent().getStringExtra("name");
        String date =  getIntent().getStringExtra("date");
        final SharedPreferences memory = getSharedPreferences(key, MODE_PRIVATE);
        String eventsFile = memory.getString(getType()+name+date,"");
        desc.setText("Description of the event:\n" + eventsFile);
        eventDesc = eventsFile;
    }

    String eventDesc;

    public void subscribeEvent(View view){
        if(currentBeforeEvent()) {
            Toast.makeText(this, "You subscribe this event", Toast.LENGTH_SHORT).show();
            final SharedPreferences memory = getSharedPreferences("DB", MODE_PRIVATE);
            final SharedPreferences.Editor editor = memory.edit();
            String date = getIntent().getStringExtra("date");
            String name = getIntent().getStringExtra("name");
            String typesAtDate = memory.getString(date, "");
            editor.putString(date, typesAtDate.replace(getType() + "\n", "") + getType() + "\n");
            String eventsInType = memory.getString(date + getType(), "");
            editor.putString(date + getType(), eventsInType.replace(name + "\n", "") + name + "\n");
            editor.putString(date + getType() + name, eventDesc);
            editor.commit();
            final SharedPreferences calendar = getSharedPreferences("calendar", MODE_PRIVATE);
            final SharedPreferences.Editor calendarEditor = calendar.edit();
            ColorManager color = new ColorManager();
            calendarEditor.putString(date, color.colorsMap.get(getType()));
            calendarEditor.commit();
        }
        else Toast.makeText(this, "You cannot subscribe this event, its date passed", Toast.LENGTH_SHORT).show();
    }

    void initView(){
        TextView date = (TextView) findViewById(R.id.date);
        TextView type = (TextView) findViewById(R.id.type);
        TextView name = (TextView) findViewById(R.id.name);
        String dateOfEv =  getIntent().getStringExtra("date");
        String nameOfEv = getIntent().getStringExtra("name");
        date.setText("Date of the event: "  + dateOfEv);
        type.setText("Category: " + getType());
        name.setText("Event name: " + nameOfEv.replace(".txt", ""));
        setCurrentTimeOnView();
    }

    public boolean currentBeforeEvent(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String dateOfEvent =  getIntent().getStringExtra("date");
        try{
            c.setTime(sdf.parse(dateOfEvent));
            c.add(Calendar.DATE, 1);  // number of days to add
            Date currentDate = new Date();
            if(!currentDate.after( sdf.parse(sdf.format(c.getTime())))) return true;
            else return false;
        }catch(Exception e){};
        return false;
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
        String date =  getIntent().getStringExtra("date");
        String name = getIntent().getStringExtra("name");
        String alertDate = date + " " + parsedHour + ":" + parsedMinute;
        if(!name.equals("")){
            if (curBefEvBase(alertDate)){
                final AsyncForAlert async = new AsyncForAlert(Browser_EventDesc.this,alertDate,name.replace(".txt",""),eventDesc);
                SharedPreferences memory = getSharedPreferences("Notifiers", MODE_PRIVATE);
                SharedPreferences.Editor editor = memory.edit();
                boolean alreadyCreated = memory.getBoolean(alertDate, false);
                if(!alreadyCreated) {
                    setAlarm(alertDate);
                    editor.putBoolean(alertDate, true);
                    editor.commit();
                    Toast.makeText(context,"Created notification",Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(context,"You've alreade created notification at this time",Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, "Cannot set this notifier, date already passed",Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(this, "Fill the name gap!",Toast.LENGTH_LONG).show();
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
        Intent myIntent = new Intent(Browser_EventDesc.this, MyReceiver.class);
        myIntent.putExtra("type",getType());
        myIntent.putExtra("name", nameOFEvent);
        myIntent.putExtra("date", dateInString);
        final int _id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Browser_EventDesc.this, _id, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }
}
