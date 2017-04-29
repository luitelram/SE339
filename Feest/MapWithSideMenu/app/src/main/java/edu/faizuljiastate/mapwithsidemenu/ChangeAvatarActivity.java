package edu.faizuljiastate.mapwithsidemenu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ChangeAvatarActivity extends AppCompatActivity {
    private Button takephoto,upload,save;
    private ImageView image;
    private Bitmap imagebitmap;
    LocalStorage s;
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private final int REQUEST_UPLOAD_IMAGE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);
        init();
        addEvent();
    }
    private void init(){
        takephoto = (Button) findViewById(R.id.takephoto);
        upload = (Button) findViewById(R.id.upload);
        save = (Button) findViewById(R.id.save);
        image = (ImageView) findViewById(R.id.imageupdate);
        imagebitmap = null;
        s = new LocalStorage(this);
        save.setEnabled(false);
    }
    private void addEvent(){
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_UPLOAD_IMAGE);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new RequestServer().execute(getString(R.string.server_ip_address)+"/updateavatar");
                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imagebitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(imagebitmap);
            save.setEnabled(true);
        }
        else if(requestCode == REQUEST_UPLOAD_IMAGE && resultCode == RESULT_OK){
            try {
                Uri imageuri = data.getData();
                InputStream is = getContentResolver().openInputStream(imageuri);
                imagebitmap = BitmapFactory.decodeStream(is);
                image.setImageBitmap(imagebitmap);
                save.setEnabled(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    private class RequestServer extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params[0]);


            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("username",s.getInformation().getName()));
            nameValuePair.add(new BasicNameValuePair("avatar",imageToStringByte(imagebitmap)));
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

            Toast.makeText(getApplicationContext(),"Your avatar have been updated",Toast.LENGTH_LONG).show();
            s.setAvatar(result);
            startActivity(new Intent(ChangeAvatarActivity.this,YourProfileActivity.class));
        }
    }
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
}
