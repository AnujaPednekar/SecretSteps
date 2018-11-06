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
    public void onReceive(Context context, Intent intent) {

        //getting today's data
        SharedPreferences sharedPreferencesWorkout = context.getSharedPreferences("WorkoutInfo",Context.MODE_PRIVATE);
        int workouts = sharedPreferencesWorkout.getInt("WorkoutCount",0);

        SharedPreferences sharedPreferencesPassive = context.getSharedPreferences("PassiveSubInfo", Context.MODE_PRIVATE);
        int passiveSteps = sharedPreferencesPassive.getInt("PassiveSubSteps", 0);

        SharedPreferences sharedPreferencesActive = context.getSharedPreferences("ActiveStepsInfo", Context.MODE_PRIVATE);
        int activeSteps = sharedPreferencesActive.getInt("ActiveSteps", 0);

        //storing today's data to yesterday sp
        spYesterdayActiveSteps = context.getSharedPreferences("YesterdayActiveInfo",0);
        editor1 = spYesterdayActiveSteps.edit();
        editor1.putInt("YesterdayActiveSteps",activeSteps);
        editor1.commit();

        spYesterdayPassiveSteps = context.getSharedPreferences("YesterdayPassiveInfo",0);
        editor2 = spYesterdayPassiveSteps.edit();
        editor2.putInt("YesterdayPassiveSteps",passiveSteps);
        editor2.commit();

        spYesterdayWorkouts = context.getSharedPreferences("YesterdayWorkoutInfo",0);
        editor3 = spYesterdayWorkouts.edit();
        editor3.putInt("YesterdayWorkout",workouts);
        editor3.commit();


        sharedPreferencesActive.edit().putInt("ActiveSteps",0).commit();
        sharedPreferencesPassive.edit().putInt("PassiveSubSteps",0).commit();
        sharedPreferencesWorkout.edit().putInt("WorkoutCount",0).commit();

        int active = context.getSharedPreferences("YesterdayActiveInfo",0).getInt("YesterdayActiveSteps",0);
        int passive = context.getSharedPreferences("YesterdayPassiveInfo",0).getInt("YesterdayPassiveSteps",0);
        int workout = context.getSharedPreferences("YesterdayWorkoutInfo",0).getInt("YesterdayWorkout",0);

        HomeActivity.getInstace().updateToday(0,0,0);
        HomeActivity.getInstace().updateYesterday((activeSteps),(passiveSteps),(workouts));
    }
}
