package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcerciseQuestion extends Excercise {
    private int eQID;
    private Question question;
    private List<Answer> answer;
    private int orderIndex;
    private String description;

    public ExcerciseQuestion(int eQID, Question question, List<Answer> answer, String description, int orderIndex) {
        this.eQID = eQID;
        this.question = question;
        this.answer = answer;
        this.description = description;
        this.orderIndex = orderIndex;
    }

    public ExcerciseQuestion(int excerciseID, String exerciseTitle, String excerciseImage, Date dateCreated, Date dateEnd, int eQID, Question question, List<Answer> answer, String description, int orderIndex) {
        super(excerciseID, exerciseTitle, excerciseImage, dateCreated, dateEnd);
        this.eQID = eQID;
        this.question = question;
        this.answer = answer;
        this.description = description;
        this.orderIndex = orderIndex;
    }

    public List<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Answer> answer) {
        this.answer = answer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int geteQID() {
        return eQID;
    }

    public void seteQID(int eQID) {
        this.eQID = eQID;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void displayQuestion() {
        System.out.println("Excercise Question ID: " + eQID);
        System.out.println("-----------------------------------------------------------");
        System.out.println("Exercise ID: " + getExcerciseID());
        System.out.println("Exercise Title: " + getExerciseTitle());
        System.out.println("Exercise Image: " + getExcerciseImage());
        System.out.println("Date Created: " + getDateCreated());
        System.out.println("Question Order Index: " + getOrderIndex());
        System.out.println("-----------------------------------------------------------");
        System.out.println("Question ID: " + getQuestion().getQuestionID());
        System.out.println("Question Description: " + getDescription());
        System.out.println("Question Type: " + getQuestion().getQuestionType());
        System.out.println("Question Text: " + getQuestion().getQuestionText());
        System.out.println("-----------------------------------------------------------");
        for (Answer answer : getAnswer()) {
            System.out.println("AnswerID: " + answer.getAnswerID() + "-" + "Option " + answer.getOptionLabel() + ": " + answer.getAnswerText() + (answer.isCorrect() ? " (Correct)" : ""));
        }

        System.out.println("-----------------------------------------------------------");
    }

//    public static void main(String[] args) {
//        Question question = new Question();
//        question.setQuestionID(1);
//        question.setQuestionType("Multiple Choice");
//        question.setQuestionText("What is the capital of France?");
//        Answer answerA = new Answer(1, "Paris", true, 'A');// Đáp án đúng
//        Answer answerB = new Answer(2, "London", false, 'B');
//        Answer answerC = new Answer(3, "Berlin", false, 'C');
//        Answer answerD = new Answer(4, "Seoul", false, 'D');
//        List<Answer> answers = new ArrayList<Answer>();
//        answers.add(answerA);
//        answers.add(answerB);
//        answers.add(answerC);
//        answers.add(answerD);
//
//
//        ExcerciseQuestion excerciseQuestion = new ExcerciseQuestion(1, question, answers, "This exercise is about the capital of France.", 1);
//        excerciseQuestion.setExcerciseID(1);
//        excerciseQuestion.setExerciseTitle("Capital of France");
//        excerciseQuestion.setExcerciseImage("image.jpg");
//        excerciseQuestion.setDateCreated(new Date());
//        excerciseQuestion.displayQuestion();
//    }
}
