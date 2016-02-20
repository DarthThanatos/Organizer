package vobis.example.com.organizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static vobis.example.com.organizer.CalendarManager.parseDate;

public class Browser extends ActionBarActivity {
    String key = "Browser";
    String dayDate;
    String keyOfEvent;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber);
        setHeadLine();
        Connectic connectic = new Connectic(this,key,"browser");
        String link = "https://raw.githubusercontent.com/DarthThanatos/OrganizerDB/master/DB%20type-event-date/folderInfo.txt";
        if(connectic.checkInternetConenction()) {
            connectic.downloadDatabase(link);
        }
    }

    ListView list;
    MyCustomAdapter myAdapter;

    public void setView() {
        final SharedPreferences memory = getSharedPreferences(key, MODE_PRIVATE);
        String eventsFile = memory.getString("browser", "");
        myAdapter.addToAdapter(eventsFile);
    }

    class MyCustomAdapter extends BaseAdapter {

        private ArrayList mData = new ArrayList();
        private LayoutInflater mInflater;

        public MyCustomAdapter() {
            mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addToAdapter(String eventsFile){
            mData = new ArrayList();
            String[] events = eventsFile.split("\n");
            Arrays.sort(events);
            ArrayList<String> eventsList = new ArrayList<String>();
            eventsList.addAll(Arrays.asList(events));
            for (String event : eventsList) addItem(event);
        }

        public void setColor(ViewHolder holder, String label){
            ColorManager color = new ColorManager();
            holder.textView.setBackgroundColor(Color.parseColor(color.colorsMap.get(label)));
        }

        public void addItem(final String item) {
            mData.add(item);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return (String) mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            System.out.println("getView " + position + " " + convertView);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.activity_browser_elem, null);
                holder = new ViewHolder();
                holder.textView = (TextView)convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            String label = ((String)mData.get(position)).replace(".txt","");
            holder.textView.setText(label);
            setColor(holder,label);
            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView textView;
    }

    public void setHeadLine(){
        TextView hello = (TextView) findViewById(R.id.date_exposure);
        hello.setText("Select category");
        list = (ListView) findViewById(R.id.listView);
        myAdapter = new MyCustomAdapter();
        if(list != null) {
            list.setAdapter(myAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String event = ((String) list.getItemAtPosition(position));
                    Intent intent = new Intent(context, ChildOfBrowser.class);
                    intent.putExtra("type", event);
                    startActivity(intent);
                }
            });
        }
    }
}
