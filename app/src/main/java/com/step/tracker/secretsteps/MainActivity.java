package com.step.tracker.secretsteps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        if(isFirstRun==false){
            Button letsBegin = (Button) findViewById(R.id.btn_lets_begin);
            letsBegin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtils.isEmpty(nameEditText.getText())) {
                      nameEditText.setError("Please enter your name");
                    }
                    else {
                        Username = nameEditText.getText().toString();
                        sharedPreferencesUserName = getSharedPreferences("UserNameInfo", 0);
                        SharedPreferences.Editor editor = sharedPreferencesUserName.edit();
                        editor.putString("UserName", Username);
                        editor.commit();

                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    }
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                            .putBoolean("isFirstRun", true).commit();
                }
            });
        }else {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            }
        }
    }


