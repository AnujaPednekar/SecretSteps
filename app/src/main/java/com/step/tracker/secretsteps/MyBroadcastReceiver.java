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
    SharedPreferences.Editor editor1,editor2,editor3,copyActiveEditor;
    @Override
    public void onReceive(Context context, Intent intent) {

        //getting today's data
        SharedPreferences sharedPreferencesWorkout = context.getSharedPreferences("WorkoutInfo", Context.MODE_PRIVATE);
        int workouts = sharedPreferencesWorkout.getInt("WorkoutCount", 0);

        SharedPreferences sharedPreferencesPassive = context.getSharedPreferences("PassiveSubInfo", Context.MODE_PRIVATE);
        int passiveSteps = sharedPreferencesPassive.getInt("PassiveSubSteps", 0);

        SharedPreferences sharedPreferencesActive = context.getSharedPreferences("ActiveStepsInfo", Context.MODE_PRIVATE);
        int activeSteps = sharedPreferencesActive.getInt("ActiveSteps", 0);
        //copy of active passive
        SharedPreferences copyActiveSP = context.getSharedPreferences("CopyActiveInfo", Context.MODE_PRIVATE);
        int copyActive = copyActiveSP.getInt("CopyActive", 0);

        SharedPreferences copyPassiveSP = context.getSharedPreferences("CopyPassiveInfo", Context.MODE_PRIVATE);
        int copyPassive = copyPassiveSP.getInt("CopyPassive", 0);

        SharedPreferences copyWorkoutSP = context.getSharedPreferences("CopyWorkoutInfo",Context.MODE_PRIVATE);
        int copyworkout = copyWorkoutSP.getInt("CopyWorkout",0);

        //storing today's data to yesterday sp
        spYesterdayActiveSteps = context.getSharedPreferences("YesterdayActiveInfo", 0);
        editor1 = spYesterdayActiveSteps.edit();
//        editor1.putInt("YesterdayActiveSteps", activeSteps);
//        editor1.commit();

        spYesterdayPassiveSteps = context.getSharedPreferences("YesterdayPassiveInfo", 0);
        editor2 = spYesterdayPassiveSteps.edit();
//        editor2.putInt("YesterdayPassiveSteps", passiveSteps);
//        editor2.commit();

        spYesterdayWorkouts = context.getSharedPreferences("YesterdayWorkoutInfo", 0);
        editor3 = spYesterdayWorkouts.edit();
//        editor3.putInt("YesterdayWorkout", workouts);
//        editor3.commit();

        int yesPassive = (context.getSharedPreferences("YesterdayPassiveInfo",0).getInt("YesterdayPassiveSteps",0));
        int yesActive = (context.getSharedPreferences("YesterdayActiveInfo",0).getInt("YesterdayActiveSteps",0));
        if(yesPassive==0 && yesActive==0){
            context.getSharedPreferences("YesterdayActiveInfo",0).edit().putInt("YesterdayActiveSteps",activeSteps).commit();
            context.getSharedPreferences("YesterdayPassiveInfo",0).edit().putInt("YesterdayPassiveSteps",passiveSteps).commit();
            context.getSharedPreferences("YesterdayWorkoutInfo",0).edit().putInt("YesterdayWorkout",workouts).commit();
        }
        else if(yesActive>0 || yesPassive>0){

            int yesterdayActive = activeSteps - copyActiveSP.getInt("CopyActive",0);
            spYesterdayActiveSteps.edit().putInt("YesterdayActiveSteps",yesterdayActive).commit();

            int yesterdayPassive = passiveSteps - copyPassiveSP.getInt("CopyPassive",0);
            spYesterdayPassiveSteps.edit().putInt("YesterdayPassiveSteps",yesterdayPassive).commit();

            int yesterdayWorkout = workouts - copyWorkoutSP.getInt("CopyWorkout",0);
            spYesterdayWorkouts.edit().putInt("YesterdayWorkout",workouts).commit();
            /*int yActive = activeSteps - copyActive;
            editor1.putInt("YesterdayActiveSteps", yActive);
            copyActiveEditor = copyActiveSP.edit();
            copyActiveEditor.putInt("CopyActive",activeSteps);*/
        }




        copyActiveSP.edit().putInt("CopyActive",activeSteps).commit();
        copyPassiveSP.edit().putInt("CopyPassive",passiveSteps).commit();
        copyWorkoutSP.edit().putInt("CopyWorkout",workouts).commit();

//        sharedPreferencesActive.edit().putInt("ActiveSteps",0).commit();
//        sharedPreferencesPassive.edit().putInt("PassiveSubSteps",0).commit();
//        sharedPreferencesWorkout.edit().putInt("WorkoutCount",0).commit();

        int activeY = context.getSharedPreferences("YesterdayActiveInfo", 0).getInt("YesterdayActiveSteps", 0);
        int passiveY = context.getSharedPreferences("YesterdayPassiveInfo", 0).getInt("YesterdayPassiveSteps", 0);
        int workoutY = context.getSharedPreferences("YesterdayWorkoutInfo", 0).getInt("YesterdayWorkout", 0);

//        int activeY = spYesterdayActiveSteps.getInt("YesterdayActiveSteps",0);
//        int passiveY = spYesterdayPassiveSteps.getInt("YesterdayPassiveSteps",0);
//        int workoutY = spYesterdayWorkouts.getInt("YesterdayWorkout",0);
        HomeActivity.getInstace().updateYesterday((spYesterdayActiveSteps.getInt("YesterdayActiveSteps",0)),
                (spYesterdayPassiveSteps.getInt("YesterdayPassiveSteps",0)),
                (spYesterdayWorkouts.getInt("YesterdayWorkout",0)));
        HomeActivity.getInstace().updateToday(0, 0, 0);
    }
}
