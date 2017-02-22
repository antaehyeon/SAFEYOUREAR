package com.example.hyeon.safeyourear;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.List;

import im.dacer.androidcharts.LineView;
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

//    // Card View
//    Card card;
//
//    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;
//    private ArrayList<MyData> myDataset;

    TextView txtStep, txtFreq, txtEar;

    Button btnCantHear;
    Button btnFreqUp;
    Button btnFreqDown;

    // Tone Generator
    private int duration = 60; // seconds
    private int sampleRate = 44100;
    private int numSamples = duration * sampleRate;
    private double sample[] = new double[numSamples];
    private double freqOfTone = 750; // hz

    public byte generatedSnd[] = new byte[2 * numSamples];

    Handler handler = new Handler();

    private AudioManager audioManager;
    public AudioTrack audioTrack = null;

    boolean btnCantHearClick = false;

    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    public static final int SHORT = 1;
    public static final int LONG = 2;

    List<Line> lines;
    List<PointValue> leftValue, rightValue;

    Line leftLine, rightLine;

    float leftTone = 0.5f;
    float rightTone = 0.5f;

    // Frequency Index (RightValue, LeftValue)
    public static final int FREQ250 = 0;
    public static final int FREQ500 = 1;
    public static final int FREQ1000 = 2;
    public static final int FREQ2000 = 3;
    public static final int FREQ4000 = 4;
    public static final int FREQ6000 = 5;
    public static final int FREQ8000 = 6;

    int freq250LeftDecibel = 30;

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

//        // Card View
//        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//
//        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        // specify an adapter (see also next example)
//        myDataset = new ArrayList<>();
//        mAdapter = new MyAdapter(myDataset);
//        mRecyclerView.setAdapter(mAdapter);

//        myDataset.add(new MyData("#InsideOut", R.mipmap.insideout));
//        myDataset.add(new MyData("#Mini", R.mipmap.mini));
//        myDataset.add(new MyData("ToyStory", R.mipmap.toystory));

