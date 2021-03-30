package com.example.uberhacktag2;

import android.app.Application;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Address  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String value = bundle.getString("value");
            TextView textView = findViewById(R.id.text_view);
            textView.setText(value);
        }
    }
}