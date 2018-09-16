package com.example.liamkelly.drawingbuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity {

    private Button mLogin;
    private EditText mUserID;
    private ListView mImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        mLogin = (Button)findViewById(R.id.login);
        mUserID = (EditText)findViewById(R.id.userid);
        mImages = (ListView)findViewById(R.id.images);

        mImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String img = (String)parent.getItemAtPosition(position);
                goToActivity(img);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> images = DatabaseManager.getInstance(SelectActivity.this).getImageNames(mUserID.getText().toString());
                ArrayAdapter adapter = new ArrayAdapter<String>(SelectActivity.this,
                        android.R.layout.simple_list_item_1,
                        (ArrayList<String>)images);
                mImages.setAdapter(adapter);
            }
        });

    }

    public void goToActivity(final String img) {
        final String[] difficulties = {"1", "2", "3", "4", "5"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a Difficulty");
        builder.setItems(difficulties, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                Intent i = new Intent(SelectActivity.this, CanvasActivity.class);
                i.putExtra("name", img);
                i.putExtra("user", mUserID.getText().toString());
                i.putExtra("difficulty", difficulties[which]);
                ImageStateManager.getInstance(SelectActivity.this).setImagePoints(DatabaseManager.getInstance(SelectActivity.this).getPoints(mUserID.getText().toString(), img));
                startActivity(i);
            }
        });
        builder.show();
    }

}
