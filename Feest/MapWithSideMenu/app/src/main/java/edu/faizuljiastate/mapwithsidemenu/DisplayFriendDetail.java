package edu.faizuljiastate.mapwithsidemenu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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

public class DisplayFriendDetail extends AppCompatActivity {

    private TextView fn,usn,bd,gender,phone,email,addres;
    private ImageView img;
    private Button bt,rtsubmit;
    private RatingBar rb,rbaction;
    private String f, rtval;
    private User u;
    private LocalStorage s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_friend_detail);
        init();
        display();
    }

    private void init(){
        fn = (TextView) findViewById(R.id.displayffn);
        usn = (TextView) findViewById(R.id.displayfusn);
        gender = (TextView) findViewById(R.id.displayfgender);
        bd = (TextView) findViewById(R.id.displayfbd);
        phone = (TextView) findViewById(R.id.displayfphone);
        email = (TextView) findViewById(R.id.displayfemail);
        addres = (TextView) findViewById(R.id.displayfadd);
        img = (ImageView) findViewById(R.id.displayfavatar);
        bt = (Button) findViewById(R.id.displayfbt);
        rb = (RatingBar) findViewById(R.id.fratingBar);
        rbaction =(RatingBar) findViewById(R.id.ratingBar);
        rtsubmit = (Button) findViewById(R.id.submitRating);
        s = new LocalStorage(this);
        final Intent intent= getIntent();
        Bundle b = intent.getExtras();
        f =(String) b.get("currentfriend");
        Gson gson = new Gson();
        u = gson.fromJson(f, User.class);
        bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DisplayFriendDetail.this,FriendsListActivity.class));
            }
        });
        rtsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rtval = Float.toString(rbaction.getRating());
                new SubmitRating().execute(getString(R.string.server_ip_address)+"/updaterating");
            }
        });
    }
    private void display(){
        fn.setText(u.getFullName());
        usn.setText(u.getName());
        gender.setText(u.getGender());
        bd.setText(u.getBirthday());
        phone.setText(u.getPhone());
        email.setText(u.getEmail());
        addres.setText(u.getAddress());
        rb.setRating(Float.parseFloat(u.getRating()));

        byte[] decodedString = Base64.decode(u.getAvatar(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        img.setImageBitmap(decodedByte);
    }
    private class SubmitRating extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params[0]);

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("toUser",u.getName()));
            nameValuePair.add(new BasicNameValuePair("rateval",rtval));

            // Encoding
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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
            Toast.makeText(DisplayFriendDetail.this,s, Toast.LENGTH_LONG).show();
            startActivity(new Intent(DisplayFriendDetail.this,FriendsListActivity.class));
        }
    }
}

