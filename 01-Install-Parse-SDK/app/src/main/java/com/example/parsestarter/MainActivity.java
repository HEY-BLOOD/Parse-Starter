package com.example.parsestarter;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseObject firstObject = new ParseObject("TestClass");
        firstObject.put("message", "Hey ! 1 message from android. Parse is now connected");
        firstObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("MainActivity", e.getLocalizedMessage());
                } else {
                    Log.d("MainActivity", "Object saved.");
                }
            }
        });
    }

}