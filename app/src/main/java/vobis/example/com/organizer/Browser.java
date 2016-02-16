package vobis.example.com.organizer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class Browser extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        System.out.println("\n\n\n\n working\n\n\n\n");
        setHeadLine();
        setBackground();
    }

    public void setBackground(){
        ImageView background = (ImageView) findViewById(R.id.background);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.raw.coat_of_arms);
        background.setImageBitmap(bitmap);
    }

    public void setHeadLine(){
        TextView hello = (TextView) findViewById(R.id.date_exposure);
        Intent intent = getIntent();
        String message = intent.getStringExtra("date");
        hello.setText("Today is : " + message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}



