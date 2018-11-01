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
        SharedPreferences sharedPreferences = arg0.getSharedPreferences("PassiveStepsInfo", Context.MODE_PRIVATE);
        long passiveSteps = sharedPreferences.getInt("PassiveSteps", 0);

        SharedPreferences sharedPreferencesActive = arg0.getSharedPreferences("ActiveStepsInfo", Context.MODE_PRIVATE);
        long activeSteps = sharedPreferencesActive.getInt("ActiveSteps", 0);

        SharedPreferences sharedPreferencesWorkout = arg0.getSharedPreferences("WorkoutInfo",Context.MODE_PRIVATE);
        int workouts = sharedPreferencesWorkout.getInt("WorkoutCount",0);

        spYesterdayActiveSteps = arg0.getSharedPreferences("YesterdayActiveInfo",0);
        editor1 = spYesterdayActiveSteps.edit();
        editor1.putLong("YesterdayActiveSteps",activeSteps);
        editor1.commit();

        spYesterdayPassiveSteps = arg0.getSharedPreferences("YesterdayPassiveInfo",0);
        editor2 = spYesterdayPassiveSteps.edit();
        editor2.putLong("YesterdayPassiveSteps",passiveSteps);
        Log.d(TAG, "onReceive: passive stepsAB:" + arg0.getSharedPreferences("YesterdayPassiveSteps",0));

        spYesterdayWorkouts = arg0.getSharedPreferences("YesterdayWorkoutInfo",0);
        editor3 = spYesterdayWorkouts.edit();
        editor3.putInt("YesterdayWorkout",workouts);

    }
}
