package edu.faizuljiastate.mapwithsidemenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class MessagingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Socket csocket;
    {
        try {
            csocket = IO.socket("http://10.26.1.180:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
   // RelativeLayout activity_main;
    FloatingActionButton fab;
    private LocalStorage s;
    private TextView tvnameheader,tvnameemail;
    private EditText edmessage;
    private ListView lvmessage;
    private ArrayList<String> messagearray ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        csocket.connect();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        s = new LocalStorage(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        tvnameheader =(TextView) header.findViewById(R.id.headername);
        tvnameemail =(TextView)header.findViewById(R.id.headeremail);
        edmessage = (EditText)findViewById(R.id.input);
        lvmessage = (ListView) findViewById(R.id.list_of_message);
        messagearray = new ArrayList<String>();
        csocket.emit("client_send_username",s.getInformation().getFullName());
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                csocket.emit("client_send_chat_message",edmessage.getText().toString());
                edmessage.setText("");
            }
        });
        csocket.on("server_send_chat_message",displayChatMessage);

    }

    @Override
    public void onStart(){
        super.onStart();
        tvnameheader.setText(s.getInformation().getFullName());
        tvnameemail.setText(s.getInformation().getEmail());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out)
        {
            s.setLogout();
            csocket.disconnect();
            startActivity(new Intent(this,LoginActivity.class));
        }
        if(item.getItemId() == R.id.menu_notification)
        {
            startActivity(new Intent(this,NotifsListActivity.class));
        }
        return true;
    }
    private Emitter.Listener displayChatMessage = new Emitter.Listener(){
        @Override
        public void call(final Object...args){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messagearray.add(args[0].toString());
                    ArrayAdapter adapter = new ArrayAdapter(MessagingActivity.this,android.R.layout.simple_list_item_1,messagearray);
                   lvmessage.setAdapter(adapter);
                }
            });
        }
    };
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
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
        } else if (id == R.id.nav_acc_all_users) {
            Intent intent = new Intent(this, AllUsersListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_acc_all_events) {
            Intent intent = new Intent(this, AllEventsListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_message) {
            Intent intent = new Intent(this, MessagingActivity.class);
            startActivity(intent);
        }else if (id == R.id.invitation) {
            Intent intent = new Intent(this, Invitation.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
