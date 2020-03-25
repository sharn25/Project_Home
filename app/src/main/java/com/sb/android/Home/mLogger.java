package com.sb.android.Home;

import android.util.Log;

/**
 * Created by singhsh on 9/15/2019.
 */

public class mLogger {
    public static void mlogger(String tag, String msg){
        Boolean z=true;
        if(z){
            Log.d(tag,msg);
        }
    }
}
