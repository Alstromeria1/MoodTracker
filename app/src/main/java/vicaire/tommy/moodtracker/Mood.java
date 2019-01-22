package vicaire.tommy.moodtracker;

public class Mood {
    int moodBackGroundColor;
    int moodImage;

    public Mood(int moodBackGroundColor, int moodImage) {
        this.moodBackGroundColor = moodBackGroundColor;
        this.moodImage = moodImage;
    }

    public int getMoodBackGroundColor() {
        return moodBackGroundColor;
    }

    public void setMoodBackGroundColor(int moodBackGroundColor) {
        this.moodBackGroundColor = moodBackGroundColor;
    }

    public int getMoodImage() {
        return moodImage;
    }

    public void setMoodImage(int moodImage) {
        this.moodImage = moodImage;
    }
}
