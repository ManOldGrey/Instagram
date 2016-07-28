package com.example.akrysa.instagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    GetTokenInstagram getTokenInstagram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final CurrentSession currentSession = new CurrentSession(this);

        getTokenInstagram = new GetTokenInstagram(this);

        if(!currentSession.isActiveSession()) {
            setContentView(R.layout.activity_main);

            ((Button) findViewById(R.id.btn_connect)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    getTokenInstagram.authorize();
                }
            });
        }else {
            setContentView(R.layout.activity_user);

            CurrentUser instagramUser = currentSession.getUser();

            ((TextView) findViewById(R.id.string_name)).setText(instagramUser.fullName);
            ((TextView) findViewById(R.id.string_username)).setText(instagramUser.username);
            ((TextView) findViewById(R.id.string_token)).setText(instagramUser.accessToken);

            ((Button) findViewById(R.id.btn_logout)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    currentSession.clearSession();

                    startActivity(new Intent(MainActivity.this, MainActivity.class));

                    finish();
                }
            });

        }
    }

    public void refreshActivity(){
        finish();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }
}
