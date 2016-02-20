package vobis.example.com.organizer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
        Toast.makeText(this,getType()+name+date,Toast.LENGTH_LONG).show();
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
        Toast.makeText(this,getType()+name+date,Toast.LENGTH_LONG).show();
        desc.setText("Description of the event: " + eventsFile);
        eventDesc = eventsFile;
    }

    String eventDesc;

    public void subscribeEvent(View view){
        Toast.makeText(this,"You subscribe this event",Toast.LENGTH_SHORT).show();
        final SharedPreferences memory = getSharedPreferences("DB", MODE_PRIVATE);
        final SharedPreferences.Editor editor = memory.edit();
        String date =  getIntent().getStringExtra("date");
        String name = getIntent().getStringExtra("name");
        String typesAtDate = memory.getString(date, "");
        editor.putString(date,typesAtDate.replace(getType() + "\n","") + getType() + "\n" );
        String eventsInType = memory.getString(date + getType(), "");
        editor.putString(date +getType(),eventsInType.replace(name + "\n","") + name + "\n");
        editor.putString(date + getType() + name,eventDesc);
        editor.commit();
        final SharedPreferences calendar = getSharedPreferences("calendar", MODE_PRIVATE);
        final SharedPreferences.Editor calendarEditor = calendar.edit();
        ColorManager color = new ColorManager();
        calendarEditor.putString(date,color.colorsMap.get(getType()));
        calendarEditor.commit();
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
    }
}
