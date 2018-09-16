package com.example.liamkelly.drawingbuddy;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class ImageStateManager {

    private List<int[]> mImagePoints;
    private int xMax = 0;
    private int yMax = 0;

    private int startX = 0;
    private int startY = 0;

    private int[][] mGrid;
    private Context mContext;

    private static ImageStateManager ourInstance;

    public static ImageStateManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new ImageStateManager(context);
        }
        return ourInstance;
    }

    private ImageStateManager(Context context) {
        mContext = context;
    }

    // Index y then x
    public void setImagePoints(List<int[]> pts) {
        mImagePoints = pts;
        xMax = 0;
        yMax = 0;
        for (int[] pt : mImagePoints) {
            xMax = Math.max(xMax, pt[0]);
            yMax = Math.max(yMax, pt[1]);
        }
        mGrid = new int[xMax][];
        Toast.makeText(mContext, "x: " + xMax + " y: " + yMax, Toast.LENGTH_SHORT).show();
        for (int x = 0; x < xMax; x++) {
            mGrid[x] = new int[yMax];
        }
        /*
        for (int[] pt : mImagePoints) { // Create grid of the pixels.
            mGrid[pt[0]][pt[1]] = 1;
        }
        */
        startX = mImagePoints.get(0)[0]; // We assume we have at least one point in our contour
        startY = mImagePoints.get(0)[1];
    }

    private int[] getNearest(int x, int y) {
        // Do a bfs
        Queue<int[]> search = new LinkedList<>();
        int[][] visited = new int[mGrid.length][];
        for (int i = 0; i < mGrid.length; i++) {
            visited[i] = new int[mGrid[i].length];
        }
        addToQueue(x, y, visited, search);
        while (!search.isEmpty()) {
            int[] next = search.poll();
            int i = next[0], j = next[1];
            if (mGrid[i][j] == 1) {
                return next;
            } else {
                addToQueue(x + 1, y, visited, search);
                addToQueue(x - 1, y, visited, search);
                addToQueue(x, y - 1, visited, search);
                addToQueue(x, y + 1, visited, search);
            }
        }
        return null;
    }

    private void addToQueue(int x, int y, int[][] visited, Queue<int[]> search) {
        if (x >= 0 && x < mGrid.length && y >= 0 && y < mGrid[0].length) {
            if (visited[x][y] != 0) {
                search.add(new int[]{x, y});
                visited[x][y] = 1;
            }
        }
    }

    public double getEnergy(int x, int y) {
        int[] nearest = getNearest(x, y);
        return Math.sqrt(Math.pow(x - nearest[0], 2) + Math.pow(y - nearest[1], 2));
    }

    public List<int[]> getPoints(int stepSize) {
        if (stepSize == 1) {
            return mImagePoints;
        } else {
            List<int[]> ret = new ArrayList<>();
            for (int i = 0; i < mImagePoints.size(); i += stepSize) {
                ret.add(mImagePoints.get(i));
            }
            return ret;
        }
    }
}
