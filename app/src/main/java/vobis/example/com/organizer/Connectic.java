package vobis.example.com.organizer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Connectic {

    Subscriber subscriber;
    Browser browser;
    String key;
    String keyOfEvent;

    public Connectic(Context context, String key, String keyOfEvent){
        if(context instanceof Browser) this.browser = (Browser)context;
        else this.subscriber = (Subscriber) context;
        this.key = key;
        this.keyOfEvent = keyOfEvent;
    }

    public String decodeString(InputStream in){ //do not use this!!!
        BufferedReader br= new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public void downloadDatabase(String urlStr) {
        final String url = urlStr;

        new Thread() {
            public void run() {
                InputStream in = null;
                Message msg = Message.obtain();
                msg.what = 1;
                try {
                    in = openHttpConnection(url);
                    if (in != null) {
                        BufferedReader br= new BufferedReader(new InputStreamReader(in));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                            Bundle b = new Bundle();
                            b.putString("file", sb.toString());
                            msg.setData(b);
                            in.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                messageHandler.sendMessage(msg);
            }
        }.start();
    }
    private InputStream openHttpConnection(String urlStr) {
        InputStream in = null;
        int resCode = -1;
        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();
            //creates an instance of URLConnection() pointing to the resource specified by the given URL
            if (!(urlConn instanceof HttpURLConnection))
                throw new IOException("URL is not an Http URL");
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.connect();
            resCode = httpConn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK)
                in = httpConn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;
    }

    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String decodedMsg = msg.getData().getString("file");
            if(decodedMsg != null) {
                final SharedPreferences memory;
                if(subscriber != null) memory = subscriber.getSharedPreferences(key, subscriber.MODE_PRIVATE);
                else memory = browser.getSharedPreferences(key, browser.MODE_PRIVATE);
                final SharedPreferences.Editor editor = memory.edit();
                editor.putString(keyOfEvent,decodedMsg);
                editor.commit();
                if(subscriber != null) subscriber.setView();
                else browser.setView();
            }
        }
    };


    public boolean checkInternetConenction() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec ;
        if(subscriber != null) connec =(ConnectivityManager)subscriber.getSystemService(browser.CONNECTIVITY_SERVICE);
        else connec =(ConnectivityManager)browser.getSystemService(browser.CONNECTIVITY_SERVICE);
        // Check for network connections
        if (connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == android.net.NetworkInfo.State.CONNECTED ||
            connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == android.net.NetworkInfo.State.CONNECTING ||
            connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == android.net.NetworkInfo.State.CONNECTING ||
            connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == android.net.NetworkInfo.State.CONNECTED ) {
                if(subscriber != null) Toast.makeText(subscriber, " Connected ", Toast.LENGTH_SHORT).show();
                else Toast.makeText(browser, " Connected ", Toast.LENGTH_LONG).show();
                return true;
        }else if (
                connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            if(subscriber != null) Toast.makeText(subscriber, "Not Connected ", Toast.LENGTH_LONG).show();
            else Toast.makeText(browser, "Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}
