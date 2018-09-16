package com.example.liamkelly.drawingbuddy;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserInfo {

    private final String IMAGES = "Images";
    private Context mContext;

    private Map<String, List<int[]>> mImagePointMap;
    private List<String> mImages;


    public UserInfo(DataSnapshot userSnap, Context c) { // 123
        DataSnapshot imgs = userSnap.child(IMAGES);
        mImagePointMap = new HashMap<>();
        mImages = new LinkedList<>();
        for (DataSnapshot img : imgs.getChildren()) { // test
            mImages.add(img.getKey());
            List<int[]> nextContour = new ArrayList<>();
            for (DataSnapshot contour : img.getChildren()) { //
                for (DataSnapshot point : contour.getChildren()) {
                    int[] pt = {
                            point.child("0").child("0").getValue(Integer.class),
                            point.child("0").child("1").getValue(Integer.class)
                    };
                    nextContour.add(pt);
                }
            }
            mImagePointMap.put(img.getKey(), nextContour);
        }
    }

    public List<String> getImages() {
        return mImages;
    }

    public List<int[]> getImagePoints(String img) {
        return mImagePointMap.get(img);
    }
}
