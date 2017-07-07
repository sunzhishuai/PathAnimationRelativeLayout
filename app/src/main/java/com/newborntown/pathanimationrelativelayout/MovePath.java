package com.newborntown.pathanimationrelativelayout;

import android.graphics.Path;
import android.graphics.Point;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Graceful Sun on 17/7/7.
 * E-mail itzhishuaisun@sina.com
 */

public class MovePath {

    public static Path getWavePath(int startY, int startX, int endX, int height, int waveCounts) {
        int width = (endX - startX) / waveCounts;
        Path path = new Path();
        List<Point> points = initPoints(waveCounts, width, height, startY);
        for (int i = 0; i < points.size(); i = i + 2) {
            Point point = points.get(i);
            if (i == 0) {
                path.moveTo(point.x, point.y);
            } else {
                Point prePoint = points.get(i - 1);
                path.quadTo(prePoint.x, prePoint.y, point.x, point.y);
            }
        }
        return path;
    }

    private static List<Point> initPoints(int waveCounts, int waveWidth, int waveHeight, int baselineX) {
        ArrayList<Point> points = new ArrayList<>();

        for (int j = 0; j < waveCounts * 4 + 1; j++) {
            int waveBase = 0;
            switch (j % 4) {
                case 0:
                case 2:
                    waveBase = baselineX;
                    break;
                case 1:
                    waveBase = baselineX - waveHeight;
                    break;
                case 3:
                    waveBase = baselineX + waveHeight;
                    break;
            }
            Point point = new Point(-waveWidth + (waveWidth / 4) * j, waveBase);
            points.add(point);
        }
        return points;
    }


    public static Path getCirclePath(int centerX, int centerY, int r) {
        Path path = new Path();
        path.addCircle(centerX,
                centerY, r
                , Path.Direction.CW);
        return path;
    }


}
