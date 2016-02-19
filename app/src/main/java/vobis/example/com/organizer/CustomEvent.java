package vobis.example.com.organizer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static vobis.example.com.organizer.CalendarManager.parseDate;

public class CustomEvent extends Subscriber{

    EditText name;
    EditText desc;

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
    }
}
