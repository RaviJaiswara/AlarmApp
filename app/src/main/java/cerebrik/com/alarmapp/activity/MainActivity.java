package cerebrik.com.alarmapp.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import cerebrik.com.alarmapp.R;
import cerebrik.com.alarmapp.receiver.AlarmReceiver;


public class MainActivity extends AppCompatActivity implements MainContract.Views {
    private final String TAG = "MainActivity";
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private TextView alarmTextView;
    private MainContract.Presenters presenters = new MainController(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmTimePicker = findViewById(R.id.alarmTimePicker);
        alarmTextView = findViewById(R.id.alarmText);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    /**
     * Set alarm based on toggle response
     * @param view The Toggle view
     */
    public void onToggleClicked(View view) {
        if (((ToggleButton)view).isChecked()){
            Log.d(TAG, "Alarm On");
            // set alarm
            presenters.setAlarm(alarmTimePicker);
        } else {
            alarmManager.cancel(pendingIntent);
            setAlarmText("");
            Log.d(TAG, "Alarm Off");
        }
    }

    /**
     * Set alarm message
     * @param alarmText The message
     */
    public void setAlarmText(String alarmText){
        alarmTextView.setText(alarmText);
    }


    @Override
    public void onSetAlarm(long alarmTime, long currentTime) {
        if (currentTime <= alarmTime){
            Log.d(TAG, "Display notification will trigger....");
            setAlarmText(getResources().getString(R.string.alarmTrigger));

            // Set broadCast for notification
            Intent intent = new Intent(this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

            // Set alarm on exact time
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarmTime, pendingIntent);
            }

        } else {
            Log.d(TAG, "current time " + currentTime + " > alarm time " + alarmTime);
            setAlarmText(getResources().getString(R.string.pastTime));
        }
    }
}

