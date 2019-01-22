package vicaire.tommy.moodtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    RelativeLayout mMoodBackGround;
    ImageView mMoodImage;
    Button mCommentButton;
    Button mHistoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoodBackGround = findViewById(R.id.mood_backGround);
        mMoodImage = findViewById(R.id.mood_img);




    }
}
