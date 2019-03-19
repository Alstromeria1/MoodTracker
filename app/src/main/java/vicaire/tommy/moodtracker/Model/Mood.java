package vicaire.tommy.moodtracker.Model;

public class Mood {


    private final LayoutColor layoutWidth;
    private String moodComment;
    private int moodBackGroundColor;
    private int moodImage;
    private int moodAudio;
    public enum LayoutColor {YELLOW , BLUE , RED , GREEN , GREY}


    public LayoutColor getLayoutColor() {
        return layoutWidth;
    }

    public Mood(int moodBackGroundColor, int moodImage , LayoutColor layoutWidth , int moodAudio) {
        this.moodBackGroundColor = moodBackGroundColor;
        this.moodImage = moodImage;
        this.layoutWidth = layoutWidth;
        this.moodAudio = moodAudio;
    }

    public int getMoodAudio(){ return moodAudio;}

    public String getMoodComment() {
        return moodComment;
    }

    public void setMoodComment(String moodComment) {
        this.moodComment = moodComment;
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
