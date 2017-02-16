package com.example.toplibrary;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MyVolley.getInstance(this).preLoadImageIntoDiskOnly("http://a.server.for.image.com/foo.png", VolleyUsage.usage());
    }

    private boolean isWindowFocusChanged = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        isWindowFocusChanged = true;
    }

    @Override
    public void onBackPressed() {
        if (isWindowFocusChanged) { // just want to make the isWindowFocusChanged field use!!
            super.onBackPressed();
        }
    }

    public void unusedMethod() {
        System.out.println("An unuse method, should be remove while proguard's shrinking step!!");
    }
}
