package com.example.liamkelly.drawingbuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class CanvasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        int steps = (10 * (Integer.parseInt(bundle.getString("difficulty")) - 1)) + 1;
        setContentView(new CanvasView(CanvasActivity.this, steps, (String)bundle.get("name"), (String)bundle.get("user")));
    }
}
