package com.step.tracker.secretsteps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class MyBroadcastReceiver extends BroadcastReceiver {
    SharedPreferences spYesterdayTotalSteps, spYesterdayPassiveStepsToShow, spYesterdayActiveSteps , spYesterdayWorkouts;
    SharedPreferences.Editor editor1,editor2,editor3;
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        arg0.getSharedPreferences("AlarmInfo", 0).edit()
                .putBoolean("isAlarmSet", true).commit();

        SharedPreferences sharedPreferencesPassiveToShow = arg0.getSharedPreferences("PassiveStepsToShow", Context.MODE_PRIVATE);
        int passiveSteps = sharedPreferencesPassiveToShow.getInt("PassiveStepShow", 0);

        SharedPreferences sharedPreferencesActive = arg0.getSharedPreferences("ActiveStepsInfo", Context.MODE_PRIVATE);
        int activeSteps = sharedPreferencesActive.getInt("ActiveSteps", 0);

        SharedPreferences sharedPreferencesWorkout = arg0.getSharedPreferences("WorkoutInfo",Context.MODE_PRIVATE);
        int workouts = sharedPreferencesWorkout.getInt("WorkoutCount",0);

        spYesterdayActiveSteps = arg0.getSharedPreferences("YesterdayActiveInfo",0);
        editor1 = spYesterdayActiveSteps.edit();
        editor1.putInt("YesterdayActiveSteps",activeSteps);
        editor1.commit();

        spYesterdayPassiveStepsToShow = arg0.getSharedPreferences("YesterdayPassiveInfo",0);
        editor2 = spYesterdayPassiveStepsToShow.edit();
        editor2.putInt("YesterdayPassiveSteps",passiveSteps);
        editor2.commit();
//        Log.d(TAG, "onReceive: passive stepsAB:" + arg0.getSharedPreferences("YesterdayPassiveSteps",0));

        spYesterdayWorkouts = arg0.getSharedPreferences("YesterdayWorkoutInfo",0);
        editor3 = spYesterdayWorkouts.edit();
        editor3.putInt("YesterdayWorkout",workouts);
        editor3.commit();

        int active = spYesterdayActiveSteps.getInt("YesterdayActiveSteps",0);
        int passive = spYesterdayPassiveStepsToShow.getInt("YesterdayPassiveSteps",0);
        int workout = spYesterdayWorkouts.getInt("YesterdayPassiveSteps",0);

        SharedPreferences sharedPreferences = arg0.getSharedPreferences("PassiveStepsInfo",0);

        sharedPreferencesActive.edit().putInt("ActiveSteps",0);
        sharedPreferences.edit().putInt("PassiveSteps",0);
        sharedPreferencesWorkout.edit().putInt("WorkoutCount",0);
    }
}
