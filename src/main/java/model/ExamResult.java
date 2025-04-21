package model;

import java.time.LocalDateTime;


public class ExamResult {
    private Exam exam;
    private Question question;
    private int resultID;
    private ExamQuestion examQuestion;
    private int eQuesID;
    private int examTakenID;
    private Learner learner;
    private LocalDateTime dateTaken;
    private float mark;
    private String answerLabel;
    private boolean answerIsCorrect;

    public ExamResult() {
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public int getResultID() {
        return resultID;
    }

    public void setResultID(int resultID) {
        this.resultID = resultID;
    }

    public ExamQuestion getExamQuestion() {
        return examQuestion;
    }

    public void setExamQuestion(ExamQuestion examQuestion) {
        this.examQuestion = examQuestion;
    }

    public int getExamTakenID() {
        return examTakenID;
    }

    public void setExamTakenID(int examTakenID) {
        this.examTakenID = examTakenID;
    }

    public Learner getLearner() {
        return learner;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }

    public LocalDateTime getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(LocalDateTime dateTaken) {
        this.dateTaken = dateTaken;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public String getAnswerLabel() {
        return answerLabel;
    }

    public void setAnswerLabel(String answerLabel) {
        this.answerLabel = answerLabel;
    }

    public boolean isAnswerIsCorrect() {
        return answerIsCorrect;
    }

    public void setAnswerIsCorrect(boolean answerIsCorrect) {
        this.answerIsCorrect = answerIsCorrect;
    }

    public int geteQuesID() {
        return eQuesID;
    }

    public void seteQuesID(int eQuesID) {
        this.eQuesID = eQuesID;
    }

    @Override
    public String toString() {
        return "ExamResult{" +
                "exam=" + exam +
                ", question=" + question +
                ", resultID=" + resultID +
                ", examQuestion=" + examQuestion +
                ", examTakenID=" + examTakenID +
                ", learner=" + learner +
                ", dateTaken=" + dateTaken +
                ", mark=" + mark +
                ", answerLabel='" + answerLabel + '\'' +
                ", answerIsCorrect=" + answerIsCorrect +
                '}';
    }
}