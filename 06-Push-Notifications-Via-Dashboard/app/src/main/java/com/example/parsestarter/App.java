package com.example.parsestarter;

import android.app.Application;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class App extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.parse_app_id))
                // if defined
                .clientKey(getString(R.string.parse_client_key))
                .server(getString(R.string.parse_server_url))
                .build()
        );

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", getResources().getString(R.string.gcm_sender_id));
        installation.saveInBackground(e -> {
            if (e != null) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Pushing notifications via dashboard is ready.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}