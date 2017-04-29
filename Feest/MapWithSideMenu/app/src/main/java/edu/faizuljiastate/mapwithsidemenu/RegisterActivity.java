package edu.faizuljiastate.mapwithsidemenu;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Patterns;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    // Variable
    private Button bregister;
    private EditText edusername;
    private EditText edpassword;
    private EditText edfullname;
    private EditText edconfirmpassword;
    private EditText edbirthday;
    private EditText edphone;
    private EditText edemail;
    private EditText edaddress;
    private RadioGroup rggender;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API. See
     * https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initial();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    protected void initial() {
        bregister = (Button) findViewById(R.id.bregister);
        edusername = (EditText) findViewById(R.id.edusername);
        edpassword = (EditText) findViewById(R.id.edpassword);
        edfullname = (EditText) findViewById(R.id.edfullname);
        edconfirmpassword = (EditText) findViewById(R.id.edconfirmpassword);
        edbirthday = (EditText) findViewById(R.id.edbirthday);
        edphone = (EditText) findViewById(R.id.edphone);
        edemail = (EditText)findViewById(R.id.edemail);
        edaddress = (EditText) findViewById(R.id.edaddress);
        rggender = (RadioGroup) findViewById(R.id.rbgender);
        bregister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bregister:
                if (validate()) {

                    int selectedId = rggender.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    final RadioButton rbgender = (RadioButton) findViewById(selectedId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new RequestServer().execute(getString(R.string.server_ip_address)+"/register", edusername.getText().toString(),
                                    edfullname.getText().toString(), edpassword.getText().toString(),
                                    edbirthday.getText().toString(), edphone.getText().toString(),edemail.getText().toString(),
                                    edaddress.getText().toString(),rbgender.getText().toString());
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
            //Data to be sent
            Bitmap avatar = BitmapFactory.decodeResource(getResources(),android.R.drawable.ic_btn_speak_now);
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("username", params[1]));
            nameValuePair.add(new BasicNameValuePair("fullname", params[2]));
            nameValuePair.add(new BasicNameValuePair("pass", params[3]));
            nameValuePair.add(new BasicNameValuePair("dob", params[4]));
            nameValuePair.add(new BasicNameValuePair("phone", params[5]));
            nameValuePair.add(new BasicNameValuePair("email", params[6]));
            nameValuePair.add(new BasicNameValuePair("address",params[7]));
            nameValuePair.add(new BasicNameValuePair("gender",params[8]));
            nameValuePair.add(new BasicNameValuePair("avatar",imageToStringByte(avatar)));

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
            if (s.equals("Username has been existed")) {
                Toast.makeText(getApplicationContext(), s,
                        Toast.LENGTH_LONG).show();
                edusername.setError("Username has been used.Please choose another");
            }
            if (s.equals("Register successfully"))
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        }
    }
    private boolean validate(){
        boolean legal = true;
        String fullname = edfullname.getText().toString().trim();
        String username = edusername.getText().toString().trim();
        String pass = edpassword.getText().toString().trim();
        String confirmpass = edconfirmpassword.getText().toString().trim();
        String birthday = edbirthday.getText().toString().trim();
        String phone = edphone.getText().toString().trim();
        String email = edemail.getText().toString().trim();
        String adress = edaddress.getText().toString().trim();

        if(fullname.isEmpty()){
            edfullname.setError("Please enter your full name");
            legal = false;
        }
        if(username.isEmpty()){
            edusername.setError("Please enter your username");
            legal = false;
        }
        if(pass.isEmpty()){
            edpassword.setError("Please enter your password");
            legal = false;
        }
        if(!pass.isEmpty() && pass.length()<8){
            edpassword.setError("Password is 8 characters long");
            legal = false;
        }
        if(confirmpass.isEmpty()){
            edconfirmpassword.setError("Please confirm your password");
            legal = false;
        }
        if(!confirmpass.equals(pass)&& !confirmpass.isEmpty()){
            edconfirmpassword.setError("Password is not matched");
            legal = false;
        }
        if(birthday.isEmpty())
        {
            edbirthday.setError("Please enter your birthday");
            legal = false;
        }

        if(phone.isEmpty()||!Patterns.PHONE.matcher(phone).matches())
        {
            edphone.setError("Please enter your valid phone");
            legal = false;
        }
        if(email.isEmpty())
        {
            edemail.setError("Please enter your email");
            legal = false;
        }
        if(adress.isEmpty())
        {
            edemail.setError("Please enter your address");
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
                .setName("Register Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
    /**************************Helper Method************************/
    private String imageToStringByte(Bitmap image){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int width = image.getWidth(),height = image.getHeight(),stdwidth = 70,stdheight = 70;
        float ratioBitmap = (float) width / (float) height;
        stdheight = (int)((float)stdwidth/ratioBitmap);
        image = Bitmap.createScaledBitmap(image,stdwidth,stdheight,true);
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray,Base64.DEFAULT);
    }
//    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
//        if (maxHeight > 0 && maxWidth > 0) {
//            int width = image.getWidth();
//            int height = image.getHeight();
//            float ratioBitmap = (float) width / (float) height;
//            float ratioMax = (float) maxWidth / (float) maxHeight;
//
//            int finalWidth = maxWidth;
//            int finalHeight = maxHeight;
//            if (ratioMax > 1) {
//                finalWidth = (int) ((float)maxHeight * ratioBitmap);
//            } else {
//                finalHeight = (int) ((float)maxWidth / ratioBitmap);
//            }
//            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
//            return image;
//        } else {
//            return image;
//        }
//    }
    /*****************************************************************/
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
