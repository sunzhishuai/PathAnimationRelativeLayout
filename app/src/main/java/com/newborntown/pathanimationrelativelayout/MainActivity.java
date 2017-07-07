package com.newborntown.pathanimationrelativelayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private PathAnimationRelativeLayout pathAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View viewById = findViewById(R.id.btn);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathAnimation = (PathAnimationRelativeLayout) findViewById(R.id.pr_show);
                int measuredWidth = pathAnimation.getMeasuredWidth();
                int waveCounts = 5 ;
                pathAnimation.setTime(3000);
                pathAnimation.setPath(MovePath.getWavePath(300,0,measuredWidth,100,waveCounts));
                pathAnimation.setRotationLightPath(true);
                pathAnimation.start();
            }
        });

    }
}
