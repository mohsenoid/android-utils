package com.mohsenoid.androidutils.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.mohsenoid.androidutils.Utils;

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

    public void actionOpenUrl(View view) {
        String url = ((EditText) findViewById(R.id.etUrl)).getText().toString();
        Utils.openWebsite(this, url);
    }
}
