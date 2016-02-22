package vobis.example.com.organizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static vobis.example.com.organizer.CalendarManager.parseDate;


public class LocalDBGuide extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_db_guide);
        setHeadLine();
    }

    Context context = this;

    public void setHeadLine(){
        TextView hello = (TextView) findViewById(R.id.date_exposure);
        ExpandableListView dbContent = (ExpandableListView) findViewById(R.id.ParentLevel);
        String message = getDate();

        final SharedPreferences memory = getSharedPreferences("DB", MODE_PRIVATE);
        final HashMap<String,ArrayList<String>> firstLvl = new HashMap<String,ArrayList<String>>();
        final ArrayList<String> Keys = new ArrayList<String>();
        String date = parseDate(message);
        Keys.addAll(Arrays.asList(memory.getString(date, "").split("\n")));
        for (String type: Keys) {
            ArrayList<String> eventsNames = new ArrayList<String>();
            eventsNames.addAll(Arrays.asList(memory.getString(date + type, "").split("\n")));
            firstLvl.put(type, eventsNames);
        }
        if(!Keys.get(0).equals("")) //it is actually the empty list
            dbContent.setAdapter(new DBSecondLevel(this, Keys, firstLvl));
        dbContent.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(context,StoredEvents.class);
                intent.putExtra("type",Keys.get(groupPosition));
                intent.putExtra("name",firstLvl.get(Keys.get(groupPosition)).get(childPosition));
                startActivity(intent);
                return false;
            }
        });
        hello.setText("Today is : " + message);
    }

    public String getDate(){
        String message = getIntent().getStringExtra("date");
        final SharedPreferences memory = getSharedPreferences("LocalDBGuide", MODE_PRIVATE);
        final SharedPreferences.Editor editor = memory.edit();
        if (message != null){
            editor.putString("date",message);
            editor.commit();
        }
        else message = memory.getString("date",null);
        return message;
    }
}
