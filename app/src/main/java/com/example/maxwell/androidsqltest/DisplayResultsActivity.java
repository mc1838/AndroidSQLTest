package com.example.maxwell.androidsqltest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        // Get intent that started this activity and get string:
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture this layout's TextView and set message as text:
        TextView txtResult = (TextView) findViewById(R.id.txtResult);
        txtResult.setText(message);
    }
}
