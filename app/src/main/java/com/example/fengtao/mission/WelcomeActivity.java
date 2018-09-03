package com.example.fengtao.mission;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.util.Timer;
import java.util.TimerTask;



public class WelcomeActivity extends AppCompatActivity {

    public void StatusbarTransparent(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*下面是在继承AppCompatActivity下，隐藏标题栏，上面注释掉的语句是在继承Activity下隐藏标题栏*/
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        StatusbarTransparent();

        setContentView(R.layout.activity_welcome);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        };
        timer.schedule(timerTask,1000*3);
    }
}
