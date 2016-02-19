package vobis.example.com.organizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EventsByDateManager extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evbydate_manager);
        setContentOnCenter();
        setHeadLine();
    }

    Context context = this;

    public String getDate(){
        String message = getIntent().getStringExtra("date");
        final SharedPreferences memory = getSharedPreferences("EventsByDateManager", MODE_PRIVATE);
        final SharedPreferences.Editor editor = memory.edit();
        if (message != null){
            editor.putString("date",message);
            editor.commit();
        }
        else message = memory.getString("date",null);
        return message;
    }

    private void setContentOnCenter(){
        Display display = getWindowManager().getDefaultDisplay();
        float screenHeight = display.getHeight();
        float screenWidth = display.getWidth();
        Button subscribe = (Button) findViewById(R.id.subscribe);
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Subscriber.class);
                intent.putExtra("date",getDate());
                startActivity(intent);
            }
        });
        Button check = (Button) findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LocalDBGuide.class);
                intent.putExtra("date",getDate());
                startActivity(intent);
            }
        });
        float buttonHeight = 50;
        subscribe.setY(screenHeight / 2 - 2 * buttonHeight);
        check.setY(screenHeight / 2 - buttonHeight);
    }

    public void setHeadLine(){
        TextView dateInfo = (TextView) findViewById(R.id.date_exposure);
        Intent intent = getIntent();
        dateInfo.setText("Today is : " + getDate());
    }
}
