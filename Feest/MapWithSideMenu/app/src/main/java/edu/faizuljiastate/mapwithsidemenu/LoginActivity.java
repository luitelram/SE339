package edu.faizuljiastate.mapwithsidemenu;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    // Variable
    private Button blogin;
    private EditText edusername;
    private EditText edpassword;
    private TextView tvregisterlink;
    private TextView tvinformation;
    LocalStorage s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initial();
    }

    protected void initial() {
        blogin = (Button) findViewById(R.id.blogin);
        edusername = (EditText) findViewById(R.id.edusername);
        edpassword = (EditText) findViewById(R.id.edpassword);
        tvregisterlink = (TextView) findViewById(R.id.tvregisterlink);
        tvinformation = (TextView) findViewById(R.id.tvinformation);

        blogin.setOnClickListener(this);
        tvregisterlink.setOnClickListener(this);

        s = new LocalStorage(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.blogin:
                if (validate()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            new RequestServer().execute(getString(R.string.server_ip_address)+"/login", edusername.getText().toString(),
                                    edpassword.getText().toString());
                        }
                    });
                }

                // startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.tvregisterlink:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private class RequestServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params[0]);

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("username", params[1]));
            nameValuePair.add(new BasicNameValuePair("pass", params[2]));

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
        protected void onPostExecute(String result) {
            if (result.equals("Username not registered")||result.equals("Password not match")||result.equals("Your account has been blocked"))
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            else {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                     User u = new User(jsonObject.getString("Username"),
                            jsonObject.getString("Fullname"), jsonObject.getString("Passwd"),
                            jsonObject.getString("DOB"),jsonObject.getString("Gender"),jsonObject.getString("Address"),
                             jsonObject.getString("Phone"),jsonObject.getString("Email"),
                            jsonObject.getString("Rating"),jsonObject.getString("IsAdmin"),jsonObject.getString("Block"),jsonObject.getString("Portrait"));
                    s.login(u);
                    s.setLogin();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
              // Toast.makeText(getApplicationContext(), s.getInformation().getAddress()+"", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        }
    }

    private boolean validate() {
        boolean legal = true;
        String username = edusername.getText().toString().trim();
        String pass = edpassword.getText().toString().trim();
        if (username.isEmpty()) {
            edusername.setError("Please enter your username");
            legal = false;
        }
        if (pass.isEmpty()) {
            edpassword.setError("Please enter your password");
            legal = false;
        }
        return legal;
    }
}

