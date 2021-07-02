package com.example.parsestarter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private static final String MESSAGE_OBJ_NAME = "Message";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String MESSAGE_DESTINATION_KEY = "destination";
    private static final String MESSAGE_CONTENT_KEY = "content";
    TextView emptyView;
    LinearLayout messageListContainer;
    FloatingActionButton fab;

    boolean isMessageEmpty = true;
    int messageNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        emptyView = findViewById(R.id.empty_view);
        messageListContainer = findViewById(R.id.message_list_container);

        ParseLiveQueryClient parseLiveQueryClient = null;
        try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory
                    .getClient(new URI(getString(R.string.parse_server_url)));
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }

        // Message - Live Query
        if (parseLiveQueryClient != null) {
            Log.d(TAG, "parseLiveQueryClient is not null.");
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(MESSAGE_OBJ_NAME);
            parseQuery.whereContains(MESSAGE_DESTINATION_KEY, "Destination of message ");
            SubscriptionHandling<ParseObject> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

            subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, object) -> {
                // handling CREATE event
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    runOnUiThread(() -> appendMessage(object));
                });
            });
        } else {
            Log.d(TAG, "ParseLiveQueryClient is null.");
            Toast.makeText(this, "ParseLiveQueryClient is null.", Toast.LENGTH_SHORT).show();
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            messageNum++;
            ParseObject message = new ParseObject(MESSAGE_OBJ_NAME);
            message.put(MESSAGE_CONTENT_KEY, "Message " + messageNum);
            message.put(MESSAGE_DESTINATION_KEY, "Destination of message " + messageNum);
            message.saveInBackground(e -> {
                if (null == e) {
                    Snackbar.make(view, "Message " + messageNum + " has been sent!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(view, e.toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        });

    }

    /**
     * Add the least Message Object to Message list
     *
     * @param object The least Message Object
     */
    private void appendMessage(ParseObject object) {
        Log.d(TAG, "Add object " + object.getString(MESSAGE_CONTENT_KEY));
        if (isMessageEmpty) {
            messageListContainer.removeAllViews();
            isMessageEmpty = false;
        }

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View messageCardView = layoutInflater.inflate(R.layout.message_card, messageListContainer, false);

        TextView messageContentView = messageCardView.findViewById(R.id.message_content);
        TextView messageDestinationView = messageCardView.findViewById(R.id.message_destination);

        messageContentView.setText(object.getString(MESSAGE_CONTENT_KEY));
        messageDestinationView.setText(object.getString(MESSAGE_DESTINATION_KEY));

        // Add new message car in the top of list
        messageListContainer.addView(messageCardView, 0);

    }

}