package com.step.tracker.secretsteps;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferencesUserName;

    String Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText nameEditText = (EditText) findViewById(R.id.ed_name);
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", false);

        if (isFirstRun == false) {
            Button letsBegin = (Button) findViewById(R.id.btn_lets_begin);
            letsBegin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(nameEditText.getText())) {
                        nameEditText.setError("Please enter your name");
                    } else {
                        Username = nameEditText.getText().toString();
                        sharedPreferencesUserName = getSharedPreferences("UserNameInfo", 0);
                        SharedPreferences.Editor editor = sharedPreferencesUserName.edit();
                        editor.putString("UserName", Username);
                        editor.commit();
                        setAlarm();
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    }
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                            .putBoolean("isFirstRun", true).commit();
                }
            });
        } else {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }
    }

    private void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,17);
        calendar.set(Calendar.MINUTE, 9);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*2, pendingIntent);
    }
}



