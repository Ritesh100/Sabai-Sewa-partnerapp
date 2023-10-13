package com.cscodetech.partner.activity;

import static com.cscodetech.partner.utils.SessionManager.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cscodetech.partner.R;
import com.cscodetech.partner.model.User;
import com.cscodetech.partner.utils.SessionManager;
import com.onesignal.OneSignal;


public class FirstActivity extends AppCompatActivity {

    SessionManager sessionManager;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        sessionManager = new SessionManager(FirstActivity.this);
        user = sessionManager.getUserDetails("");
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (sessionManager.getBooleanData(login)) {
                        if (!user.getCategoryid().equalsIgnoreCase("0")) {
                            OneSignal.deleteTag("Categoryid");
                            OneSignal.sendTag("PartnerId", user.getId());
                            Intent openMainActivity = new Intent(FirstActivity.this, HomeActivity.class);
                            startActivity(openMainActivity);
                            finish();
                        } else {
                            Intent openMainActivity = new Intent(FirstActivity.this, CategoryActivity.class);
                            startActivity(openMainActivity);
                            finish();
                        }

                    } else {
                        Intent openMainActivity = new Intent(FirstActivity.this, LoginActivity.class);
                        startActivity(openMainActivity);
                        finish();
                    }

                }
            }
        };
        timer.start();
    }
}