package com.step.tracker.secretsteps;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    private static final String TAG = HomeActivity.class.getSimpleName() ;
    @BindView(R.id.tv_today_active)
    TextView todayActiveTv;

    @BindView(R.id.tv_today_passive)
    TextView todayPassiveTv;

    @BindView(R.id.tv_yesterday_active)
    TextView yesterdayActive;

    @BindView(R.id.tv_yesterday_passive)
    TextView yesterdayPassive;

    @BindView(R.id.tv_yesterday_workouts)
    TextView yesterdayWorkouts;

    @BindView(R.id.tv_yesterday_total)
    TextView yesterdayTotal;

    @BindView(R.id.btn_begin_workout)
    Button beginWorkoutBtn;

    @BindView(R.id.tv_workout)
    TextView activeWorkouts;

    @BindView(R.id.welcome)
    TextView welcome;
//    private int stepsActive =0;
    private int RESQUEST_CODE = 1;
    boolean isLaunched=true;
    private SensorManager sensorManagerHome;
    private int totalActiveSteps ;
    private int totalPassiveSteps ;
    private int totalWorkouts;
    SharedPreferences sharedPreferences,sharedPreferencesActive,sharedPreferencesWorkout,sharedPreferencesUserName;
    SharedPreferences.Editor editor1,editor2,editor3;

    long ysPassive,ysActive;
    private boolean alarmSet=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        sensorManagerHome = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        beginWorkoutBtn.setOnClickListener(HomeActivity.this);
//get activesteps for steps
        sharedPreferencesActive = getSharedPreferences("ActiveStepsInfo",MODE_PRIVATE);
        int activeSteps = sharedPreferencesActive.getInt("ActiveSteps", 0);

        sharedPreferencesWorkout = getSharedPreferences("WorkoutInfo",MODE_PRIVATE);
        int workout = sharedPreferencesWorkout.getInt("WorkoutCount",0);

        sharedPreferencesUserName = getSharedPreferences("UserNameInfo",0);
        String userName = sharedPreferencesUserName.getString("UserName","");

        activeWorkouts.setText("Workouts: "+workout);
        todayActiveTv.setText("Active Steps: "+activeSteps);
        todayPassiveTv.setText("Passive Steps: "+  (totalPassiveSteps-totalActiveSteps));
        sharedPreferences = getSharedPreferences("PassiveStepsInfo",MODE_PRIVATE);
        welcome.setText("    Welcome "+userName);
        }




    @Override
    protected void onResume() {
        super.onResume();
        Sensor countSensor = sensorManagerHome.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManagerHome.registerListener(HomeActivity.this, countSensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        sensorManagerHome.unregisterListener(HomeActivity.this);
        Intent intent = new Intent(HomeActivity.this,WorkoutActivity.class);
        intent.putExtra("isLaunched", isLaunched);
        startActivityForResult(intent,RESQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK){
            totalActiveSteps = totalActiveSteps + data.getIntExtra("result",0);
            totalWorkouts = data.getIntExtra("workoutCount",0);
            Log.d(TAG, "onActivityResult:totalWorkouts "+totalWorkouts);
            todayActiveTv.setText("Active Steps : "+(totalActiveSteps));
            activeWorkouts.setText("Workouts: "+totalWorkouts);
            //saving active steps
            sharedPreferencesActive = getSharedPreferences("ActiveStepsInfo",0);
            editor1 = sharedPreferencesActive.edit();
            editor1.putLong("Timestamp",Calendar.getInstance().getTimeInMillis());
            editor1.putInt("ActiveSteps",totalActiveSteps);
            editor1.commit();

            sharedPreferencesWorkout = getSharedPreferences("WorkoutInfo",0);
            editor2 = sharedPreferencesWorkout.edit();
            editor2.putInt("WorkoutCount",totalWorkouts);
            editor2.commit();

        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        totalPassiveSteps = (int)(event.values[0]);
        todayPassiveTv.setText("Passive steps: "+ (totalPassiveSteps-totalActiveSteps));
        //saving passive steps
        editor3 = sharedPreferences.edit();
        long stamp =  Calendar.getInstance().getTimeInMillis();
        editor3.putLong("Timestamp",stamp);
        editor3.putInt("PassiveSteps",(totalPassiveSteps));
        editor3.commit();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
