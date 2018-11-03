package com.step.tracker.secretsteps;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkoutActivity extends AppCompatActivity implements View.OnClickListener, Chronometer.OnChronometerTickListener, SensorEventListener {

    @BindView(R.id.tv_total_steps)
    TextView totalStepsTv;

    @BindView(R.id.btn_end_workout)
    Button endWorkoutBtn;

    @BindView(R.id.app_chronometer)
    Chronometer chronometer;

    public int todayActiveSteps;
    static int workoutCount;
    private SensorManager sensorManager;
    int totalStepSinceReboot;
    private String TAG = WorkoutActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        ButterKnife.bind(this);
//        Intent intent = getIntent();
//        todayActiveSteps = intent.getIntExtra("todayActiveSteps", 0);
//        totalStepsTv.setText("0");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        workoutCount++;
        startChronometer();
        endWorkoutBtn.setOnClickListener(this);
//        sharedPreferencesActive = getSharedPreferences("ActiveStepsInfo",MODE_PRIVATE);

    }


    @Override
    protected void onResume() {
        super.onResume();
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(WorkoutActivity.this, countSensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        long timestamp;
        int passiveSteps = 0;
        //get passively tracked steps
        SharedPreferences sharedPreferences = getSharedPreferences("PassiveStepsInfo", MODE_PRIVATE);
        timestamp = sharedPreferences.getLong("Timestamp", 0);
        passiveSteps = sharedPreferences.getInt("PassiveSteps", 0);
        //calculate active steps
        totalStepSinceReboot = (int) (event.values[0]);
        todayActiveSteps = totalStepSinceReboot - passiveSteps;
        totalStepsTv.setText(String.valueOf(todayActiveSteps));
        //saving active steps
//        sharedPreferencesActive = getSharedPreferences("ActiveStepsInfo",0);
//        editor2 = sharedPreferencesActive.edit();
//        editor2.putInt("ActiveSteps",todayActiveSteps);
//        editor2.putLong("Timestamp",Calendar.getInstance().getTimeInMillis());
//        editor2.commit();


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void showWorkoutEndAlert() {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(WorkoutActivity.this);
        builder.setTitle("Finish workout")
                .setMessage("Are you sure you want to finish the workout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", todayActiveSteps);
                        returnIntent.putExtra("workoutCount", workoutCount);
                        setResult(RESULT_OK, returnIntent);
                        sensorManager.unregisterListener(WorkoutActivity.this);
                        chronometer.stop();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void startChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        chronometer.setOnChronometerTickListener(this);
    }

    public void onChronometerTick(Chronometer arg0) {
        long time = SystemClock.elapsedRealtime() - chronometer.getBase();
        int hrs = (int) (time / 3600000);
        int mins = (int) (time - hrs * 3600000) / 60000;
        int secs = (int) (time - hrs * 3600000 - mins * 60000) / 1000;
        if (hrs > 0) {
            String timeInHMS = (hrs < 10 ? "0" + hrs : hrs) + ":" + (mins < 10 ? "0" + mins : mins) + ":" + (secs < 10 ? "0" + secs : secs);
            chronometer.setText(timeInHMS);
        } else {
            String timeInMS = (mins < 10 ? "0" + mins : mins) + ":" + (secs < 10 ? "0" + secs : secs);
            chronometer.setText(timeInMS);
        }
    }

    public void onClick(View v) {
        showWorkoutEndAlert();
    }


    @Override
    public void onBackPressed() {
        showWorkoutEndAlert();
    }

}


