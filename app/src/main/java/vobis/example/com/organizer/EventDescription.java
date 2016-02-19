package vobis.example.com.organizer;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        desc.setText("Description of the event: " + eventsFile);
        eventDesc = eventsFile;
    }

    String eventDesc;

    public void subscribeEvent(View view){
        Toast.makeText(this,"You subscribe this event",Toast.LENGTH_SHORT).show();
        final SharedPreferences memory = getSharedPreferences("DB", MODE_PRIVATE);
        final SharedPreferences.Editor editor = memory.edit();
        String typesAtDate = memory.getString(parseDate(getDate()), "");
        editor.putString(parseDate(getDate()), typesAtDate.replace(getType() + "\n", "") + getType() + "\n");
        String eventsInType = memory.getString(parseDate(getDate()) + getType(), "");
        editor.putString(parseDate(getDate()) +getType(),eventsInType.replace(getName() + "\n","") + getName() + "\n");
        editor.putString(parseDate(getDate())+getType() + getName(),eventDesc);
        editor.commit();
        final SharedPreferences calendar = getSharedPreferences("calendar", MODE_PRIVATE);
        final SharedPreferences.Editor calendarEditor = calendar.edit();
        ColorManager color = new ColorManager();
        calendarEditor.putString(parseDate(getDate()),color.colorsMap.get(getType()));
        calendarEditor.commit();
    }

    void initView(){
        TextView date = (TextView) findViewById(R.id.date);
        TextView type = (TextView) findViewById(R.id.type);
        TextView name = (TextView) findViewById(R.id.name);
        date.setText("Date of the event: "  + getDate());
        type.setText("Category: " + getType());
        name.setText("Event name: " + getName().replace(".txt",""));
    }
}
