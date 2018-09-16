package com.example.liamkelly.drawingbuddy;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DatabaseManager {

    private DatabaseReference mDatabase;
    private Context mContext;
    private final Map<String, UserInfo> mUserIDInfoMap;
    private boolean shouldTransition = true;

    private static DatabaseManager ourInstance;

    public static DatabaseManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new DatabaseManager(context);
        }
        ourInstance.mContext = context;
        return ourInstance;
    }

    public void newDrawing() {
        shouldTransition = true;
    }

    private DatabaseManager(Context context) {
        mUserIDInfoMap = new HashMap<>();
        mContext = context;
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                // ...
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mUserIDInfoMap.put(ds.getKey(), new UserInfo(ds, mContext ));
                }
                if (shouldTransition) {
                    shouldTransition = false;
                    Intent i = new Intent(mContext, SelectActivity.class);
                    mContext.startActivity(i);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
                Toast.makeText(mContext, databaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public List<int[]> getPoints(String userID, String imageName) {
        if (mUserIDInfoMap.containsKey(userID)) {
            return mUserIDInfoMap.get(userID).getImagePoints(imageName);
        } else {
            return null;
        }
    }

    public List<String> getImageNames(String userId) {
        if (mUserIDInfoMap.containsKey(userId)) {
            return mUserIDInfoMap.get(userId).getImages();
        } else {
            return new LinkedList<>();
        }
    }

    public void sendResults() {

    }
}
