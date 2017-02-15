package com.example.hyeon.safeyourear;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SplashActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 해상도 얻어오기
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        // 이미지 뷰 해상도에 맞춰서 적용하기
        ImageView img = (ImageView) findViewById(R.id.imageView);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) img.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;

        // relativeLayout = new RelativeLayout(this); 로 하면 setPadding 에 대한 코드적용이 이뤄지지 않음
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_splash);
        relativeLayout.setPadding(0, 0, 0, 0);

        img.setLayoutParams(params);

        // 액티비티 핸들러로 지연시키기 ( 5 sec )
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0, 5000);
    }

    // 뒤로가기 키 막기
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
