package edu.faizuljiastate.mapwithsidemenu;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

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

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {
    // Variable
    private Button createEvent;
    private EditText eventaddress;
    private EditText eventname;
    private EditText eventTime;
    private EditText eventDate;
    private EditText eventMinAge;
    private EditText eventDescription;
    private String userID;
    private RadioGroup rgtype;

    private LocalStorage s;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API. See
     * https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        initial();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        s = new LocalStorage(this);
        userID = s.getInformation().getName();
    }

    protected void initial() {

        createEvent = (Button) findViewById(R.id.createEventBtn);
        eventname = (EditText) findViewById(R.id.ID_event_name);
        eventaddress = (EditText) findViewById(R.id.ID_eventAddress);
        eventTime = (EditText) findViewById(R.id.ID_event_time);
        eventDate = (EditText) findViewById(R.id.ID_event_date);
        eventMinAge = (EditText) findViewById(R.id.ID_min_age);
        eventDescription = (EditText) findViewById(R.id.eddescription);
        rgtype = (RadioGroup) findViewById(R.id.rgeventtype);
        createEvent.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createEventBtn:
                if (validate()) {
                    int selectedId = rgtype.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    final RadioButton rbeventtype = (RadioButton) findViewById(selectedId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new CreateEventActivity.RequestServer().execute(getString(R.string.server_ip_address)+"/createevent", userID, eventname.getText().toString(),eventaddress.getText().toString(),
                                    eventDate.getText().toString() , eventTime.getText().toString() , eventMinAge.getText().toString(),eventDescription.getText().toString(),rbeventtype.getText().toString());
                        }
                    });
                }

                break;
        }
    }

    //////////////
    private class RequestServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params[0]);

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("userID", params[1]));
            nameValuePair.add(new BasicNameValuePair("eventName", params[2]));
            nameValuePair.add(new BasicNameValuePair("eventAddress", params[3]));
            nameValuePair.add(new BasicNameValuePair("eventDate", params[4]));
            nameValuePair.add(new BasicNameValuePair("eventTime", params[5]));
            nameValuePair.add(new BasicNameValuePair("eventMinAge", params[6]));
            nameValuePair.add(new BasicNameValuePair("eventDesc", params[7]));
            nameValuePair.add(new BasicNameValuePair("eventType", params[8]));
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
        protected void onPostExecute(String s) {
            Toast.makeText(CreateEventActivity.this,s,Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CreateEventActivity.this,MainActivity.class));
        }


    }
    private boolean validate(){
        boolean legal = true;
        String name    = eventname.getText().toString().trim();
        String date    = eventDate.getText().toString().trim();
        String time    = eventTime.getText().toString().trim();
        String address = eventaddress.getText().toString().trim();
        String minAge  = eventMinAge.getText().toString().trim();

        if(name.isEmpty()){
            eventname.setError("Please enter an event name");
            legal = false;
        }
        if(date.isEmpty()){
            eventDate.setError("Please enter an event username");
            legal = false;
        }
        if(time.isEmpty()){
            eventTime.setError("Please enter an event time");
            legal = false;
        }
        if(address.isEmpty())
        {
            eventaddress.setError("Please enter an event address");
            legal = false;
        }
        if(minAge.isEmpty())
        {
            eventMinAge.setError("Please enter your email");
            legal = false;
        }

        return legal;
    }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("CreateEvent Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }
    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


}
