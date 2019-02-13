package vicaire.tommy.moodtracker.Model;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vicaire.tommy.moodtracker.Controler.AlarmReceiver;
import vicaire.tommy.moodtracker.Model.Mood;

import static android.content.Context.MODE_PRIVATE;
import static vicaire.tommy.moodtracker.Controler.MainActivity.LINK;
import static vicaire.tommy.moodtracker.Controler.MainActivity.MAX_DAY;
import static vicaire.tommy.moodtracker.Controler.MainActivity.PREF_KEY_COMMENT;
import static vicaire.tommy.moodtracker.Controler.MainActivity.PREF_KEY_DATE;
import static vicaire.tommy.moodtracker.Controler.MainActivity.PREF_KEY_INDEX;
import static vicaire.tommy.moodtracker.Controler.MainActivity.PREF_KEY_MOOD_LIST;
import static vicaire.tommy.moodtracker.Controler.MainActivity.PREF_KEY_SAVED_MOOD;


public class Utils {
    // Setting alarm to fire at midnight
    public static void setAlarm(Context context){
        // Alarm Manager
        AlarmManager alarmManager;
        PendingIntent alarmIntent;
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent newIntent = new Intent(context , AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context , 0 , newIntent , 0);

        Calendar calendar =Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE ,0);
        calendar.set(Calendar.SECOND , 0);
        calendar.set(Calendar.MILLISECOND , 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        Toast.makeText(context , "ALARM SET" , Toast.LENGTH_SHORT).show();

    }

    public static void sendMoodToHistory(Context context) {

        // SharedPreferences
        SharedPreferences moodPreferences = context.getApplicationContext().getSharedPreferences(LINK, MODE_PRIVATE);

        int mCurrentIndex = moodPreferences.getInt(PREF_KEY_INDEX , 0);
        String mComment = moodPreferences.getString(PREF_KEY_COMMENT, null);

        ArrayList<Mood> savedMoods = new ArrayList<>();

       ArrayList<Mood> mMoodArrayList = new ArrayList<>();

        // Calendar parameter
        Calendar mCalendar = Calendar.getInstance();
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String mCurrentDate = mSimpleDateFormat.format(mCalendar.getTime());
        String mDateSaved;

        if (moodPreferences.contains(PREF_KEY_MOOD_LIST )){

            Gson gson = new Gson();
            String json = moodPreferences.getString(PREF_KEY_MOOD_LIST , null);
            Type type = new TypeToken<List<Mood>>(){}.getType();

            mMoodArrayList = gson.fromJson(json , type);
        }


        if (moodPreferences.contains(PREF_KEY_SAVED_MOOD )){

            Gson gson = new Gson();
            String json = moodPreferences.getString(PREF_KEY_SAVED_MOOD , null);
            Type type = new TypeToken<List<Mood>>(){}.getType();

            savedMoods = gson.fromJson(json , type);
        }
        if (moodPreferences.contains(PREF_KEY_DATE)) {

            mDateSaved = moodPreferences.getString(PREF_KEY_DATE, null);
        } else {

            moodPreferences.edit().putString(PREF_KEY_DATE, mCurrentDate).apply();
            mDateSaved = mCurrentDate;
        }

        if (!mDateSaved.equals(mCurrentDate)) {
            // If there is more than 7 days , it removes the last index to avoid the out of bounds
            if (savedMoods.size() >= MAX_DAY ) {
                savedMoods.remove(savedMoods.size() - 1);
            }
            savedMoods.add(0, mMoodArrayList.get(mCurrentIndex));
            savedMoods.get(0).setMoodComment(mComment);

            Gson mGson = new Gson();
            String mJson = mGson.toJson(savedMoods);
            SharedPreferences.Editor editor = moodPreferences.edit();

            editor.putString(PREF_KEY_COMMENT, null);
            editor.putString(PREF_KEY_DATE, mCurrentDate);
            editor.putInt(PREF_KEY_INDEX , 1);

            editor.apply();

            moodPreferences.edit().putString(PREF_KEY_SAVED_MOOD, mJson).apply();




        }
    }

}
