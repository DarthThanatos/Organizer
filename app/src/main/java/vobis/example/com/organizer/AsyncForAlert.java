package vobis.example.com.organizer;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static vobis.example.com.organizer.CalendarManager.isDateValid;

public class AsyncForAlert extends AsyncTask<Void,Integer,String> {

    Context context;
    Bitmap icon;
    long end;
    Date endDate;
    String title;
    String contentText;
    boolean exc;

    public AsyncForAlert(Context context, String endDate,String title, String contentText){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");//dd/MM/yyyy
        this.title = title;
        this.contentText = contentText;
        try {
            //if(!isDateValid(endDate))
                this.endDate = sdfDate.parse(endDate);
           // else this.endDate = endDate;
            end = this.endDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.context = context;
        icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);

    }

    private void createNotification() {
        Notification noti = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(contentText)
                .setTicker("Subscribed event notification! Tonight's the night")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setLargeIcon(icon)
                .setAutoCancel(true)
                .setSound(Uri.parse("android.resource://vobis.example.com.organizer/" + R.raw.notification))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, noti);
    }

    @Override
    protected String doInBackground(Void... params) {

        synchronized (this){
            Date now = new Date();
            long start = now.getTime();
            if(start >= end) exc = true;
            else
                try {
                    wait(end - start);
                    exc = false;
                    System.out.println("waiting");
                    //Toast.makeText(context,"Working",Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }



        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values){

    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected void onPostExecute(String result){
        if (!exc) createNotification();
        /*SoundPool mySound = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        int notificationId = mySound.load(context,R.raw.notification,1);
        mySound.play(notificationId, 1, 1, 1, 0, 1);*/
    }
}
