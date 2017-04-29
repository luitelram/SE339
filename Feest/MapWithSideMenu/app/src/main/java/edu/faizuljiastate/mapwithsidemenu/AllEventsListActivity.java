package edu.faizuljiastate.mapwithsidemenu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class AllEventsListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView list;
    private LocalStorage s;
    private TextView tvnameheader,tvnameemail;
    private ImageView ivheaderavatar;
    private ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events_list);
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllEventsListActivity.this,MainActivity.class));
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        tvnameheader =(TextView) header.findViewById(R.id.headername);
        tvnameemail =(TextView)header.findViewById(R.id.headeremail);
        ivheaderavatar = (ImageView) header.findViewById(R.id.headeravatar);

        list=(ListView)findViewById(R.id.allevtlist);
        s= new LocalStorage(this);
        events = new ArrayList<Event>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new RequestServer().execute(getString(R.string.server_ip_address)+"/displayallevents");
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        tvnameheader.setText(s.getInformation().getFullName());
        tvnameemail.setText(s.getInformation().getEmail());
        byte[] decodedString = Base64.decode(s.getInformation().getAvatar(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ivheaderavatar.setImageBitmap(decodedByte);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_sign_out) {
            s.clear();
            startActivity(new Intent(this,LoginActivity.class));
        }
        if(id == R.id.menu_notification)
        {
            startActivity(new Intent(this,NotifsListActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, YourProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_create_event) {
            Intent intent = new Intent(this, CreateEventActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_list_event) {
            Intent intent = new Intent(this, EventsListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_friends) {
            Intent intent = new Intent(this, FriendsListActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_acc_all_users) {
            Intent intent = new Intent(this, AllUsersListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_acc_all_events) {
            Intent intent = new Intent(this, AllEventsListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_message) {
            Intent intent = new Intent(this, MessagingActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private class RequestServer extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params[0]);

            String result = "";
            try {
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String s){
            try {
                JSONArray jsonEventsArray = new JSONArray(s);
                for(int i = 0; i<jsonEventsArray.length(); i++){
                    JSONObject obj = jsonEventsArray.getJSONObject(i);
                    events.add(new Event(obj.getString("EventName"),obj.getString("HostName"),obj.getString("EventAddress"),
                            obj.getString("EventTime"),obj.getString("EventDate"),obj.getString("Restriction"),
                            obj.getString("EventDescription")));
                }
                EventView adapter = new EventView(AllEventsListActivity.this,R.layout.activity_events_adapter,events);
                list.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
