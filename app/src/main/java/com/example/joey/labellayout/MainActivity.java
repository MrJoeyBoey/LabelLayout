package com.example.joey.labellayout;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LabelLayout layout=findViewById(R.id.label_layout);

        layout.addLabel("首页", "消息", "联系人", "更多")
//              .setTagColor(Color.RED)
//              .setLabelColor(Color.BLUE)
//              .setLabelHeight(50)
//              .setLabelColor(Color.parseColor("#EC7263"))
//              .setTagColor(Color.GRAY)
//              .setTagSize(10)
              .create();
    }
}
