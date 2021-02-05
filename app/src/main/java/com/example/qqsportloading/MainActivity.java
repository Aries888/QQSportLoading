package com.example.qqsportloading;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qqsportloading.view.RoundProgressBar;

public class MainActivity extends AppCompatActivity {

    private RoundProgressBar mRoundProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRoundProgressBar = findViewById(R.id.rpb);

        mRoundProgressBar.start(0, 8888);
    }
}