package vicaire.tommy.moodtracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static vicaire.tommy.moodtracker.MainActivity.LINK;
import static vicaire.tommy.moodtracker.MainActivity.MAX_DAY;
import static vicaire.tommy.moodtracker.MainActivity.PREF_KEY_COMMENT;
import static vicaire.tommy.moodtracker.MainActivity.PREF_KEY_DATE;
import static vicaire.tommy.moodtracker.MainActivity.PREF_KEY_INDEX;
import static vicaire.tommy.moodtracker.MainActivity.PREF_KEY_SAVED_MOOD;
import static vicaire.tommy.moodtracker.MainActivity.mMoodArrayList;
import static vicaire.tommy.moodtracker.Mood.LayoutColor.BLUE;
import static vicaire.tommy.moodtracker.Mood.LayoutColor.GREEN;
import static vicaire.tommy.moodtracker.Mood.LayoutColor.GREY;
import static vicaire.tommy.moodtracker.Mood.LayoutColor.RED;
import static vicaire.tommy.moodtracker.Mood.LayoutColor.YELLOW;

public class Utils {

    //  public static void sendMoodToHistory(Context context) {

    public static void sendMoodToHistory(Context context) {



         Calendar mCalendar = Calendar.getInstance();
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        ArrayList<Mood> savedMoods = new ArrayList<>();




        int mCurrentIndex;
        String mComment;

        String mCurrentDate = mSimpleDateFormat.format(mCalendar.getTime());
        String mDateSaved;
        SharedPreferences moodPreferences = context.getApplicationContext().getSharedPreferences(LINK, MODE_PRIVATE);

        mCurrentIndex = moodPreferences.getInt(PREF_KEY_INDEX , 0);


        mComment = moodPreferences.getString(PREF_KEY_COMMENT, null);


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

        if (mDateSaved.equals(mCurrentDate)) {
            // If there is more than 7 days , it removes the last index to avoid the out of bounds
            if (savedMoods.size() >= MAX_DAY) {
                savedMoods.remove(savedMoods.size() - 1);
            }
            savedMoods.add(0, mMoodArrayList.get(mCurrentIndex));
            savedMoods.get(0).setMoodComment(mComment);

            Gson mGson = new Gson();
            String mJson = mGson.toJson(savedMoods);
            SharedPreferences.Editor editor = moodPreferences.edit();

            editor.putString(PREF_KEY_COMMENT, null);
            editor.putString(PREF_KEY_DATE, mCurrentDate);
            editor.remove(PREF_KEY_INDEX);

            editor.apply();

            moodPreferences.edit().putString(PREF_KEY_SAVED_MOOD, mJson).apply();


        }
    }

}
