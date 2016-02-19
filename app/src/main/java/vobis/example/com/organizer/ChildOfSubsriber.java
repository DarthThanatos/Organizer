package vobis.example.com.organizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static vobis.example.com.organizer.CalendarManager.parseDate;

public class ChildOfSubsriber extends Subscriber{

    String key = "ChildOfSubscriber";
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber);
        setHeadLine();
        Connectic connectic = new Connectic(this,key,dayDate+getType());
        String link = "https://raw.githubusercontent.com/DarthThanatos/OrganizerDB/master/DB%20date-type-event/" + dayDate.replace(" ","%20") + "/" + getType().replace(" ","%20") + "/metadata.txt";
        if(connectic.checkInternetConenction()) {
            connectic.downloadDatabase(link);
        }
    }

    @Override
    public void setView(){
        final SharedPreferences memory = getSharedPreferences(key, MODE_PRIVATE);
        String eventsFile = memory.getString(dayDate+getType(), "");
        if(!eventsFile.equals(""))myAdapter.addToAdapter(eventsFile);
    }


    public String getType(){
        String message = getIntent().getStringExtra("type");
        final SharedPreferences memory = getSharedPreferences("ChildOfSubscriber", MODE_PRIVATE);
        final SharedPreferences.Editor editor = memory.edit();
        if (message != null){
            editor.putString("type",message);
            editor.commit();
        }
        else message = memory.getString("type",null);
        return message;
    }

    EvName myAdapter;

    @Override
    public void setHeadLine(){
        TextView hello = (TextView) findViewById(R.id.date_exposure);
        hello.setText("Today is : " + getDate());
        list = (ListView) findViewById(R.id.listView);
        final SharedPreferences memory = getSharedPreferences(key, MODE_PRIVATE);
        String eventsFile = memory.getString(dayDate+getType(),"");
        myAdapter  = new EvName();
        if(list != null){
            list.setAdapter(myAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String event = (String) list.getItemAtPosition(position);
                    Intent intent = new Intent(context, EventDescription.class);
                    intent.putExtra("name", event);
                    startActivity(intent);
                }
            });
        }
    }

    class EvName extends MyCustomAdapter{

        public EvName(){
            super(false);
        }

        @Override
        public void setColor(ViewHolder holder, String label){
            ColorManager color = new ColorManager();
            holder.textView.setBackgroundColor(Color.parseColor(color.colorsMap.get(getType())));
        }
    }
}
