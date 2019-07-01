package cerebrik.com.alarmapp.receiver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import cerebrik.com.alarmapp.service.AlarmService;


public class AlarmReceiver extends WakefulBroadcastReceiver {
    private final String TAG = "AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Start alarm service");
        //Start service for show notification
        ComponentName componentName = new ComponentName(context.getPackageName(), AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(componentName)));
        setResultCode(Activity.RESULT_OK);
    }
}
