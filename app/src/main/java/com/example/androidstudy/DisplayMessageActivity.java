package com.example.androidstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    private TextView messagePrint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        setAllViews();
        showMessage();
    }

    private void setAllViews() {
        messagePrint = findViewById(R.id.message_print);
    }

    private void showMessage() {
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        messagePrint.setText(getResources().getString(R.string.your_password)+message);
    }
}