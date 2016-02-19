package vobis.example.com.organizer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static vobis.example.com.organizer.CalendarManager.parseDate;

public class StoredEvents extends LocalDBGuide {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stored_events);
        initView();
    }


    public void deleteEvent(View view){
        Toast.makeText(this,"Event successfully deleted from memory",Toast.LENGTH_SHORT).show();
        final SharedPreferences memory = getSharedPreferences("DB", MODE_PRIVATE);
        final SharedPreferences.Editor editor = memory.edit();
        String type = getIntent().getStringExtra("type");
        String name = getIntent().getStringExtra("name");
        String eventsInType = memory.getString(parseDate(getDate()) + type, "");
        String removedEvent = eventsInType.replace(name + "\n", "");
        if(removedEvent.equals("")) {
            editor.putString(parseDate(getDate()) + type,null );
            String typesAtDate = memory.getString(parseDate(getDate()), "");
            String removedType = typesAtDate.replace(type + "\n", "");
            if(removedType.equals(""))editor.putString(parseDate(getDate()),null);
            else editor.putString(parseDate(getDate()),removedType);
        }
        else editor.putString(parseDate(getDate()) + type, removedEvent);
        editor.commit();
        final SharedPreferences calendar = getSharedPreferences("calendar", MODE_PRIVATE);
        final SharedPreferences.Editor calendarEditor = calendar.edit();
        ColorManager color = new ColorManager();
        String[] types = memory.getString(parseDate(getDate()), "").split("\n");
        if (types[0].equals("")) calendarEditor.putString(parseDate(getDate()),"#CD5C5C"); // default color
        else calendarEditor.putString(parseDate(getDate()),color.colorsMap.get(types[types.length-1]));
        calendarEditor.commit();
    }

    void initView(){
        TextView date = (TextView) findViewById(R.id.date);
        TextView type = (TextView) findViewById(R.id.type);
        TextView name = (TextView) findViewById(R.id.name);
        TextView desc = (TextView) findViewById(R.id.desc);
        String eventName = getIntent().getStringExtra("name").replace(".txt", "");
        String eventType = getIntent().getStringExtra("type");
        date.setText("Date of the event: "  + getDate());
        type.setText("Category: " + eventType);
        name.setText("Event name: " + eventName);
        desc.setText("Description: " + getSharedPreferences("DB", MODE_PRIVATE).getString(parseDate(getDate())+eventType+eventName,""));
    }

}
