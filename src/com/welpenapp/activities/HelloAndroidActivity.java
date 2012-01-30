package com.welpenapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelloAndroidActivity extends Activity {
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		final Button button = (Button) findViewById(R.id.presentielijst);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final Intent intent = new Intent(v.getContext(),
						PresentieLijstActivity.class);
				startActivity(intent);
			}
		});
		
		final Button settingsButton = (Button) findViewById(R.id.button4);
		settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(),
                        PresentieLijstActivity.class);
                startActivity(intent);
            }
        });

	}

}