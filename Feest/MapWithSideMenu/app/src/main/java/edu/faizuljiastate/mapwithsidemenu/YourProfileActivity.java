package edu.faizuljiastate.mapwithsidemenu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class YourProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView tvfullname,tvusername,tvage,tvgender,tvnameheader,tvnameemail,tvphone,tvemail,tvadd;
    private ImageView ivheaderavatar,avatar;
    private Button bchangepropicture;
    private RatingBar rb;
    LocalStorage s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMap(view);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        tvfullname = (TextView) findViewById(R.id.fullname_database);
        tvusername = (TextView) findViewById(R.id.username_database);
        tvage = (TextView) findViewById(R.id.age_database);
        tvgender = (TextView) findViewById(R.id.gender_database);
        tvphone = (TextView) findViewById(R.id.phone_database);
        tvemail = (TextView) findViewById(R.id.email_database);
        tvadd = (TextView) findViewById(R.id.add_database);
        rb = (RatingBar) findViewById(R.id.pratingBar);
        bchangepropicture = (Button) findViewById(R.id.change_prof_pic);
        bchangepropicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(YourProfileActivity.this,ChangeAvatarActivity.class));
            }
        });
        avatar = (ImageView) findViewById(R.id.avatar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        tvnameheader =(TextView) header.findViewById(R.id.headername);
        tvnameemail =(TextView)header.findViewById(R.id.headeremail);
        ivheaderavatar = (ImageView) header.findViewById(R.id.headeravatar);
        s = new LocalStorage(this);
    }
    @Override
    public void onStart(){
        super.onStart();
        tvfullname.setText(s.getInformation().getFullName());
        tvusername.setText(s.getInformation().getName());
        tvgender.setText(s.getInformation().getGender());
        rb.setRating(Float.valueOf(s.getInformation().getRating()));
        tvage.setText(s.getInformation().getBirthday());
        tvnameheader.setText(s.getInformation().getFullName());
        tvnameemail.setText(s.getInformation().getEmail());
        tvphone.setText(s.getInformation().getPhone());
        tvemail.setText(s.getInformation().getEmail());
        tvadd.setText(s.getInformation().getAddress());
        byte[] decodedString = Base64.decode(s.getInformation().getAvatar(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ivheaderavatar.setImageBitmap(decodedByte);
        avatar.setImageBitmap(decodedByte);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_sign_out) {
            s.setLogout();
            startActivity(new Intent(this,LoginActivity.class));
        }
        if(id == R.id.menu_notification)
        {
            startActivity(new Intent(this,NotifsListActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

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
        }
        else if (id == R.id.invitation) {
            Intent intent = new Intent(this, Invitation.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onMap (View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
