package com.example.liamkelly.drawingbuddy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.MediaType;
import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.giphy.sdk.core.network.response.ListMediaResponse;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView mGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GPHApi client = new GPHApiClient("ft4w3IYkimNWEOl4AY33clM0ya0WpWko");

        mGif = (ImageView) findViewById(R.id.loading);


        client.search("loading", MediaType.gif, null, null, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                Log.d("we got somethin", "lol");
                if (result == null) {
                    // Do what you want to do with the error
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    if (result.getData() != null) {
                        if (result.getData().size() > 0) {
                            Handler h = new Handler();
                            Runnable r = new Runnable() {
                                @Override
                                public void run() {
                                    DatabaseManager.getInstance(MainActivity.this);
                                }
                            };
                            h.postDelayed(r, 3000);
                            Random rand = new Random();
                            Media gif = result.getData().get(rand.nextInt(result.getData().size()));
                            Glide.with(MainActivity.this).load(gif.getImages().getOriginal().getGifUrl())
                                    .asGif()
                                    .crossFade()
                                    .listener(new RequestListener<String, GifDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String s, Target<GifDrawable> target, boolean b) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GifDrawable gifDrawable, String s, Target<GifDrawable> target, boolean b, boolean b1) {
                                            Handler h = new Handler();
                                            Runnable r = new Runnable() {
                                                @Override
                                                public void run() {
                                                    DatabaseManager.getInstance(MainActivity.this);
                                                }
                                            };
                                            h.postDelayed(r, 3000);
                                            return false;
                                        }
                                    })
                                    .into(mGif);
                            Log.d("got", "" + gif.getImages().getOriginal().getGifUrl());
                        } else {
                            Log.d("2 small", "lol");
                            DatabaseManager.getInstance(MainActivity.this);
                        }
                    } else {
                        Log.e("giphy error", "No results found");
                        DatabaseManager.getInstance(MainActivity.this);
                    }
                }
            }
        });
    }
}
