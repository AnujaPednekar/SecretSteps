package com.step.tracker.secretsteps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;


public class MyBroadcastReceiver extends BroadcastReceiver {
    SharedPreferences spYesterdayTotalSteps, spYesterdayPassiveSteps, spYesterdayActiveSteps , spYesterdayWorkouts;
    SharedPreferences.Editor editor1,editor2,editor3;
    @Override
    public void onReceive(Context arg0, Intent intent) {

        //getting today's data
        SharedPreferences sharedPreferencesWorkout = arg0.getSharedPreferences("WorkoutInfo",Context.MODE_PRIVATE);
        int workouts = sharedPreferencesWorkout.getInt("WorkoutCount",0);

        SharedPreferences sharedPreferencesPassive = arg0.getSharedPreferences("PassiveSubInfo", Context.MODE_PRIVATE);
        int passiveSteps = sharedPreferencesPassive.getInt("PassiveSubSteps", 0);

        SharedPreferences sharedPreferencesActive = arg0.getSharedPreferences("ActiveStepsInfo", Context.MODE_PRIVATE);
        int activeSteps = sharedPreferencesActive.getInt("ActiveSteps", 0);

        //storing today's data to yesterday sp
        spYesterdayActiveSteps = arg0.getSharedPreferences("YesterdayActiveInfo",0);
        editor1 = spYesterdayActiveSteps.edit();
        editor1.putInt("YesterdayActiveSteps",activeSteps);
        editor1.commit();

        spYesterdayPassiveSteps = arg0.getSharedPreferences("YesterdayPassiveInfo",0);
        editor2 = spYesterdayPassiveSteps.edit();
        editor2.putInt("YesterdayPassiveSteps",passiveSteps);
        editor2.commit();

        spYesterdayWorkouts = arg0.getSharedPreferences("YesterdayWorkoutInfo",0);
        editor3 = spYesterdayWorkouts.edit();
        editor3.putInt("YesterdayWorkout",workouts);
        editor3.commit();

        arg0.getSharedPreferences("Alarm", 0).edit()
                .putBoolean("AlarmSet", true).commit();

        HomeActivity.a = 20;
//        int workout = spYesterdayWorkouts.getInt("YesterdayWorkout",0);
//        int active = spYesterdayActiveSteps.getInt("YesterdayActiveSteps",0);
//        int passive = spYesterdayPassiveSteps.getInt("YesterdayPassiveSteps",0);


    }
}
