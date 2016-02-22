package vobis.example.com.organizer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class DBSecondLevel extends BaseExpandableListAdapter{

    Context ctx;
    ArrayList<String> Keys = new ArrayList<String>();
    HashMap<String,ArrayList<String>> First = new HashMap<String,ArrayList<String>>();
    ColorManager color = new ColorManager();

    public DBSecondLevel(Context ctx, ArrayList<String> Keys, HashMap<String, ArrayList<String>> First){
        this.ctx = ctx;
        this.Keys = Keys;
        this.First = First;
    }

    @Override
    public int getGroupCount() {
        return Keys.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return First.get(Keys.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return Keys.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return First.get(Keys.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String) this.getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.activity_nested_db,null);
        }
        TextView textView = (TextView)convertView.findViewById(R.id.second_child_item);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(title);
        if(title != null && !title.equals(""))
            textView.setBackgroundColor(Color.parseColor(color.colorsMap.get(title.replace(".txt", ""))));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.activity_nested_bottom,null);
        }
        TextView textView = (TextView)convertView.findViewById(R.id.fourth_child_item);
        textView.setText(((String) this.getChild(groupPosition, childPosition)).replace(".txt", ""));
        String type = Keys.get(groupPosition);
        //textView.setBackgroundColor(Color.parseColor(color.colorsMap.get(type)));
        // if you want children to be dyed as well, uncomment the line above - but I think it is better this way :)
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}

