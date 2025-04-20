package test;

import dao.AssignmentDAO;
import model.Assignment;
import model.Question;
import service.AssignmentService;

import java.sql.SQLException;
import java.util.List;

public class AssignmentServiceTest {
    public static void main(String[] args) {
        try {
            // Khởi tạo các service và dao cần thiết
            AssignmentService assignmentService = new AssignmentService();
            AssignmentDAO assignmentDAO = new AssignmentDAO();

            // Test với assignmentID = 1007
            int assignmentID = 1007;

            System.out.println("=== Bắt đầu test AssignmentService ===");

            // 1. Test lấy thông tin assignment
            System.out.println("\n1. Test lấy thông tin assignment:");
            Assignment assignment = assignmentDAO.getAssignmentById(assignmentID);
            if (assignment != null) {
                System.out.println("Thông tin assignment:");
                System.out.println("- ID: " + assignment.getAssignmentID());
                System.out.println("- Title: " + assignment.getAssignmentTitle());
                System.out.println("- Description: " + assignment.getDescription());
            } else {
                System.out.println("Không tìm thấy assignment với ID: " + assignmentID);
            }

            // 2. Test lấy danh sách câu hỏi
            System.out.println("\n2. Test lấy danh sách câu hỏi:");
            List<Question> questions = assignmentService.getAllQuestionOfAssignment(assignmentID);
            System.out.println("Số lượng câu hỏi: " + questions.size());
            for (Question question : questions) {
                System.out.println("\nThông tin câu hỏi:");
                System.out.println("- ID: " + question.getQuestionID());
                System.out.println("- Text: " + question.getQuestionText());
                System.out.println("- Type: " + question.getQuestionType());
                System.out.println("- Mark: " + question.getQuestionMark());
                if (question.getQuestionImage() != null) {
                    System.out.println("- Image URL: " + question.getQuestionImage());
                }
                if (question.getAudioFile() != null) {
                    System.out.println("- Audio URL: " + question.getAudioFile());
                }
            }

            // 3. Test thêm câu hỏi mới
            System.out.println("\n3. Test thêm câu hỏi mới:");
            Question newQuestion = new Question();
            newQuestion.setAssignmentID(assignmentID);
            newQuestion.setQuestionText("Câu hỏi test mới nhất");
            newQuestion.setQuestionType("multiple_choice");
            newQuestion.setQuestionMark(1.0);

            String[] answers = {"Câu trả lời 1", "Câu trả lời 2", "Câu trả lời 100"};
            String[] isCorrect = {"1", "0", "0"}; // Câu trả lời đầu tiên là đúng, các câu còn lại là sai
            String[] optionLabels = {"A", "B", "C"}; // Thêm option_label cho mỗi câu trả lời

            try {
                assignmentService.addQuestion(newQuestion, answers, isCorrect, optionLabels, assignmentID);
                System.out.println("Thêm câu hỏi thành công");
            } catch (Exception e) {
                System.out.println("Lỗi khi thêm câu hỏi: " + e.getMessage());
            }

            // 4. Test xóa câu hỏi
            System.out.println("\n4. Test xóa câu hỏi:");
            if (!questions.isEmpty()) {
                int questionIDToDelete = questions.get(0).getQuestionID();
                try {
                    assignmentService.deleteQuestion(questionIDToDelete);
                    System.out.println("Xóa câu hỏi ID " + questionIDToDelete + " thành công");
                } catch (Exception e) {
                    System.out.println("Lỗi khi xóa câu hỏi: " + e.getMessage());
                }
            }

            System.out.println("\n=== Kết thúc test AssignmentService ===");

        } catch (SQLException e) {
            System.out.println("Lỗi SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Lỗi không xác định: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 