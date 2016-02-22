package vobis.example.com.organizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static vobis.example.com.organizer.CalendarManager.parseDate;

public class MyReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service1 = new Intent(context, MyAlarmService.class);
        String name = intent.getStringExtra("name");
        String type = intent.getStringExtra("type");
        String date = intent.getStringExtra("date");
        Serializable manager = intent.getSerializableExtra("QueryManagerObject");
        service1.putExtra("type", type);
        service1.putExtra("name", name);
        service1.putExtra("date", date);
        service1.putExtra("QueryManagerObject",manager);
        context.startService(service1);
    }
}