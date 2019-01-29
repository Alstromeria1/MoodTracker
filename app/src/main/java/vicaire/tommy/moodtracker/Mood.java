package vicaire.tommy.moodtracker;

public class Mood {


    private final LayoutColor layoutWidth;

    int moodBackGroundColor;
    int moodImage;
    public enum LayoutColor {YELLOW , BLUE , RED , GREEN , GREY}


    public LayoutColor getLayoutColor() {
        return layoutWidth;
    }

    public Mood(int moodBackGroundColor, int moodImage , LayoutColor layoutWidth) {
        this.moodBackGroundColor = moodBackGroundColor;
        this.moodImage = moodImage;
        this.layoutWidth = layoutWidth;
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
