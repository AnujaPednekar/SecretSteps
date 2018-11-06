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

    private static final String TAG = HomeActivity.class.getSimpleName();
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
    private int RESQUEST_CODE = 1;
    private SensorManager sensorManagerHome;
    private int totalActiveSteps;
    private int totalPassiveSteps;
    private int totalWorkouts;
    SharedPreferences sharedPreferences, sharedPreferencesActive, sharedPreferencesPassive, sharedPreferencesWorkout, sharedPreferencesUserName;
    SharedPreferences.Editor editor1, editor2, editor3, editor4;
    int yesPassive, yesActive, yesWorkout;
    private boolean alarmSet = false;
    private int activeSteps, passiveSub;
    int workout;
    private static HomeActivity homeActivityRunningInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        homeActivityRunningInstance = this;
        sensorManagerHome = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        beginWorkoutBtn.setOnClickListener(HomeActivity.this);
        //get activesteps,passivesteps,username and workouts
        sharedPreferencesActive = getSharedPreferences("ActiveStepsInfo", MODE_PRIVATE);
        activeSteps = sharedPreferencesActive.getInt("ActiveSteps", 0);
        totalActiveSteps = activeSteps;

        sharedPreferences = getSharedPreferences("PassiveStepsInfo", MODE_PRIVATE);
        int passiveSteps = sharedPreferences.getInt("PassiveSteps", 0);

        sharedPreferencesPassive = getSharedPreferences("PassiveSubInfo", 0);
        passiveSub = sharedPreferencesPassive.getInt("PassiveSubSteps", 0);

        sharedPreferencesWorkout = getSharedPreferences("WorkoutInfo", MODE_PRIVATE);
        workout = sharedPreferencesWorkout.getInt("WorkoutCount", 0);

        sharedPreferencesUserName = getSharedPreferences("UserNameInfo", 0);
        String userName = sharedPreferencesUserName.getString("UserName", "");

        Boolean isAlarmSet = getSharedPreferences("AlarmInfo", 0)
                .getBoolean("isAlarmSet", false);
        if(isAlarmSet==false){
        setAlarm();
        }

        yesWorkout = getSharedPreferences("YesterdayWorkoutInfo", 0).getInt("YesterdayWorkout", 0);
        yesActive = getSharedPreferences("YesterdayActiveInfo", 0).getInt("YesterdayActiveSteps", 0);
        yesPassive = getSharedPreferences("YesterdayPassiveInfo", 0).getInt("YesterdayPassiveSteps", 0);

        //setting today's steps
        activeWorkouts.setText("Workouts: " +((getSharedPreferences("WorkoutInfo",0).getInt("WorkoutCount",0))-(getSharedPreferences("YesterdayWorkoutInfo", 0).getInt("YesterdayWorkout", 0))));
        todayActiveTv.setText("Active Steps: " + (activeSteps - yesActive));
        todayPassiveTv.setText("Passive Steps: " +(getSharedPreferences("PassiveSubInfo",0).getInt("PassiveSubSteps",0) - (getSharedPreferences("YesterdayPassiveInfo",0).getInt("YesterdayPassiveSteps",0))));
        welcome.setText("Welcome " + userName);

        //setting yesterday's steps
        yesterdayActive.setText("Active Steps: " + (getSharedPreferences("YesterdayActiveInfo", 0).getInt("YesterdayActiveSteps", 0)));
        yesterdayPassive.setText("Passive Steps: " + (getSharedPreferences("YesterdayPassiveInfo", 0).getInt("YesterdayPassiveSteps", 0)));
        yesterdayWorkouts.setText("Workouts: " + (getSharedPreferences("YesterdayWorkoutInfo", 0).getInt("YesterdayWorkout", 0)));
        yesterdayTotal.setText("Total Step Count: " + ((yesActive) + (yesPassive)));
    }

    public static HomeActivity getInstace() {
        return homeActivityRunningInstance;
    }

    public void updateYesterday(final int active, final int passive, final int workout) {
        HomeActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                yesterdayActive.setText("Active Step: " + active);
                yesterdayPassive.setText("Passive Steps: " + passive);
                yesterdayWorkouts.setText("Workouts: " + workout);
                yesterdayTotal.setText("Total Step Count: "+ (active+passive));
            }
        });
    }

    public void updateToday(final int active, final int passive, final int workoutM) {
        HomeActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                todayActiveTv.setText("Active Step: " + (active));
                todayPassiveTv.setText("Passive Steps: " + (passive));
                activeWorkouts.setText("Workouts: " + (workoutM));
                Log.d(TAG, "Active Steps: "+activeSteps+"  YesterdayActiveStep: "+active+"  To be set: "+(activeSteps-active));
                Log.d(TAG, "Passive Steps: "+passiveSub+"  YesterdayPassiveStep: "+passive+"  To be set: "+(passiveSub-passive));
                Log.d(TAG, "Active Workouts: "+workout+"  YesterdayWorkout: "+workoutM+"  To be set: "+(workout-workoutM));

            }
        });
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
        Intent intent = new Intent(HomeActivity.this, WorkoutActivity.class);
        startActivityForResult(intent, RESQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            totalActiveSteps = totalActiveSteps + data.getIntExtra("result", 0);
            totalWorkouts = data.getIntExtra("workoutCount", 0);
            todayActiveTv.setText("Active Steps : " + (totalActiveSteps - getSharedPreferences("YesterdayActiveInfo", 0).getInt("YesterdayActiveSteps", 0)));
            activeWorkouts.setText("Workouts: " +(totalWorkouts - (getSharedPreferences("YesterdayWorkoutInfo",0).getInt("YesterdayWorkout",0)) ));
            //saving active steps
            editor1 = sharedPreferencesActive.edit();
            editor1.putLong("Timestamp", Calendar.getInstance().getTimeInMillis());
            editor1.putInt("ActiveSteps", totalActiveSteps);
            editor1.commit();
            //saving today's workouts
            sharedPreferencesWorkout = getSharedPreferences("WorkoutInfo", 0);
            editor4 = sharedPreferencesWorkout.edit();
            editor4.putInt("WorkoutCount", totalWorkouts);
            editor4.commit();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        totalPassiveSteps = (int) (event.values[0]);
        int diffInSteps = (totalPassiveSteps - totalActiveSteps);
//        todayPassiveTv.setText("Passive steps: " + (diffInSteps - getSharedPreferences("YesterdayPassiveInfo", 0).getInt("YesterdayPassiveSteps", 0)));
        todayPassiveTv.setText("Passive Steps: "+(getSharedPreferences("PassiveSubInfo",0).getInt("PassiveSubSteps",0) - (getSharedPreferences("YesterdayPassiveInfo",0).getInt("YesterdayPassiveSteps",0))));
        //saving passive steps,timestamp
        editor2 = sharedPreferences.edit();
        editor2.putLong("Timestamp", Calendar.getInstance().getTimeInMillis());
        editor2.putInt("PassiveSteps", (totalPassiveSteps));
        editor2.commit();
        //saving subtracted passive steps
        sharedPreferencesPassive = getSharedPreferences("PassiveSubInfo", 0);
        editor3 = sharedPreferencesPassive.edit();
        editor3.putInt("PassiveSubSteps", diffInSteps);
        editor3.commit();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void setAlarm() {
        getSharedPreferences("AlarmInfo", MODE_PRIVATE).edit()
                .putBoolean("isAlarmSet", true).commit();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);

        if(calendar.getTimeInMillis()>System.currentTimeMillis()) {
            Intent intent = new Intent(HomeActivity.this, MyBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),1000 * 60 * 60 * 24, pendingIntent);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}
