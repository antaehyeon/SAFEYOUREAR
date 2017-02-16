package com.example.hyeon.safeyourear;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

import im.dacer.androidcharts.LineView;

public class AudioMetryActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;

    TextView title;

    int whiteColor = Color.parseColor("#FFFFFF");
    int baseColor = Color.parseColor("#213b4c");

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_metry);

//        getSupportActionBar().setTitle("청력측정");
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF07263b));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar_audiometry);
        toolbar.setTitleTextColor(whiteColor);
        toolbar.setBackgroundColor(baseColor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        title = (TextView) toolbar.findViewById(R.id.toolBar_audiometry_title);
        title.setTextColor(whiteColor);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setTextSize(20);

        relativeLayout = (RelativeLayout) findViewById(R.id.activity_audio_metry);
        relativeLayout.setBackgroundColor(baseColor);
        relativeLayout.setPadding(0, 0, 0, 0);


        //https://medium.com/@mindwing/actionbar-%EB%A5%BC-%EB%8B%A4%EB%A4%84%EB%B4%85%EC%8B%9C%EB%8B%A4-401709e5480d#.jsyw89cbu
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));
        //http://ghj1001020.tistory.com/m/21
        window.setStatusBarColor(baseColor);



        LineView lineView = (LineView) relativeLayout.findViewById(R.id.line_view);
        initView(lineView);
//        lineView.setDrawDotLine(false); //optional
//        lineView.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY); //optional
//        LineView.setBottomTextList(strList);
//        lineView.setColorArray(new int[]{Color.BLACK,Color.GREEN,Color.GRAY,Color.CYAN});

        ArrayList<Integer> dataList = new ArrayList<>();
        float random = (float)(Math.random()*9+1);
        for (int i=0; i<9; i++){
            dataList.add((int)(Math.random()*random));
        }

        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();
        dataLists.add(dataList);

        lineView.setDataList(dataLists); //or lineView.setFloatDataList(floatDataLists)


    }

    private void initView(LineView lineView) {
        ArrayList<String> test = new ArrayList<String>();
        for (int i=0; i<9; i++){
            test.add(String.valueOf(i+1));
        }
        lineView.setBottomTextList(test);
        lineView.setColorArray(new int[]{Color.parseColor("#F44336"),Color.parseColor("#9C27B0"),Color.parseColor("#2196F3"),Color.parseColor("#009688")});
        lineView.setDrawDotLine(true);
        lineView.setShowPopup(LineView.SHOW_POPUPS_NONE);

    }
}
