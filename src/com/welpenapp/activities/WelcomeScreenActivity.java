package com.welpenapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeScreenActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        final Button presentielijst = (Button) findViewById(R.id.presentielijst);
        presentielijst.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(),
                    PresentieLijstActivity.class);
                startActivity(intent);
            }
        });

        final Button speltakchooser = (Button) findViewById(R.id.speltakchooser);
        speltakchooser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(),
                    SpeltakChooserActivity.class);
                startActivity(intent);
            }
        });
    }
}