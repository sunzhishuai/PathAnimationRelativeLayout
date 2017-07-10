package com.newborntown.pathanimationrelativelayout;

import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Graceful Sun on 17/7/8.
 * E-mail itzhishuaisun@sina.com
 */

public class PathActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        final PathAnimationRelativeLayout pathView = (PathAnimationRelativeLayout) findViewById(R.id.path);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathView.setRotationLightPath(true);
                pathView.setTime(5000);
                pathView.setPath(MovePath.getWavePath(500,0,pathView.getMeasuredWidth(),100,5));
                pathView.start();
            }
        });
    }
}
