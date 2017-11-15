package com.babase.lib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.babase.lib.utils.GlideCircleTransform;
import com.babase.lib.utils.GlideUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.text_iv);

        GlideUtil.setPlaceholderDrawableId(R.mipmap.ic_launcher, R.mipmap.ic_launcher);

        GlideCircleTransform glideCircleTransform = new GlideCircleTransform(this);
        glideCircleTransform.setText("HH");
        GlideUtil.loadCircle(this, "http://img05.tooopen.com/images/20140328/sy_57865838889.jpg", true, glideCircleTransform, imageView);
    }
}
