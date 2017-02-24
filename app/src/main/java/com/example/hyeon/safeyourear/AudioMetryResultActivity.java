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

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

import static android.R.attr.data;
import static android.R.attr.lines;
import static com.example.hyeon.safeyourear.R.id.chart;

public class AudioMetryResultActivity extends AppCompatActivity {

    // COLORS : 색깔 변수들
    int whiteColor = Color.parseColor("#FFFFFF");
    int baseColor = Color.parseColor("#213b4c");


    // VARIABLE : 변수들
    RelativeLayout relativeLayout;
    TextView title;


    // VARIABLE - GRAPH
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

    List<Line> averageLine;
    List<PointValue> averageValues;


    // FONT CHANGE : 폰트 변경
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_metry_result);

        // ActionBar 대신 ToolBar 생성
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar_audiometry);
        toolbar.setTitleTextColor(whiteColor);
        toolbar.setBackgroundColor(baseColor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        // Toolbar 에 넣을 txetView(title) 속성 조정
        title = (TextView) toolbar.findViewById(R.id.toolBar_audiometry_title);
        title.setTextColor(whiteColor);
        title.setText("청력측정 결과");
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setTextSize(20);

        // audiometry 최상위 뷰 속성 조정
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_audio_metry_result);
        relativeLayout.setBackgroundColor(baseColor);
        relativeLayout.setPadding(0, 0, 0, 0);

        /* Notification Bar 색상 지정
           https://medium.com/@mindwing/actionbar-%EB%A5%BC-%EB%8B%A4%EB%A4%84%EB%B4%85%EC%8B%9C%EB%8B%A4-401709e5480d#.jsyw89cbu
           http://ghj1001020.tistory.com/m/21 */
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(baseColor);

//        // Graph Draw
//        // 그래프 그리기 - HelloChart
//        chart = (LineChartView) relativeLayout.findViewById(chart);
//
//        // 그래프에 랜덤 데이터 넣는 부분이였지만, 초기데이터 40으로 지정함
//        for (int i = 0; i < maxNumberOfLines; ++i) {
//            for (int j = 0; j < numberOfPoints; ++j) {
////                randomNumbersTab[i][j] = (float) Math.random() * 100f;
//                randomNumbersTab[i][j] = 40;
//            }
//        }
//
//        generateData();
//
//        // Disable viewport recalculations, see toggleCubic() method for more info.
//        chart.setViewportCalculationEnabled(false);
//
//        resetViewport();




    } // onCreate VOID


    // Graph Draw - 데이터 생성
    rivate void generateData() {

        lines = new ArrayList<Line>();
        Log.i("Generate Data = ", "" + lines.size());

        // 그래프에 데이터 넣는 과정
        leftValue = new ArrayList<PointValue>();
        rightValue = new ArrayList<PointValue>();
//            for (int j = 0; j < numberOfPoints; ++j) {
//                values.add(new PointValue(j, randomNumbersTab[i][j]));
//                Log.i("HYEON", "" + randomNumbersTab[i][j]);
//            }

        // PointValue (X, Y좌표)
        leftValue.add(new PointValue(250, 30));
        leftValue.add(new PointValue(500, 30));
        leftValue.add(new PointValue(1000, 30));
        leftValue.add(new PointValue(2000, 30));
        leftValue.add(new PointValue(4000, 30));
        leftValue.add(new PointValue(6000, 30));
        leftValue.add(new PointValue(8000, 30));

        // PointValue (X, Y좌표)
        rightValue.add(new PointValue(250, 30));
        rightValue.add(new PointValue(500, 30));
        rightValue.add(new PointValue(1000, 30));
        rightValue.add(new PointValue(2000, 30));
        rightValue.add(new PointValue(4000, 30));
        rightValue.add(new PointValue(6000, 30));
        rightValue.add(new PointValue(8000, 30));

        leftLine = new Line(leftValue);
        rightLine = new Line(rightValue);

        leftLine.setColor(ChartUtils.COLORS[4]); // RED
        rightLine.setColor(ChartUtils.COLORS[0]); // BLUE

        leftLine.setShape(circle);
        rightLine.setShape(diamond);

        leftLine.setCubic(isCubic);
        leftLine.setFilled(isFilled);
        leftLine.setHasLabels(hasLabels);
        leftLine.setHasLabelsOnlyForSelected(hasLabelForSelected);
        leftLine.setHasLines(hasLines);
        leftLine.setHasPoints(hasPoints);

        rightLine.setCubic(isCubic);
        rightLine.setFilled(isFilled);
        rightLine.setHasLabels(hasLabels);
        rightLine.setHasLabelsOnlyForSelected(hasLabelForSelected);
        rightLine.setHasLines(hasLines);
        rightLine.setHasPoints(hasPoints);

//        if (pointsHaveDifferentColor){
//            line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
//        }

        leftLine.setPointRadius(5);
        rightLine.setPointRadius(5);

        lines.add(leftLine);
        lines.add(rightLine);

//        PointValue pv = new PointValue(250, 10);
//        rightValue.set(0, pv);

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
//            axisY = Axis.generateAxisFromRange(0.0f, 100.0f, -10.0f);
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



} // AudioMetryResultActivity CLASS
