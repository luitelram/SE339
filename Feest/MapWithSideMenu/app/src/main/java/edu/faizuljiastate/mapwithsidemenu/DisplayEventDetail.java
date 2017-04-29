package edu.faizuljiastate.mapwithsidemenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class DisplayEventDetail extends AppCompatActivity {
    private TextView en, et, ed, eh, ea;
    private Button bt;
    private String evt;
    private Event e;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_event_detail);
        init();
        display();
    }
    private void init(){
        en = (TextView) findViewById(R.id.displayename);
        et = (TextView) findViewById(R.id.displayetime);
        ed = (TextView) findViewById(R.id.displayedate);
        eh = (TextView) findViewById(R.id.displayehost);
        ea = (TextView) findViewById(R.id.displayeaddress);

        bt = (Button) findViewById(R.id.displayubt);

        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        evt =(String) b.get("currentevtonmap");
        Gson gson = new Gson();
        e = gson.fromJson(evt, Event.class);
        bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DisplayEventDetail.this,MainActivity.class));
            }
        });

    }
    private void display(){
        en.setText(e.getName());
        et.setText(e.getTime());
        ed.setText(e.getDate());
        eh.setText(e.getHost());
        ea.setText(e.getAddress());

//        byte[] decodedString = Base64.decode(u.getAvatar(), Base64.DEFAULT);
//        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        img.setImageBitmap(decodedByte);
    }

}
