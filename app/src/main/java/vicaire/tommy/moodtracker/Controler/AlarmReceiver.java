package vicaire.tommy.moodtracker.Controler;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import vicaire.tommy.moodtracker.Model.Utils;


public class AlarmReceiver extends BroadcastReceiver {

    // This alarm will call the sendMoodTohistory from the Utils class
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context , "Active" , Toast.LENGTH_SHORT).show();
        Utils.sendMoodToHistory(context);
        Utils.setAlarm(context);

    }
}
