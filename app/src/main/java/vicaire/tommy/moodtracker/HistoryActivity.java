package vicaire.tommy.moodtracker;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ShareActionProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static vicaire.tommy.moodtracker.MainActivity.LINK;
import static vicaire.tommy.moodtracker.MainActivity.PREF_KEY_SAVED_MOOD;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<Mood> savedMood = new ArrayList<>();
    SharedPreferences moodPreferences;

    Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        moodPreferences = getApplicationContext().getSharedPreferences(LINK , MODE_PRIVATE);

        if (moodPreferences.contains(PREF_KEY_SAVED_MOOD )){

            Gson gson = new Gson();
            String json = moodPreferences.getString(PREF_KEY_SAVED_MOOD , null);
            Type type = new TypeToken<List<Mood>>(){}.getType();

            savedMood = gson.fromJson(json , type);
        }



        test = findViewById(R.id.button);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0 ; i < savedMood.size(); i++){

                    System.out.println(savedMood.get(i));
                }


            }
        });




    }
}
