package vicaire.tommy.moodtracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static vicaire.tommy.moodtracker.Mood.LayoutColor.BLUE;
import static vicaire.tommy.moodtracker.Mood.LayoutColor.GREEN;
import static vicaire.tommy.moodtracker.Mood.LayoutColor.GREY;
import static vicaire.tommy.moodtracker.Mood.LayoutColor.RED;
import static vicaire.tommy.moodtracker.Mood.LayoutColor.YELLOW;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

   private RelativeLayout mMoodBackGround;
   private ImageView mMoodImage;
   private Button mCommentButton;
   private Button mHistoryButton;

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;




   private int mCurrentIndex = 1;
   private final int DELTA_MIN = 50;
   private final int MOOD_LENGTH = 4;
   String mComment;



   GestureDetectorCompat mGestureDetectorCompat;


   public static final ArrayList<Mood> mMoodArrayList = new ArrayList<>();
    private ArrayList<Mood> savedMoods = new ArrayList<>();

    SharedPreferences  moodPreferences;

    public static final String PREF_KEY_INDEX = "PREF_KEY_INDEX";
    public static final String  PREF_KEY_COMMENT = "PREF_KEY_COMMENT";
    public static final String  PREF_KEY_DATE = "PREF_KEY_DATE";
    public static final String PREF_KEY_SAVED_MOOD = "PREF_KEY_SAVED_MOOD";
    public static final String LINK = "LINK";

    Calendar mCalendar = Calendar.getInstance();
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");



    String mCurrentDate = mSimpleDateFormat.format(mCalendar.getTime());
    String mDateSaved;

    public static final int MAX_DAY = 7;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent newIntent = new Intent(this , AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this , 0 , newIntent , 0);

        Calendar calendar =Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE ,40);
        calendar.set(Calendar.SECOND , 00);
        calendar.set(Calendar.MILLISECOND , 00);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);



        mGestureDetectorCompat = new GestureDetectorCompat(this , this);

        moodPreferences = getApplicationContext().getSharedPreferences(LINK, MODE_PRIVATE);




         if(moodPreferences.contains(PREF_KEY_INDEX)){
            mCurrentIndex = moodPreferences.getInt(PREF_KEY_INDEX , 0);
        }else {
            moodPreferences.edit().putInt(PREF_KEY_INDEX , 1).apply();
         }

        if (moodPreferences.contains(PREF_KEY_COMMENT)) {
            mComment = moodPreferences.getString(PREF_KEY_COMMENT, null);
        }else {
            moodPreferences.edit().putString(PREF_KEY_COMMENT , mComment).apply();
        }


        if (moodPreferences.contains(PREF_KEY_SAVED_MOOD )){

            Gson gson = new Gson();
            String json = moodPreferences.getString(PREF_KEY_SAVED_MOOD , null);
            Type type = new TypeToken<List<Mood>>(){}.getType();

            savedMoods = gson.fromJson(json , type);
        }



        mMoodBackGround = findViewById(R.id.mood_backGround);
        mMoodImage = findViewById(R.id.mood_img);
        mCommentButton = findViewById(R.id.comment_button);
        mHistoryButton = findViewById(R.id.history_button);

        mMoodArrayList.add(new Mood(getResources().getColor(R.color.banana_yellow), R.drawable.smiley_super_happy , YELLOW));
        mMoodArrayList.add(new Mood(getResources().getColor(R.color.light_sage), R.drawable.smiley_happy , GREEN));
        mMoodArrayList.add(new Mood(getResources().getColor(R.color.cornflower_blue_65), R.drawable.smiley_normal , BLUE));
        mMoodArrayList.add(new Mood(getResources().getColor(R.color.warm_grey), R.drawable.smiley_disappointed , GREY));
        mMoodArrayList.add(new Mood(getResources().getColor(R.color.faded_red), R.drawable.smiley_sad , RED));





        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


              Intent historyActivity = new Intent(MainActivity.this,HistoryActivity.class);
              startActivity(historyActivity);

            }
        });
        processCommentClick();

        if (moodPreferences.contains(PREF_KEY_DATE)) {

            mDateSaved = moodPreferences.getString(PREF_KEY_DATE, null);
        } else {

            moodPreferences.edit().putString(PREF_KEY_DATE, mCurrentDate).apply();
            mDateSaved = mCurrentDate;
        }

       // sendMoodToHistory();

        mMoodBackGround.setBackgroundColor(mMoodArrayList.get(mCurrentIndex).getMoodBackGroundColor());
        mMoodImage.setImageResource(mMoodArrayList.get(mCurrentIndex).getMoodImage());

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return this.mGestureDetectorCompat.onTouchEvent(event) || super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float deltaX = e2.getX() - e1.getX();
        float deltaY = e2.getY() - e1.getY();
        if (Math.abs(deltaX) >= Math.abs(deltaY)) {
            return false;
        } else {

            if (Math.abs(deltaY) >= DELTA_MIN) {
                if (deltaY < 0) {
                    if (mCurrentIndex < MOOD_LENGTH && mCurrentIndex >= 0) {
                        mCurrentIndex += 1;
                    }

                    switchMood();
                    return true;
                } else {
                    if (mCurrentIndex <= MOOD_LENGTH && mCurrentIndex > 0) {
                        mCurrentIndex -= 1;
                    }

                    switchMood();
                    return true;
                }
            }
        }



        return false;


    }



    public void sendMoodToHistory(Context context) {
        if (mDateSaved.equals(mCurrentDate)) {
            // If there is more than 7 days , it removes the last index to avoid the out of bounds
            if (savedMoods.size() >= MAX_DAY) {
                savedMoods.remove(savedMoods.size() - 1);
            }
            savedMoods.add(0, mMoodArrayList.get(mCurrentIndex));
            savedMoods.get(0).setMoodComment(mComment);

            Gson gson = new Gson();
            String json = gson.toJson(savedMoods);
            SharedPreferences.Editor editor = moodPreferences.edit();

            editor.putString(PREF_KEY_COMMENT, null);
            editor.putString(PREF_KEY_DATE, mCurrentDate);
            editor.remove(PREF_KEY_INDEX);

            editor.apply();

            moodPreferences.edit().putString(PREF_KEY_SAVED_MOOD, json).apply();


            mCurrentIndex = 1;
        }
    }

    private void switchMood(){
        mMoodImage.setImageResource(mMoodArrayList.get(mCurrentIndex).getMoodImage());
        mMoodBackGround.setBackgroundColor(mMoodArrayList.get(mCurrentIndex).getMoodBackGroundColor());

        moodPreferences.edit().putInt(PREF_KEY_INDEX , mCurrentIndex).apply();




    }
    private void processCommentClick(){
        mCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_comment , null);
                final TextView mCommentText = mView.findViewById(R.id.text_comment);
                final EditText mEditComment = mView.findViewById(R.id.edit_comment);
                final Button mOkButton = mView.findViewById(R.id.ok_button);
                final Button mCancelButton = mView.findViewById(R.id.cancel_button);
                mOkButton.setEnabled(false);
                mEditComment.setHorizontallyScrolling(false);
                if(moodPreferences.contains(PREF_KEY_COMMENT)){
                    mEditComment.setText(mComment);
                    mEditComment.setSelection(mEditComment.getText().length());
                }

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                mEditComment.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        mOkButton.setEnabled(mEditComment.getText().length() != 0);

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


                mOkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mComment = mEditComment.getText().toString();
                        moodPreferences.edit().putString(PREF_KEY_COMMENT , mComment).apply();
                        dialog.cancel();
                    }
                });
                mCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mEditComment.getText().length() < 1){
                            mComment = null;
                        }
                        dialog.cancel();
                    }
                });

            }
        });

    }
}
