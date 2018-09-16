package com.example.liamkelly.drawingbuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class CanvasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int width = getWindow().getDecorView().getWidth();
        int height = getWindow().getDecorView().getHeight();
        Log.d("activity", "height: " + height + " width: " + width);
        setContentView(new CanvasView(CanvasActivity.this, 1, width, height));
    }
}
