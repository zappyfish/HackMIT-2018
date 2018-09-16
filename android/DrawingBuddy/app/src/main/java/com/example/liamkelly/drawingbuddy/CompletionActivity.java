package com.example.liamkelly.drawingbuddy;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.MediaType;
import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.giphy.sdk.core.network.response.ListMediaResponse;

import java.util.Random;



public class CompletionActivity extends AppCompatActivity {

    private ImageView mGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completion);

        mGif = (ImageView) findViewById(R.id.gif);

        final double score = ImageStateManager.getInstance(CompletionActivity.this).getAverageEnergy();

        GPHApi client = new GPHApiClient("dc6zaTOxFJmzC");

        final String searchWord;
        if (score < 10) {
            searchWord = "awesome";
        } else if (score < 20) {
            searchWord = "decent";
        } else if (score < 30) {
            searchWord = "improvements";
        } else {
            searchWord = "unfortunate";
        }

        ((Button)findViewById(R.id.again)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent i = new Intent(CompletionActivity.this, SelectActivity.class);
                startActivity(i);
            }
        });
        client.search(searchWord, MediaType.gif, null, null, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                if (result.getData() != null) {
                    if (result.getData().size() > 0) {
                        Random rand = new Random();
                        Media gif = result.getData().get(rand.nextInt(result.getData().size()));
                        Glide.with(CompletionActivity.this).load(gif.getImages().getOriginal().getGifUrl())
                                .asGif()
                                .crossFade()
                                .listener(new RequestListener<String, GifDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String s, Target<GifDrawable> target, boolean b) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GifDrawable gifDrawable, String s, Target<GifDrawable> target, boolean b, boolean b1) {
                                        ((TextView)findViewById(R.id.result)).setText(searchWord + ", your score was: " + score);
                                        return false;
                                    }
                                })
                                .into(mGif);
                    } else {
                    }
                        } else {
                            Log.e("giphy error", "No results found");
                        }
                    }
                });
            }

        }
