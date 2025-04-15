package main;

import dao.PaymentDAO;
import model.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // Tách logic tạo câu hỏi và câu trả lời thành method riêng
    private static List<Question> createSampleQuestions() {
        List<Question> questions = new ArrayList<>();
        
        Question question1 = new Question();
        question1.setQuestionText("What is the capital of France?");
        question1.setQuestionType("Multiple Choice");
        question1.setQuestionMark(0.25);
        
        Question question2 = new Question();
        question2.setQuestionText("Which planet is known as the Red Planet?");
        question2.setQuestionType("Multiple Choice");
        question2.setQuestionMark(0.25);
        
        questions.add(question1);
        questions.add(question2);
        
        return questions;
    }

    private static List<List<Answer>> createSampleAnswers() {
        List<List<Answer>> answersForQuestions = new ArrayList<>();
        
        // Answers for question 1
        List<Answer> answers1 = new ArrayList<>();
        answers1.add(new Answer("Paris", true, 'A'));
        answers1.add(new Answer("London", false, 'B'));
        answers1.add(new Answer("Rome", false, 'C'));
        answers1.add(new Answer("Berlin", false, 'D'));
        
        // Answers for question 2
        List<Answer> answers2 = new ArrayList<>();
        answers2.add(new Answer("Mars", true, 'A'));
        answers2.add(new Answer("Venus", false, 'B'));
        answers2.add(new Answer("Jupiter", false, 'C'));
        answers2.add(new Answer("Saturn", false, 'D'));
        
        answersForQuestions.add(answers1);
        answersForQuestions.add(answers2);
        
        return answersForQuestions;
    }

//    public static void main(String[] args) {
//        try (Connection connection = DBConnect.getInstance().getConnection()) {
//            ExamDAO examDAO = new ExamDAO(connection);
//
//            // Create an empty Exam
//            Exam exam = new Exam();
//            exam.setExamName("Sample Exam");
//            exam.setExamDescription("This is a description for the sample exam.");
//            exam.setDateCreated(new Date());
//            exam.setExpertID(2); // Nên lấy từ config hoặc parameter
//
//            int examID = examDAO.createEmptyExam(exam);
//            System.out.println("Created empty Exam with ID: " + examID);
//
//            // Sử dụng các method đã tách
//            List<Question> questions = createSampleQuestions();
//            List<List<Answer>> answersForQuestions = createSampleAnswers();
//
//            // Add questions and answers to the Exam
//            examDAO.addQuestionsToExam(examID, exam, questions, answersForQuestions);
//            System.out.println("Added questions and answers to Exam with ID: " + examID);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // Nên log error thay vì chỉ print stack trace
//        }
//    }
public static void main(String[] args) {
        // Tạo Learner test
        Learner learner = new Learner();
        learner.setLearnerID(1); // Giả sử learnerID = 1 đã tồn tại trong DB
    int learnerID = 1;
        // Tạo danh sách CoursePaid
        List<CoursePaid> coursePaidList = new ArrayList<>();

        // Giả sử có 2 courseID đã tồn tại trong bảng Course
        int[] courseIDs = {8, 11,12}; // thay bằng ID có sẵn trong DB

        for (int courseID : courseIDs) {
            Course course = new Course();
            course.setCourseID(courseID);

            CoursePaid cp = new CoursePaid();
            cp.setCourseID(courseID);
            cp.setLearnerID(learnerID);

            coursePaidList.add(cp);
        }

        // Tạo Payment
        Payment payment = new Payment();
        payment.setTotalAmount(BigDecimal.valueOf(500000)); // ví dụ thanh toán 400k
        payment.setPayDate(LocalDateTime.now());
        payment.setDescription("Test mua 3 khóa học");
    payment.setLearnerID(learnerID);

        // Gọi hàm xử lý
        PaymentDAO dao = new PaymentDAO(); // class chứa hàm addPaymentWithCourses
        boolean result = dao.addPaymentWithCourses(payment, coursePaidList);

        // In kết quả
        if (result) {
            System.out.println("✅ Thanh toán và cập nhật doanh thu thành công.");
        } else {
            System.out.println("❌ Có lỗi xảy ra khi thanh toán.");
        }
    }

//    public static void main(String[] args) {
//        try {
//            URL url = new URL("https://api.payos.vn/v2/banks");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Accept", "application/json");
//
//            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
//            String output;
//            StringBuilder json = new StringBuilder();
//
//            while ((output = br.readLine()) != null) {
//                json.append(output);
//            }
//            conn.disconnect();
//            System.out.println("Danh sách ngân hàng: " + json.toString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}