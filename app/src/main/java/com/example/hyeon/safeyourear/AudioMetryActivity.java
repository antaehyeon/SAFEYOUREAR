package com.example.hyeon.safeyourear;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.List;

import im.dacer.androidcharts.LineView;
import it.gmariotti.cardslib.library.internal.Card;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class AudioMetryActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;

    TextView title;

    int whiteColor = Color.parseColor("#FFFFFF");
    int baseColor = Color.parseColor("#213b4c");

    LineChartView chart;
    private LineChartData data;


    // 그래프 V2 변수들
    int numberOfLines = 2;
    int maxNumberOfLines = 4;
    int numberOfPoints = 8;

    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape diamond = ValueShape.DIAMOND;
    private ValueShape circle = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor = false;
    private boolean hasGradientToTransparent = false;

    // Card View
    Card card;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_metry);

        // ActionBar 대신 ToolBar 생성
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar_audiometry);
        toolbar.setTitleTextColor(whiteColor);
        toolbar.setBackgroundColor(baseColor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        // Toolbar 에 넣을 txetView(title) 속성 조정
        title = (TextView) toolbar.findViewById(R.id.toolBar_audiometry_title);
        title.setTextColor(whiteColor);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setTextSize(20);

        // audiometry 최상위 뷰 속성 조정
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_audio_metry);
        relativeLayout.setBackgroundColor(baseColor);
        relativeLayout.setPadding(0, 0, 0, 0);


        // Notification Bar 색상 지정
        //https://medium.com/@mindwing/actionbar-%EB%A5%BC-%EB%8B%A4%EB%A4%84%EB%B4%85%EC%8B%9C%EB%8B%A4-401709e5480d#.jsyw89cbu
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));
        //http://ghj1001020.tistory.com/m/21
        window.setStatusBarColor(baseColor);


//        // 그래프 그리기
//        LineView lineView = (LineView) relativeLayout.findViewById(R.id.line_view);
//        initView(lineView);
////        lineView.setDrawDotLine(false); //optional
////        lineView.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY); //optional
////        LineView.setBottomTextList(strList);
////        lineView.setColorArray(new int[]{Color.BLACK,Color.GREEN,Color.GRAY,Color.CYAN});
//
//        ArrayList<Integer> dataList = new ArrayList<>();
//        float random = (float)(Math.random()*9+1);
//        for (int i=0; i<9; i++){
//            dataList.add((int)(Math.random()*random));
//        }
//
//        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();
//        dataLists.add(dataList);
//
//        lineView.setDataList(dataLists); //or lineView.setFloatDataList(floatDataLists)

        // 그래프 그리기 - HelloChart
        chart = (LineChartView) relativeLayout.findViewById(R.id.chart);

        // 그래프에 랜덤 데이터 넣는 부분이였지만, 초기데이터 40으로 지정함
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
//                randomNumbersTab[i][j] = (float) Math.random() * 100f;
                randomNumbersTab[i][j] = 40;
            }
        }

        generateData();

        // Disable viewport recalculations, see toggleCubic() method for more info.
        chart.setViewportCalculationEnabled(false);

        resetViewport();

//        previewX();

        // Card View
        Card card = new Card();

    }

    // 그래프 초기화 메소드
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

    // 그래프 V2
    private void generateData() {

        List<Line> lines = new ArrayList<Line>();
        Log.i("Generate Data = ", "" + lines.size());
        for (int i = 0; i < numberOfLines; ++i) {

            // 그래프에 데이터 넣는 과정
            List<PointValue> values = new ArrayList<PointValue>();
//            for (int j = 0; j < numberOfPoints; ++j) {
//                values.add(new PointValue(j, randomNumbersTab[i][j]));
//                Log.i("HYEON", "" + randomNumbersTab[i][j]);
//            }

            if (i == 1) {
                values.add(new PointValue(250, 30));
                values.add(new PointValue(500, 30));
                values.add(new PointValue(1000, 30));
                values.add(new PointValue(2000, 30));
                values.add(new PointValue(4000, 30));
                values.add(new PointValue(6000, 30));
                values.add(new PointValue(8000, 30));
            } else {
                // PointValue (X, Y좌표)
                values.add(new PointValue(250, 40));
                values.add(new PointValue(500, 40));
                values.add(new PointValue(1000, 40));
                values.add(new PointValue(2000, 40));
                values.add(new PointValue(4000, 40));
                values.add(new PointValue(6000, 40));
                values.add(new PointValue(8000, 40));
            }


            Line line = new Line(values);
            if (i == 1) { line.setColor(ChartUtils.COLORS[4]); } else { line.setColor(ChartUtils.COLOR_BLUE); }

            if (i == 1) { line.setShape(circle); } else { line.setShape(diamond); }
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            //line.setHasGradientToTransparent(hasGradientToTransparent);
            if (pointsHaveDifferentColor){
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
            Log.i("Generate Data = ", "" + lines.size());

        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis().setHasLines(true);

            List<Float> xData = new ArrayList<Float>();
            xData.add(0f);
            xData.add(250f);
            xData.add(500f);
            xData.add(1000f);
            xData.add(2000f);
            xData.add(4000f);

            xData.add(6000f);
            xData.add(8000f);

            axisX = Axis.generateAxisFromRange(0.0f, 8000.0f, 1000.0f);
//            axisX = Axis.generateAxisFromCollection(xData);
            axisX.setHasLines(true);

//            axisX = Axis.generateAxisFromCollection(xData);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Frequency(Hz)");
                axisY.setName("DBHL(Decibels)");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);
    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = 100;
        v.left = 0;
        v.right = numberOfPoints - 1;
        v.right = 8000;
        Log.i("resetViewport", "right = " + v.right);
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

//    private void previewX() {
//        Viewport tempViewport = new Viewport(chart.getMaximumViewport());
//        float dx = tempViewport.width() / 4;
////        dx = 250;
//        Log.i("HYEON", "dx : " + dx + " TempViewPort : " + tempViewport + " height : " + tempViewport.height());
//        tempViewport.left = 0;
//        tempViewport.right = 10;
//        tempViewport.inset(dx, 0);
//
//        chart.setCurrentViewport(tempViewport);
//
//    }
}
