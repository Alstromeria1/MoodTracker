package vicaire.tommy.moodtracker;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

   private RelativeLayout mMoodBackGround;
   private ImageView mMoodImage;
   private Button mCommentButton;
   private Button mHistoryButton;
   private int mCurrentIndex = 1;
   private final int DELTA_MIN = 50;
   private final int MOOD_LENGTH = 4;
   GestureDetectorCompat mGestureDetectorCompat;


   private final ArrayList<Mood> mMoodArrayList = new ArrayList<>();







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGestureDetectorCompat = new GestureDetectorCompat(this , this);
        mMoodBackGround = findViewById(R.id.mood_backGround);
        mMoodImage = findViewById(R.id.mood_img);
        mCommentButton = findViewById(R.id.comment_button);
        mHistoryButton = findViewById(R.id.history_button);

        mMoodArrayList.add(new Mood(getResources().getColor(R.color.banana_yellow), R.drawable.smiley_super_happy)) ;
        mMoodArrayList.add(new Mood(getResources().getColor(R.color.light_sage), R.drawable.smiley_happy))      ;
        mMoodArrayList.add(new Mood(getResources().getColor(R.color.cornflower_blue_65), R.drawable.smiley_normal)) ;
        mMoodArrayList.add(new Mood(getResources().getColor(R.color.warm_grey), R.drawable.smiley_disappointed))    ;
        mMoodArrayList.add(new Mood(getResources().getColor(R.color.faded_red), R.drawable.smiley_sad))  ;


        mMoodBackGround.setBackgroundColor(mMoodArrayList.get(mCurrentIndex).getMoodBackGroundColor());

        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent historyActivity = new Intent(MainActivity.this,HistoryActivity.class);
                startActivity(historyActivity);

            }
        });

        mCommentButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_comment , null);
            TextView mCommentText = mView.findViewById(R.id.text_comment);
            EditText mEditComment = mView.findViewById(R.id.edit_comment);
            Button mOkButton = mView.findViewById(R.id.ok_button);
            Button mCancelButton = mView.findViewById(R.id.cancel_button);

            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();

            mOkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            mCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

        }
    });


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

    private void switchMood(){
        mMoodImage.setImageResource(mMoodArrayList.get(mCurrentIndex).getMoodImage());
        mMoodBackGround.setBackgroundColor(mMoodArrayList.get(mCurrentIndex).getMoodBackGroundColor());

    }
}
