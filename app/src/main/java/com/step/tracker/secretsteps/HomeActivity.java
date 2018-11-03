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
    SharedPreferences sharedPreferences,sharedPreferencesActive,sharedPreferencesPassive,sharedPreferencesWorkout,sharedPreferencesUserName;
    SharedPreferences.Editor editor1, editor2,editor3,editor4;
    int yesPassive,yesActive,yesWorkout;
    private boolean alarmSet=false;
    int workout;
    static int a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        sensorManagerHome = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        beginWorkoutBtn.setOnClickListener(HomeActivity.this);
        //get activesteps,passivesteps,username and workouts
        sharedPreferencesActive = getSharedPreferences("ActiveStepsInfo",MODE_PRIVATE);
        int activeSteps = sharedPreferencesActive.getInt("ActiveSteps", 0);
        totalActiveSteps = activeSteps;

        sharedPreferences = getSharedPreferences("PassiveStepsInfo",MODE_PRIVATE);
        int passiveSteps = sharedPreferences.getInt("PassiveSteps",0);

        sharedPreferencesPassive = getSharedPreferences("PassiveSubInfo",0);
        int passiveSub = sharedPreferencesPassive.getInt("PassiveSubSteps",0);

        sharedPreferencesWorkout = getSharedPreferences("WorkoutInfo",MODE_PRIVATE);
        workout = sharedPreferencesWorkout.getInt("WorkoutCount",0);

        sharedPreferencesUserName = getSharedPreferences("UserNameInfo",0);
        String userName = sharedPreferencesUserName.getString("UserName","");


        a =10;
        //setting today's text
        do{
        setAlarm();
        activeWorkouts.setText("Workouts: "+(workout-yesWorkout));
        todayActiveTv.setText("Active Steps: "+(activeSteps-yesActive));
        todayPassiveTv.setText("Passive Steps: "+(passiveSub-yesPassive));
        welcome.setText("Welcome "+userName);


        Boolean isAlarmSet = getSharedPreferences("Alarm", MODE_PRIVATE)
                .getBoolean("AlarmSet", false);
        if(isAlarmSet == true) {

            getSharedPreferences("Alarm", MODE_PRIVATE).edit()
                    .putBoolean("AlarmSet", false).commit();

            yesterdayActive.setText("Active Steps: "+(activeSteps-yesActive));
            yesterdayPassive.setText("Passive Steps: "+(passiveSub-yesPassive));
            yesterdayWorkouts.setText("Workouts: "+(workout-yesWorkout));
            yesterdayTotal.setText("Total Step Count: "+((activeSteps-yesActive)+(passiveSub-yesPassive)));

        }}while(a==20);


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
//        intent.putExtra("isLaunched", isLaunched);
        startActivityForResult(intent,RESQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK){
            totalActiveSteps = totalActiveSteps + data.getIntExtra("result",0);
            totalWorkouts = data.getIntExtra("workoutCount",0);

            todayActiveTv.setText("Active Steps : "+(totalActiveSteps-yesActive));
            activeWorkouts.setText("Workouts: "+totalWorkouts);
            //saving active steps
            editor1 = sharedPreferencesActive.edit();
            editor1.putLong("Timestamp",Calendar.getInstance().getTimeInMillis());
            editor1.putInt("ActiveSteps",totalActiveSteps);
            editor1.commit();
            //saving workouts
            sharedPreferencesWorkout = getSharedPreferences("WorkoutInfo",0);
            editor4 = sharedPreferencesWorkout.edit();
            editor4.putInt("WorkoutCount",totalWorkouts);
            editor4.commit();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        totalPassiveSteps = (int)(event.values[0]);
        int diffInSteps = (totalPassiveSteps-totalActiveSteps);
        todayPassiveTv.setText("Passive steps: "+(diffInSteps-yesPassive) );
        //saving passive steps,timestamp
        editor2 = sharedPreferences.edit();
        editor2.putLong("Timestamp",Calendar.getInstance().getTimeInMillis());
        editor2.putInt("PassiveSteps",(totalPassiveSteps));
        editor2.commit();
        //saving subtracted passive steps
        sharedPreferencesPassive = getSharedPreferences("PassiveSubInfo",0);
        editor3 = sharedPreferencesPassive.edit();
        editor3.putInt("PassiveSubSteps",diffInSteps);
        editor3.commit();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void setAlarm() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 14);

        Intent intent = new Intent(HomeActivity.this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);
    }

}
