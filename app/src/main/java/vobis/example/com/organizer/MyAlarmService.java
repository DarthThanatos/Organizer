package vobis.example.com.organizer;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MyAlarmService extends Service
{

    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId){
        super.onStart(intent, startId);
        String name = intent.getStringExtra("name");
        String type = intent.getStringExtra("type");
        String date = intent.getStringExtra("date");
        try {
            Uri notificationSound = Uri.parse("android.resource://vobis.example.com.organizer/" + R.raw.notification);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notificationSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(),MainActivity.class);
        Notification notification = new Notification(R.drawable.ic_launcher,"New event takes place today! Tonight's the night", System.currentTimeMillis());

        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(this.getApplicationContext(), "Type: " + type, "Name: " + name, pendingNotificationIntent);
        mManager.notify(0, notification);
    }

    @Override
    public void onDestroy(){
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
