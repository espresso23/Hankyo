package java.model;


import java.util.Date;

public class Excercise {
    private int excerciseID;
    private String exerciseTitle;
    private String excerciseImage;
    private Date dateCreated;
    private Date dateEnd;

    public Excercise() {
    }

    public Excercise(int excerciseID, String exerciseTitle, String excerciseImage, Date dateCreated, Date dateEnd) {
        this.excerciseID = excerciseID;
        this.exerciseTitle = exerciseTitle;
        this.excerciseImage = excerciseImage;
        this.dateCreated = dateCreated;
        this.dateEnd = dateEnd;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getExcerciseID() {
        return excerciseID;
    }

    public void setExcerciseID(int excerciseID) {
        this.excerciseID = excerciseID;
    }

    public String getExcerciseImage() {
        return excerciseImage;
    }

    public void setExcerciseImage(String excerciseImage) {
        this.excerciseImage = excerciseImage;
    }

    public String getExerciseTitle() {
        return exerciseTitle;
    }

    public void setExerciseTitle(String exerciseTitle) {
        this.exerciseTitle = exerciseTitle;
    }

    @Override
    public String toString() {
        return "Excercise{" +
                "dateCreated=" + dateCreated +
                ", excerciseID=" + excerciseID +
                ", exerciseTitle='" + exerciseTitle + '\'' +
                ", excerciseImage='" + excerciseImage + '\'' +
                ", dateEnd=" + dateEnd +
                '}';
    }
}
