package com.example.hyeon.safeyourear;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class AudioMetryResultActivity extends AppCompatActivity {

    // COLORS : 색깔 변수들
    int whiteColor = Color.parseColor("#FFFFFF");
    int baseColor = Color.parseColor("#213b4c");


    // VARIABLE : 변수들
    RelativeLayout relativeLayout;
    TextView title;


    // VARIABLE - GRAPH
    LineChartView resultChart;

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape diamond = ValueShape.DIAMOND;
    private ValueShape circle = ValueShape.CIRCLE;
    private ValueShape square = ValueShape.SQUARE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor = false;
    private boolean hasGradientToTransparent = false;

    Line averageLine;
    LineChartData data;

    List<Line> lines;
    List<PointValue> averageValues;

    float[] freqData = {250, 500, 1000, 2000, 4000, 6000, 8000};

    // VARIABLE - RECYCLER VIEW
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    RecyclerView mRecylerView;

    float[] freqDecibelRight;
    float[] freqDecibelLeft;

    // VARIABLE - SINGLETON
    Singleton mSingleton;


    // FONT CHANGE : 폰트 변경
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    // ON CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_metry_result);

        // SINGLETON GET INSTANCE
        mSingleton = Singleton.getInstance();

        // ActionBar 대신 ToolBar 생성
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_audioMetryResult);
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

        // AudioMetryActivity 에서 Data 받아오기 (사용자가 조정한 Decibel을 Singleton 을 이용해서 받아온다)
        freqDecibelLeft = mSingleton.getFreqLeftData();
        freqDecibelRight = mSingleton.getFreqRightData();

        Log.i("HYEON", "AudioMetryResultActivity ADDRESS : " + freqDecibelLeft);
        for (int i = 0; i < 7; i++) {
            Log.i("HYEON", "freqDecibelLeft [" + i + "] : " + freqDecibelLeft[i]);
        }

        // Graph Draw
        // 그래프 그리기 - HelloChart
        resultChart = (LineChartView) relativeLayout.findViewById(R.id.resultChart);
        generateData();
        resetViewport();
        resultChart.setViewportCalculationEnabled(false);

        // Recycler View를 위한 RecyclerView 선언 후에 LinearLayoutManager 을 set
//        mRecylerView.setLayoutManager(mLayoutManager);

        /*
            청력측정한 데이터를 액티비티 구성할 때 받아옴
            0, 1, 2, 3, 4, 5, 6, 7
            250, 500, 1000, 2000, 4000, 6000, 8000
        */





    } // onCreate VOID


    // Graph Draw - 데이터 생성
    private void generateData() {

        lines = new ArrayList<Line>();

        // 그래프에 데이터 넣는 과정
        averageValues = new ArrayList<PointValue>();
        for (int i = 0; i < 7; i++) {
            Log.i("HYEON", "generateData FOR " + i);
            Log.i("HYEON", "freqData[" + i + "] : " + freqData[i]);
            Log.i("HYEON", "freqDecibelLeft[" + i + "] : " + freqDecibelLeft[i]);
            averageValues.add(new PointValue(freqData[i], freqDecibelLeft[i]));
        } // FOR INPUT GRAPH DATA 0-6

        // 라인 속성들 지정
        averageLine = new Line(averageValues);
        averageLine.setColor(ChartUtils.COLORS[2]);
        averageLine.setShape(diamond);
        averageLine.setCubic(isCubic);
        averageLine.setFilled(isFilled);
        averageLine.setHasLabels(hasLabels);
        averageLine.setHasLabelsOnlyForSelected(hasLabelForSelected);
        averageLine.setHasLines(hasLines);
        averageLine.setHasPoints(hasPoints);
        averageLine.setPointRadius(1);

        // ArrayList에 Line 하나 추가
        lines.add(averageLine);

        // LineChartData에 최종으로 ArrayList Lines가 들어감
        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis().setHasLines(true);
            Axis axisY = new Axis().setHasLines(true);

            axisX = Axis.generateAxisFromRange(0.0f, 8000.0f, 1000.0f);

            axisX.setName("Frequency(Hz)");
            axisY.setName("DBHL(Decibels)");

            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        resultChart.setLineChartData(data);
    } // GENERATE DATA FUNCTION

    // Graph Draw - 그래프 범위 조정
    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(resultChart.getMaximumViewport());
        v.bottom = 0;
        v.top = 100;
        v.left = 0;
        v.right = 8000;
        resultChart.setMaximumViewport(v);
        resultChart.setCurrentViewport(v);
    } // RESET VIEW PORT FUNCTION



} // AudioMetryResultActivity CLASS

//public class ViewHolder extends RecyclerView.ViewHolder {
//    public TextView mTextView;
//    public ViewHolder(TextView v) {
//        super(v);
//        mTextView = v;
//    }
//}
