package com.sb.android.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by singhsh on 10/5/2019.
 */

public class AppMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_menu);
        RippleView btnradio = (RippleView) findViewById(R.id.btnradio);
        RippleView btnweather = (RippleView) findViewById(R.id.btnweather);

        //On click linsterners implementation
        btnweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(AppMenuActivity.this,"com.sb.homeweather");
            }
        });
        btnradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(AppMenuActivity.this,"com.sb.homeradio");
            }
        });
    }



    public void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            /*intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));*/
            mLogger.mlogger("AppMenuActivity","Package Name not Found");
            return;
        }
        mLogger.mlogger("AppMenuActivity","Package Name Found, Lunching App");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
