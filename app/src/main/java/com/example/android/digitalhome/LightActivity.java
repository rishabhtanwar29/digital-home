package com.example.android.digitalhome;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.jar.JarException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Rishabh Tanwar on 11/28/2017.
 */

public class LightActivity extends AppCompatActivity {

    private static Bundle bundle = new Bundle();
    ToggleButton ltoggle1;
    ToggleButton ltoggle2;

    SeekBar lseekbar1;
    SeekBar lseekbar2;

    Button btnSendHttpRequest;

    OkHttpClient client;
    Request request;

    Boolean myvar;
    Boolean myvar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lightactivity);
        myvar=bundle.getBoolean("ToggleButtonState1", false);
        myvar2=bundle.getBoolean("ToggleButtonState2", false);

        ltoggle1 = (ToggleButton) findViewById(R.id.ltoggleButton1);
        ltoggle2 = (ToggleButton) findViewById(R.id.ltoggleButton2);

        lseekbar1 = (SeekBar) findViewById(R.id.lseekBar1);
        lseekbar2 = (SeekBar) findViewById(R.id.lseekBar2);

        ltoggle1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myvar = true;
                } else {
                    myvar = false;
                }
            }
        });

        ltoggle2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    myvar2 = true;
                } else {
                    myvar2 = false;
                }
            }
        });

        btnSendHttpRequest = (Button) findViewById(R.id.sendbutton1);
        btnSendHttpRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new OkHttpClient();
                myfunction(myvar, myvar2);

            }
        });
    }


    private void myfunction(Boolean lmyvar , Boolean lmyvar2) {

        SharedPreferences tokenSharedPreferences=getSharedPreferences("myToken", Context.MODE_PRIVATE);
        String strToken = tokenSharedPreferences.getString("token", null);
        SharedPreferences idSharedPreferences=getSharedPreferences("myId", Context.MODE_PRIVATE);
        String strId = idSharedPreferences.getString("id", null);

        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        RequestBody body = RequestBody.create(mediaType,"------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"Item1Bool\"\r\n\r\n"+lmyvar+"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"Item1Value\"\r\n\r\n0\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"Item2Bool\"\r\n\r\n"+lmyvar2+"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"Item2Value\"\r\n\r\n0\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
        request = new Request.Builder()
                .url("https://home-automation-aries.herokuapp.com/api/update/"+strId+"/")
                .put(body)
                .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                .addHeader("Authorization", "Token "+strToken)
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "c15ae0f5-decd-c961-7e4b-95109a850d6a")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                /**String mMessage = response.body().string();*/
                if (response.isSuccessful()) {
                    try {

                        response= client.newCall(request).execute();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        bundle.putBoolean("ToggleButtonState1", ltoggle1.isChecked());
        bundle.putBoolean("ToggleButtonState2", ltoggle2.isChecked());

        bundle.putInt("SeekBarState1", lseekbar1.getProgress());
        bundle.putInt("SeekBarState2", lseekbar2.getProgress());

    }
    @Override
    public void onResume() {
        super.onResume();
        ltoggle1.setChecked(bundle.getBoolean("ToggleButtonState1", false));
        ltoggle2.setChecked(bundle.getBoolean("ToggleButtonState2", false));

        lseekbar1.setProgress(bundle.getInt("SeekBarState1", 100));
        lseekbar2.setProgress(bundle.getInt("SeekBarState2", 100));

    }
}

