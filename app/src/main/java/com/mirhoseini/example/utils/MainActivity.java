package com.mirhoseini.example.utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.mirhoseini.utils.Utils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void actionExitKillProcess(View view) {
        Utils.exit();
    }

    public void actionExitNormal(View view) {
        Utils.exit(this);
    }
}
