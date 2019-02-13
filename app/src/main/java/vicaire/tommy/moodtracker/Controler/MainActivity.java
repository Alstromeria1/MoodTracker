package vicaire.tommy.moodtracker.Controler;

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

import vicaire.tommy.moodtracker.Model.Mood;
import vicaire.tommy.moodtracker.R;
import vicaire.tommy.moodtracker.Model.Utils;

import static vicaire.tommy.moodtracker.Model.Mood.LayoutColor.BLUE;
import static vicaire.tommy.moodtracker.Model.Mood.LayoutColor.GREEN;
import static vicaire.tommy.moodtracker.Model.Mood.LayoutColor.GREY;
import static vicaire.tommy.moodtracker.Model.Mood.LayoutColor.RED;
import static vicaire.tommy.moodtracker.Model.Mood.LayoutColor.YELLOW;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

   private RelativeLayout mMoodBackGround;
   private ImageView mMoodImage;
   private Button mCommentButton;
   private Button mHistoryButton;





   // Swipe
   public static final ArrayList<Mood> mMoodArrayList = new ArrayList<>();
   private int mCurrentIndex = 1;
   public static final int MAX_DAY = 7;

   GestureDetectorCompat mGestureDetectorCompat;


   // Comment
   String mComment;


   // SharedPreferences
   SharedPreferences  moodPreferences;
   private ArrayList<Mood> savedMoods = new ArrayList<>();
   // SharedPreferences keys
   public static final String PREF_KEY_INDEX = "PREF_KEY_INDEX";
   public static final String  PREF_KEY_COMMENT = "PREF_KEY_COMMENT";
   public static final String  PREF_KEY_DATE = "PREF_KEY_DATE";
   public static final String PREF_KEY_SAVED_MOOD = "PREF_KEY_SAVED_MOOD";
   public static final String PREF_KEY_MOOD_LIST = "PREF_KEY_MOOD_LIST";
   public static final String LINK = "LINK";

   // Date
   Calendar mCalendar = Calendar.getInstance();
   SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
   String mCurrentDate = mSimpleDateFormat.format(mCalendar.getTime());
   String mDateSaved;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            // Instantiate
        mGestureDetectorCompat = new GestureDetectorCompat(this , this);
        moodPreferences = getApplicationContext().getSharedPreferences(LINK, MODE_PRIVATE);
        mMoodBackGround = findViewById(R.id.mood_backGround);
        mMoodImage = findViewById(R.id.mood_img);
        mCommentButton = findViewById(R.id.comment_button);
        mHistoryButton = findViewById(R.id.history_button);
            // Filling mMoodArrayList with mood objects
        mMoodArrayList.add(new Mood(getResources().getColor(R.color.banana_yellow), R.drawable.smiley_super_happy , YELLOW));
        mMoodArrayList.add(new Mood(getResources().getColor(R.color.light_sage), R.drawable.smiley_happy , GREEN));
        mMoodArrayList.add(new Mood(getResources().getColor(R.color.cornflower_blue_65), R.drawable.smiley_normal , BLUE));
        mMoodArrayList.add(new Mood(getResources().getColor(R.color.warm_grey), R.drawable.smiley_disappointed , GREY));
        mMoodArrayList.add(new Mood(getResources().getColor(R.color.faded_red), R.drawable.smiley_sad , RED));
            // Called method
        Utils.setAlarm(this);
        processHistoryClick();
        processCommentClick();

        Gson mGson = new Gson();
        String mJson = mGson.toJson(mMoodArrayList);
        moodPreferences.edit().putString(PREF_KEY_MOOD_LIST , mJson).apply();


    }

    @Override
    public void onResume(){
        super.onResume();
        recoverPreferences();
        //Default Mood shown
        mMoodBackGround.setBackgroundColor(mMoodArrayList.get(mCurrentIndex).getMoodBackGroundColor());
        mMoodImage.setImageResource(mMoodArrayList.get(mCurrentIndex).getMoodImage());


    }



    // Recovering everything from the SharedPreferences
    private void recoverPreferences(){

            // if the SharedPreferences contains the index of mMoodArrayList (used to know which mood needs to be shown) : get it back
            // else initialize it
        if(moodPreferences.contains(PREF_KEY_INDEX)){
            mCurrentIndex = moodPreferences.getInt(PREF_KEY_INDEX , 0);
        }else {
            moodPreferences.edit().putInt(PREF_KEY_INDEX , 1).apply();
        }
            // if the SharedPreferences contains a comment : get it back
            // else initialize it
        if (moodPreferences.contains(PREF_KEY_COMMENT)) {
            mComment = moodPreferences.getString(PREF_KEY_COMMENT, null);
        }else {
            moodPreferences.edit().putString(PREF_KEY_COMMENT , mComment).apply();
        }
            // if the SharedPreferences contains the SavedMood (used in history) : get it back from json objects
        if (moodPreferences.contains(PREF_KEY_SAVED_MOOD )){
            Gson gson = new Gson();
            String json = moodPreferences.getString(PREF_KEY_SAVED_MOOD , null);
            Type type = new TypeToken<List<Mood>>(){}.getType();
            savedMoods = gson.fromJson(json , type);
        }
            // if the SharedPreferences contains the date : get it back
            // else initialize it
        if (moodPreferences.contains(PREF_KEY_DATE)) {
            mDateSaved = moodPreferences.getString(PREF_KEY_DATE, null);
        } else {
            moodPreferences.edit().putString(PREF_KEY_DATE, mCurrentDate).apply();
            mDateSaved = mCurrentDate;
        }


    }



    //  Updating SharedPreferences key and mood shown when swiped
    private void switchMood(){
        mMoodImage.setImageResource(mMoodArrayList.get(mCurrentIndex).getMoodImage());
        mMoodBackGround.setBackgroundColor(mMoodArrayList.get(mCurrentIndex).getMoodBackGroundColor());

        moodPreferences.edit().putInt(PREF_KEY_INDEX , mCurrentIndex).apply();
    }
    // Starting the second activity
    private void processHistoryClick(){
        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent historyActivity = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(historyActivity);
            }
        });

    }
    // Showing an Alert Dialog to write a comment
    private void processCommentClick(){
        mCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Instantiate
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_comment , null);
                final TextView mCommentText = mView.findViewById(R.id.text_comment);
                final EditText mEditComment = mView.findViewById(R.id.edit_comment);
                final Button mOkButton = mView.findViewById(R.id.ok_button);
                final Button mCancelButton = mView.findViewById(R.id.cancel_button);
                // Parameter
                mOkButton.setEnabled(false);
                mEditComment.setHorizontallyScrolling(false);

                // SharedPreferences
                if(moodPreferences.contains(PREF_KEY_COMMENT)){
                    mEditComment.setText(mComment);
                    mEditComment.setSelection(mEditComment.getText().length());
                }

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                mEditComment.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        mOkButton.setEnabled(mEditComment.getText().length() != 0);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) { }
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
    //
    // METHODS USED FOR EVENT HANDLING
    //
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

    // Swipe
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int DELTA_MIN = 50;
        int MOOD_LENGTH = 4;
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
}