//        myDataset.add(new MyData("현재 진행하고 있는 단계는 1 단계 입니다.", "현재 테스트중인 데시벨은 1000Hz 입니다."));


        // 아래쪽에서 STEP 변수들
        txtStep = (TextView) findViewById(R.id.tv_step);
        txtFreq = (TextView) findViewById(R.id.tv_freq);

        txtEar = (TextView) findViewById(R.id.tv_ear);
        // BLUE : #2196F3 (BLUE)
        // RED : #F44336 (RED)
        txtEar.setTextColor(Color.parseColor("#F44336"));


        btnCantHear = (Button) findViewById(R.id.btn_cantHear);
        btnCantHear.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btnCantHearClick) {
                    btnCantHear.setText("들리지 않으면\n클릭하세요");
                    btnCantHearClick = true;
                    toastMessage("테스트가 시작됩니다.", SHORT);
                    toastMessage("버튼을 이용해 들리지 않을때까지 조정하세요", LONG);
                    audioMetryDataChange(1, 1000, LEFT);

                    genTone();
                    playSound();
                    playTone(1.0f, 0.0f);

                } else {
                    // 역치 정하고 데이터 저장하는 부분으로 진행
                    setSineWaveData(60, 44100, (int)(Math.random() * 8000) + 1);
                    genTone();
                    playSound();
                    playTone(0.5f, 0.5f);
                }
            }
        });
        btnFreqDown = (Button) findViewById(R.id.btn_freqDown);
        btnFreqDown.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastMessage("DOWN 버튼을 클릭하였습니다", SHORT);
                leftTone -= 0.1f;
                rightTone -= 0.1f;
                playTone(leftTone, rightTone);
            }
        });
        btnFreqUp = (Button) findViewById(R.id.btn_freqUp);
        btnFreqUp.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftTone += 0.1f;
                rightTone += 0.1f;
                playTone(leftTone, rightTone);

                //public void setGraphData(int index, int graphFreq, int graphDecibel, int graphEar)
                freq250LeftDecibel -= 5;
                setGraphData(FREQ250, 250, freq250LeftDecibel, LEFT);

            }
        });

        btnCantHear.setText("시작하려면 버튼을 \n클릭하세요");

        // 하드웨어 오디오 볼륨 조절
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, 0);

    }

    /*
    seque   0,   1,    2,    3,    4,    5,    6
    index 250, 500, 1000, 2000, 4000, 6000, 8000
    */
    public void setGraphData(int index, int graphFreq, int graphDecibel, int graphEar) {
        PointValue tempPV = new PointValue(graphFreq, graphDecibel);

        // LEFT == 1
        if (graphEar == LEFT) {
            leftValue.set(index, tempPV);
        } else {
            rightValue.set(index, tempPV);
        }
    }

    public void setSineWaveData(int duration, int sampleRate, int freqOfTone) {
        this.duration = duration;
        this.sampleRate = sampleRate;
        this.freqOfTone = freqOfTone;

        numSamples = duration * sampleRate;
        sample = new double[numSamples];

        generatedSnd = new byte[2 * numSamples];

    }

    public void toastMessage (CharSequence text, int mode) {
        int toastMode;
        Context context = getApplicationContext();
        if (mode == 1) {
            toastMode = Toast.LENGTH_SHORT;
        } else {
            toastMode = Toast.LENGTH_LONG;
        }

        Toast toast = Toast.makeText(context, text, toastMode);
        toast.show();
    }

    public void audioMetryDataChange(int step, int freq, int ear) {
        if (step < 10) {
            txtStep.setText("0"+String.valueOf(step));
        } else {
            txtStep.setText(String.valueOf(step));
        }
        txtFreq.setText(String.valueOf(freq));
        if (ear == 1) {
            txtEar.setText("LEFT");
        } else {
            txtEar.setText("RIGHT");
        }
    }

    // 그래프 초기화 메소드
    private void initView(LineView lineView) {
        ArrayList<String> test = new ArrayList<String>();
        for (int i=0; i<9; i++) {
            test.add(String.valueOf(i+1));
        }
        lineView.setBottomTextList(test);
        lineView.setColorArray(new int[]{Color.parseColor("#F44336"),Color.parseColor("#9C27B0"),Color.parseColor("#2196F3"),Color.parseColor("#009688")});
        lineView.setDrawDotLine(true);
        lineView.setShowPopup(LineView.SHOW_POPUPS_NONE);

    }

    // 그래프 V2
    private void generateData() {

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
        rightValue.add(new PointValue(250, 40));
        rightValue.add(new PointValue(500, 40));
        rightValue.add(new PointValue(1000, 40));
        rightValue.add(new PointValue(2000, 40));
        rightValue.add(new PointValue(4000, 40));
        rightValue.add(new PointValue(6000, 40));
        rightValue.add(new PointValue(8000, 40));

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

        PointValue pv = new PointValue(250, 10);
        rightValue.set(0, pv);

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

    void genTone(){
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

    public void playSound() {

        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                numSamples, AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.getChannelConfiguration();
        try {
            audioTrack.play();
        } catch (Exception e) { // error message if not playable
            Toast.makeText(getApplicationContext(), "Error playing audio",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void playTone(float left, float right) { // overloaded playTone
        // method with volume
        // determined by user
        Float leftVolume = left;
        Float rightVolume = right;
        try {
            audioTrack.write(generatedSnd, 0, generatedSnd.length);// pass
            // genSound
            // into
            // write
            // method
            audioTrack.setStereoVolume(leftVolume, rightVolume); // left and
            // right
            // are set
            // by
            // user
            // choice
            audioTrack.play();
        } catch (Exception e) { // error message if not playable
            Toast.makeText(getApplicationContext(), "Error playing audio",
                    Toast.LENGTH_SHORT).show();
        }
    }


}
