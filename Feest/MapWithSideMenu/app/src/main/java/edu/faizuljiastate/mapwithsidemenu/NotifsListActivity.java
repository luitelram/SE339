package edu.faizuljiastate.mapwithsidemenu;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.vision.text.Text;
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

public class NotifsListActivity extends AppCompatActivity {
    private ListView notiflist;
    private TextView dismissall;
    private LocalStorage s;
    private ArrayList<Notification> notifs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifs_list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotifsListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        notiflist=(ListView)findViewById(R.id.notifslist);
        dismissall = (TextView) findViewById(R.id.dismissall);
        s = new LocalStorage(this);
        notifs = new ArrayList<Notification>();

        dismissall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new DismissAllRequest().execute(getString(R.string.server_ip_address)+"/dismissallnotifs");
                    }
                });
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new RequestServer().execute(getString(R.string.server_ip_address)+"/getnofitication");
            }
        });

        notiflist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
//                Gson gson = new Gson();
//                String json = gson.toJson(notifs.get(+position));
                if(notifs.get(position).getType().equals("F")){
                    final View layout = View.inflate(NotifsListActivity.this,R.layout.friend_req_dialog, null);
                    AlertDialog.Builder b = new AlertDialog.Builder(NotifsListActivity.this);
                    b.setView(layout);
                    TextView fdiaglog = (TextView) layout.findViewById(R.id.fdialog);
                    fdiaglog.setText("'"+notifs.get(position).getFromUser()+"' sents you a friend request");

                    b.setCancelable(true)
                            .setPositiveButton("Accept",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //TODO
                                }
                            });
                    b.setNegativeButton("Decline",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new DeclineFriend().execute(getString(R.string.server_ip_address)+"/declinefriend",notifs.get(position).getId());
                                }
                            });
                        }
                    });
                    Dialog d = b.create();
                    d.show();
                }
                else{
                    final View layout = View.inflate(NotifsListActivity.this,R.layout.friend_req_dialog, null);
                    AlertDialog.Builder b = new AlertDialog.Builder(NotifsListActivity.this);
                    b.setView(layout);
                    TextView fdiaglog = (TextView) layout.findViewById(R.id.fdialog);
                    fdiaglog.setText("'"+notifs.get(position).getFromUser()+"' invites you to an event");

                    b.setCancelable(true)
                            .setPositiveButton("Accept",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //TODO
                                }
                            });
                    b.setNegativeButton("Decline",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO
                        }
                    });
                    Dialog d = b.create();
                    d.show();
                }
//                Intent intent = new Intent(FriendsListActivity.this, DisplayFriendDetail.class);
//                intent.putExtra("currentfriend", json);
//                startActivity(intent);
            }
        });

    }
    private class RequestServer extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params[0]);
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("username", s.getInformation().getName()));
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
            try {
                JSONArray NoticeArray = new JSONArray(s);
                if(NoticeArray.length()==0){
                    dismissall.setText("No notifications at this time");
                    dismissall.setClickable(false);
                }
                else{
                    for(int i = 0; i<NoticeArray.length(); i++){
                        JSONObject obj = NoticeArray.getJSONObject(i);
                        notifs.add(new Notification(obj.getString("NotificationID"),obj.getString("FromUser"),obj.getString("ToUser"),obj.getString("NotificationType"),
                                obj.getString("EventCurrentID")));
                    }
                    NotifView adapter = new NotifView(NotifsListActivity.this,R.layout.activity_notifs_adapter,notifs);
                    notiflist.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class DismissAllRequest extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params[0]);
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("username", s.getInformation().getName()));
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
            finish();
            startActivity(getIntent());
            return null;
        }
    }

    private class DeclineFriend extends  AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params[0]);
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("did", params[1]));
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
//            finish();
//            startActivity(getIntent());
            return result;
        }
        @Override
        protected void onPostExecute(String s){
            Toast.makeText(NotifsListActivity.this,s,Toast.LENGTH_LONG).show();
        }
    }
}
