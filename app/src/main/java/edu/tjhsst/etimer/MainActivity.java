package edu.tjhsst.etimer;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //Timer
    double init,time;
    Button timer, reset;
    Handler handler;
    boolean isPressed;
    final DecimalFormat df = new DecimalFormat("#0.000");
    String[] scrambles = {"N/A", "N/A", "N/A", "N/A", "N/A", "N/A"};
    TextView scramble;

    //Accelerometer
    private SensorManager sensorMan;
    private Sensor accelerometer;

    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    //SeekBar
    double sensitivity = 1;
    SeekBar slider;
    TextView sense;

    //time list
    Button time1, time2, time3, time4, time5;
    TextView avg5;
    double[] timelist = {-1.0, -1.0, -1.0, -1.0, -1.0, -1.0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Timer
        handler = new Handler();
        timer = (Button) findViewById(R.id.timer);
        reset = (Button) findViewById(R.id.reset);
        scramble = (TextView) findViewById(R.id.scramble);
        String firstScramble = genScramble();
        scramble.setText(firstScramble);
        scrambles[0] = firstScramble;

        //Accelerometer
        sensorMan = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        //SeekBar
        slider = (SeekBar) findViewById(R.id.slider);
        sense = (TextView) findViewById(R.id.sense);
//        slider.setMax(49);
//        slider.setProgress(9);
        slider.setMax(10);
        slider.setProgress(5);

        //Timer
        final Runnable updater = new Runnable() {
            @Override
            public void run() {
                if (isPressed) {
                    time = (System.currentTimeMillis() - init) / 1000.0;
                    timer.setText(df.format(time));
                    handler.postDelayed(this, 30);
                }
            }
        };
        timer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                init = System.currentTimeMillis();
                if (isPressed) newTime(time);
                else {
                    isPressed = true;
                    timer.setTextColor(Color.BLACK);
                    handler.post(updater);
                }}
        });
        timer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!isPressed) timer.setTextColor(Color.GREEN);
                return false;
            }
        });

        /*timer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                init = System.currentTimeMillis();
                timer.setTextColor(Color.BLACK);
                if(!justPressed){       //first time pressed
                    isPressed = true;
                    handler.post(updater);
                }}
        });
        timer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(isPressed){isPressed = false; justPressed = true;}       //pressed to stop timer
                else{timer.setTextColor(Color.GREEN); justPressed = false;}
                return false;
            }
        });*/


        reset.setOnClickListener(new View.OnClickListener() {       //doesn't rly do anything rn
            @Override
            public void onClick(View view) {
                timer.setText("0.000");
                isPressed = false;
            }
        });

        //SeekBar
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                sensitivity = ((double)(i+1))/10;
//                sense.setText("Sensitivity: " + sensitivity);
                if(i<=5)sensitivity=-.8*i+5;
                else sensitivity=-.18*i+1.9;
                sense.setText("Sensitivity: "+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //time list
        time1 = (Button) findViewById(R.id.time1);
        time2 = (Button) findViewById(R.id.time2);
        time3 = (Button) findViewById(R.id.time3);
        time4 = (Button) findViewById(R.id.time4);
        time5 = (Button) findViewById(R.id.time5);
        avg5 = (TextView)findViewById(R.id.avg5);

        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, InfoActivity.class);
                i.putExtra("scramble", scrambles[1]);
                i.putExtra("time", timelist[0]);
                startActivity(i);
            }
        });
        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, InfoActivity.class);
                i.putExtra("scramble", scrambles[2]);
                i.putExtra("time", timelist[1]);
                startActivity(i);
            }
        });
        time3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, InfoActivity.class);
                i.putExtra("scramble", scrambles[3]);
                i.putExtra("time", timelist[2]);
                startActivity(i);
            }
        });
        time4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, InfoActivity.class);
                i.putExtra("scramble", scrambles[4]);
                i.putExtra("time", timelist[3]);
                startActivity(i);
            }
        });
        time5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, InfoActivity.class);
                i.putExtra("scramble", scrambles[5]);
                i.putExtra("time", timelist[4]);
                startActivity(i);
            }
        });

    }

    public void newTime(double time){
        isPressed = false;
        String currScramble = genScramble();
        scramble.setText(currScramble);            //DOESN'T SAVE TO SCRAMBLES YET
        for(int i = timelist.length-1; i>0; i--)
            timelist[i]=timelist[i-1];
        timelist[0] = time;
        time1.setText(df.format(time));
        if(timelist[1]!=-1)time2.setText(df.format(timelist[1]));
        if(timelist[2]!=-1)time3.setText(df.format(timelist[2]));
        if(timelist[3]!=-1)time4.setText(df.format(timelist[3]));
        if(timelist[4]!=-1)time5.setText(df.format(timelist[4]));

        scrambles[5] = scrambles[4];
        if(!scrambles[3].equals("N/A")) scrambles[4]=scrambles[3];
        if(!scrambles[2].equals("N/A")) scrambles[3]=scrambles[2];
        if(!scrambles[1].equals("N/A")) scrambles[2]=scrambles[1];
        if(!scrambles[0].equals("N/A")) scrambles[1]=scrambles[0];
        scrambles[0]=currScramble;

        if(timelist[4]!=-1){
            double sum = 0.0;
            double min = timelist[0];
            double max = timelist[0];
            for(int i = 0; i<timelist.length-1; i++) {
                sum += timelist[i];
                if(timelist[i]<min)min=timelist[i];
                if(timelist[i]>max)max=timelist[i];
            }
            avg5.setText("Average of 5: " + df.format((sum-min-max)/3.0));
        }
    }

    //Accelerometer
    @Override
    public void onResume() {
        super.onResume();
        sensorMan.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMan.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mGravity = event.values.clone();
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            double d = Math.sqrt(x*x + y*y + z*z);
            mAccelCurrent = (float)d;
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if(mAccel > sensitivity){         //default = 3, 1 works pretty well, should prolly have a setting to change sensitivity
                if(isPressed)newTime(time);  //5 is highest, 0.1 is lowest
            }}

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // required method
    }

    public String genScramble(){
        String d = "";
        int k;
        int oldK = -4;
        int opp = -99;
        int oldopp = -99;

        for(int i = 0;i<15;i++)
        {
            k = (int)(Math.random() * 18 + 1);

            if(Math.abs(opp-oldopp)==5)
                while ((k<4||(k>9&&k<13))||(oldK<k && k<=oldK+3))
                    k = (int)(Math.random() * 18 + 1);
            else if(Math.abs(opp-oldopp)==7)
                while (((k>3&&k<7)||(k>12&&k<16))||(oldK<k && k<=oldK+3))
                    k = (int)(Math.random() * 18 + 1);
            else if(Math.abs(opp-oldopp)==9)
                while (((k>6&&k<10)||k>15)||(oldK<k && k<=oldK+3))
                    k = (int)(Math.random() * 18 + 1);
            else
            {
                while(oldK<k && k<=oldK+3)
                    k = (int)(Math.random() * 18 + 1);
            }
            if(k<=3){oldK = 0;}
            else if(k<=6){oldK = 3;}
            else if(k<=9){oldK = 6;}
            else if(k<=12){oldK = 9;}
            else if(k<=15){oldK = 12;}
            else{oldK = 15;}

            oldopp = opp;
            if(k==1){d += "R ";opp = 1;}
            if(k==2){d += "R' ";opp = 1;}
            if(k==3){d += "R2 ";opp = 1;}
            if(k==4){d += "U ";opp = 2;}
            if(k==5){d += "U' ";opp = 2;}
            if(k==6){d += "U2 ";opp = 2;}
            if(k==7){d += "F ";opp = 3;}
            if(k==8){d += "F' ";opp = 3;}
            if(k==9){d += "F2 ";opp = 3;}
            if(k==10){d += "L ";opp = 6;}
            if(k==11){d += "L' ";opp = 6;}
            if(k==12){d += "L2 ";opp = 6;}
            if(k==13){d += "D ";opp = 9;}
            if(k==14){d += "D' ";opp = 9;}
            if(k==15){d += "D2 ";opp = 9;}
            if(k==16){d += "B ";opp = 12;}
            if(k==17){d += "B' ";opp = 12;}
            if(k==18){d += "B2 ";opp = 12;}
        }
        return d;
    }
}
