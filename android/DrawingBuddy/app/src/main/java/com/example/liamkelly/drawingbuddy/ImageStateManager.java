package com.example.liamkelly.drawingbuddy;

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

    private static final ImageStateManager ourInstance = new ImageStateManager();

    public static ImageStateManager getInstance() {
        return ourInstance;
    }

    private ImageStateManager() {

    }

    // Index y then x
    public void setImagePoints(List<int[]> pts) {
        mImagePoints = pts;
        for (int[] pt : mImagePoints) {
            xMax = Math.max(xMax, pt[0]);
            yMax = Math.max(yMax, pt[1]);
        }
        mGrid = new int[yMax][];
        for (int y = 0; y < yMax; y++) {
            mGrid[y] = new int[xMax];
        }
        for (int[] pt : mImagePoints) { // Create grid of the pixels.
            mGrid[pt[1]][pt[0]] = 1;
        }
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
            if (mGrid[j][i] == 1) {
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
        if (x >= 0 && x < mGrid[0].length && y >= 0 && y < mGrid.length) {
            if (visited[y][x] != 0) {
                search.add(new int[]{x, y});
                visited[y][x] = 1;
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
