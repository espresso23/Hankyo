package model;


public class Question {
    private int questionID;
    private String questionText;
    private String questionImage;
    private String audioFile;
    private String questionType;

    public Question(int questionID, String questionText, String questionImage, String questionType, String audioFile) {
        this.questionID = questionID;
        this.questionText = questionText;
        this.questionImage = questionImage;
        this.questionType = questionType;
        this.audioFile = audioFile;
    }

    public Question() {
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    @Override
    public String toString() {
        return "Question{" +
                "audioFile='" + audioFile + '\'' +
                ", questionID=" + questionID +
                ", questionText='" + questionText + '\'' +
                ", questionImage='" + questionImage + '\'' +
                ", questionType='" + questionType + '\'' +
                '}';
    }
}
