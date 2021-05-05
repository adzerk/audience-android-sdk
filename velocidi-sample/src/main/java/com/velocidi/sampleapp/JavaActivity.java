package com.velocidi.sampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.velocidi.Channel;
import com.velocidi.Config;
import com.velocidi.Velocidi;
import com.velocidi.UserId;

import org.json.JSONObject;

import java.util.Arrays;

public class JavaActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Channel trackEndpoint = new Channel(Uri.parse("http://tr.cdp.velocidi.com/events"), true);
        Channel matchEndpoint = new Channel(Uri.parse("http://match.cdp.velocidi.com/match"), true);
        Config config = new Config(trackEndpoint, matchEndpoint);

        Velocidi.init(config, this);

        try {
            JSONObject eventJsonObj = new JSONObject()
                .put("clientId", "velocidi")
                .put("siteId", "velocidi.com")
                .put("type", "appView");

            Velocidi.getInstance().track(
                    new UserId("user_email_hash", "email_sha256"),
                    eventJsonObj
            );

        } catch (Exception e) {
            Log.d(TAG, "Error sending tracking event");
        }


        Velocidi.getInstance().match(
                "someProvider",
                Arrays.asList(
                        new UserId("user_email_hash", "email_sha256"),
                        new UserId("user_advertising_id", "gaid")
                )
        );
    }
}
