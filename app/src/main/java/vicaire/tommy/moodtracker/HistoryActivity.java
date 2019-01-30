package vicaire.tommy.moodtracker;


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static vicaire.tommy.moodtracker.MainActivity.LINK;
import static vicaire.tommy.moodtracker.MainActivity.MAXDAY;
import static vicaire.tommy.moodtracker.MainActivity.PREF_KEY_SAVED_MOOD;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<Mood> savedMood = new ArrayList<>();
    SharedPreferences moodPreferences;
    TextView emptyHistory;
    LinearLayout parentLL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        emptyHistory = findViewById(R.id.empty_history);
        parentLL = findViewById(R.id.parentLinearLayout);



        moodPreferences = getApplicationContext().getSharedPreferences(LINK , MODE_PRIVATE);

        if (moodPreferences.contains(PREF_KEY_SAVED_MOOD )){

            Gson gson = new Gson();
            String json = moodPreferences.getString(PREF_KEY_SAVED_MOOD , null);
            Type type = new TypeToken<List<Mood>>(){}.getType();

            savedMood = gson.fromJson(json , type);
        }

        // if SavedMood contains a mood the layout will disappear letting place to the history
        if (savedMood.size() > 0) {
            emptyHistory.setVisibility(View.GONE);
        }

        // Viewport width and height
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels / MAXDAY;

        // Yellow mood's width and height
        int yellowWidth = width;
        // Green mood's width and height
        int greenWidth = (int) (width / 1.2);
        // Blue mood's width and height
        int blueWidth = (int) (width / 1.5);
        // Grey mood's width and height
        int greyWidth = (int) (width / 2.2);
        // Red mood's width and height
        int redWidth = (int) (width / 3.3);


        // These parameters will describe each width depending on the mood
        LinearLayout.LayoutParams yellowLayoutParam = new LinearLayout.LayoutParams(yellowWidth, height);
        LinearLayout.LayoutParams greenLayoutParam = new LinearLayout.LayoutParams(greenWidth, height);
        LinearLayout.LayoutParams blueLayoutParam = new LinearLayout.LayoutParams(blueWidth, height);
        LinearLayout.LayoutParams greyLayoutParam = new LinearLayout.LayoutParams(greyWidth, height);
        LinearLayout.LayoutParams redLayoutParam = new LinearLayout.LayoutParams(redWidth, height);



        for(int i = savedMood.size() -1 ; i >= 0 ; i--){


            View mView = getLayoutInflater().inflate(R.layout.history_item , null);
            TextView textView = mView.findViewById(R.id.days_text);
            textView.setText(String.valueOf(i));


     mView.setBackgroundColor(savedMood.get(i).getMoodBackGroundColor());
     switch (savedMood.get(i).getLayoutColor()) {
         case YELLOW:
             mView.setLayoutParams(yellowLayoutParam);
             break;
         case GREEN:
             mView.setLayoutParams(greenLayoutParam);
             break;
         case BLUE:
             mView.setLayoutParams(blueLayoutParam);
             break;
         case GREY:
             mView.setLayoutParams(greyLayoutParam);
             break;
         case RED:
             mView.setLayoutParams(redLayoutParam);
             break;

     }
            ImageView commentImg = mView.findViewById(R.id.comment_img);
            if(savedMood.get(i).getMoodComment() != null){
                final int position = i;
                commentImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getBaseContext(), String.valueOf(savedMood.get(position).getMoodComment()), Toast.LENGTH_SHORT).show();
                    }
                });
                commentImg.setVisibility(View.VISIBLE);

            }

        parentLL.addView(mView);

        }

    }
}
