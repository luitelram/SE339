package edu.faizuljiastate.mapwithsidemenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class DisplayUserDetail extends AppCompatActivity {
    private TextView fn,usn,bd,gender,phone,email,addres;
    private ImageView img;
    private Button bt,af;
    private RatingBar rb;
    private String f;
    private User u;
    private LocalStorage s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_detail);
        init();
        display();
    }
    private void init(){
        fn = (TextView) findViewById(R.id.displayufn);
        usn = (TextView) findViewById(R.id.displayuusn);
        gender = (TextView) findViewById(R.id.displayugender);
        bd = (TextView) findViewById(R.id.displayubd);
        phone = (TextView) findViewById(R.id.displayuphone);
        email = (TextView) findViewById(R.id.displayuemail);
        addres = (TextView) findViewById(R.id.displayuadd);
        img = (ImageView) findViewById(R.id.displayuavatar);
        bt = (Button) findViewById(R.id.displayubt);
        af = (Button) findViewById(R.id.frrequest);
        rb = (RatingBar) findViewById(R.id.ufratingBar);
        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        f =(String) b.get("currentuser");
        Gson gson = new Gson();
        u = gson.fromJson(f, User.class);
        bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DisplayUserDetail.this,AllUsersListActivity.class));
            }
        });
        af.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       new RequestServer().execute(getString(R.string.server_ip_address)+"/friendrequest");
                   }
               });
            }
        });
    }
    private void display(){
        s = new LocalStorage(this);
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
    private class RequestServer extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params[0]);
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("username", s.getInformation().getName()));
            nameValuePair.add(new BasicNameValuePair("friendname", u.getName()));
            nameValuePair.add(new BasicNameValuePair("friendfullname", u.getFullName()));
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
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
        }
    }
}
