package vobis.example.com.organizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChildOfBrowser extends Browser {

    String key = "ChildOfBrowser";
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        setHeadLine();
        Connectic connectic = new Connectic(this,key,getType());
        String link = "https://raw.githubusercontent.com/DarthThanatos/OrganizerDB/master/DB%20type-event-date/" + getType().replace(" ","%20") +"/metadata.txt";
        if(connectic.checkInternetConenction()) {
            connectic.downloadDatabase(link);
        }
    }

    @Override
    public void setView(){
        final SharedPreferences memory = getSharedPreferences(key, MODE_PRIVATE);
        String eventsFile = memory.getString(getType(), "");
        if(!eventsFile.equals(""))myAdapter.addToAdapter(eventsFile);
    }


    public String getType(){
        String message = getIntent().getStringExtra("type");
        final SharedPreferences memory = getSharedPreferences("ChildOfBrowser", MODE_PRIVATE);
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
        hello.setText("Select the event");
        list = (ListView) findViewById(R.id.listView);
        final SharedPreferences memory = getSharedPreferences(key, MODE_PRIVATE);
        String eventsFile = memory.getString(dayDate+getType(),"");
        myAdapter  = new EvName();
        if(list != null){
            list.setAdapter(myAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String event = ((String) list.getItemAtPosition(position)).replace(".txt","");
                    Intent intent = new Intent(context, Browser_EventDesc.class);
                    String name = event.split(" ")[0];
                    String date = event.split(" ")[1];
                    intent.putExtra("name", name);
                    intent.putExtra("date",date);
                    intent.putExtra("event",event);
                    startActivity(intent);
                }
            });
        }
    }

    class EvName extends MyCustomAdapter {

        public EvName(){
            super();
        }

        @Override
        public void setColor(ViewHolder holder, String label){
            ColorManager color = new ColorManager();
            holder.textView.setBackgroundColor(Color.parseColor(color.colorsMap.get(getType())));
        }
    }
}
