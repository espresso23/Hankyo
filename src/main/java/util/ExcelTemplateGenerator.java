package util;

import model.Answer;
import model.Question;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelTemplateGenerator {
    
    public static List<Question> readQuestionsFromExcel(InputStream inputStream, String fileName) throws IOException {
        List<Question> questions = new ArrayList<>();
        Workbook workbook = null;
        
        try {
            // Xác định loại file Excel dựa vào phần mở rộng
            if (fileName.toLowerCase().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else if (fileName.toLowerCase().endsWith(".xls")) {
                try {
                    workbook = new HSSFWorkbook(inputStream);
                } catch (NotOLE2FileException e) {
                    throw new IllegalArgumentException("File không đúng định dạng Excel XLS. Vui lòng kiểm tra lại file của bạn.");
                }
            } else if (fileName.toLowerCase().endsWith(".csv")) {
                throw new IllegalArgumentException("Định dạng CSV hiện chưa được hỗ trợ. Vui lòng sử dụng file Excel (.xlsx hoặc .xls)");
            } else {
                throw new IllegalArgumentException("Định dạng file không được hỗ trợ. Vui lòng sử dụng file Excel (.xlsx hoặc .xls)");
            }

            if (workbook.getNumberOfSheets() == 0) {
                throw new IllegalArgumentException("File Excel không có sheet nào");
            }
            
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet.getPhysicalNumberOfRows() <= 1) {
                throw new IllegalArgumentException("File Excel không có dữ liệu (chỉ có header hoặc trống)");
            }
            
            // Bỏ qua dòng header
            boolean isFirstRow = true;
            
            for (Row row : sheet) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }
                
                // Kiểm tra dòng trống
                if (isEmptyRow(row)) {
                    continue;
                }
                
                Question question = new Question();
                List<Answer> answers = new ArrayList<>();
                
                try {
                    // Đọc thông tin câu hỏi
                    String questionText = getCellValue(row.getCell(0));
                    if (questionText == null || questionText.trim().isEmpty()) {
                        throw new IllegalArgumentException("Nội dung câu hỏi không được để trống tại dòng " + (row.getRowNum() + 1));
                    }
                    question.setQuestionText(questionText);
                    
                    // Xử lý questionType
                    String questionType = getCellValue(row.getCell(1));
                    System.out.println("Debug - Dòng " + (row.getRowNum() + 1) + " - Question Type gốc: '" + questionType + "'");
                    
                    if (questionType != null) {
                        questionType = questionType.trim().toLowerCase();
                        System.out.println("Debug - Sau khi chuẩn hóa: '" + questionType + "'");
                    }
                    
                    if (questionType == null || (!questionType.equals("multiple_choice") && !questionType.equals("short_answer"))) {
                        throw new IllegalArgumentException("Loại câu hỏi không hợp lệ tại dòng " + (row.getRowNum() + 1) + 
                            ". Phải là 'multiple_choice' hoặc 'short_answer'. Giá trị hiện tại: '" + questionType + "'");
                    }
                    question.setQuestionType(questionType);
                    
                    try {
                        double questionMark = Double.parseDouble(getCellValue(row.getCell(2)));
                        if (questionMark <= 0) {
                            throw new IllegalArgumentException("Điểm số phải lớn hơn 0 tại dòng " + (row.getRowNum() + 1));
                        }
                        question.setQuestionMark(questionMark);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Điểm số không hợp lệ tại dòng " + (row.getRowNum() + 1));
                    }
                    
                    // Nếu là câu hỏi trắc nghiệm
                    if ("multiple_choice".equals(questionType)) {
                        String correctAnswer = getCellValue(row.getCell(7));
                        if (correctAnswer == null || !correctAnswer.matches("[A-D]")) {
                            throw new IllegalArgumentException("Đáp án đúng phải là A, B, C hoặc D tại dòng " + (row.getRowNum() + 1));
                        }
                        
                        // Đọc 4 lựa chọn
                        for (int i = 0; i < 4; i++) {
                            String answerText = getCellValue(row.getCell(3 + i));
                            if (answerText == null || answerText.trim().isEmpty()) {
                                throw new IllegalArgumentException("Lựa chọn " + (char)('A' + i) + " không được để trống tại dòng " + (row.getRowNum() + 1));
                            }
                            
                            Answer answer = new Answer();
                            answer.setAnswerText(answerText);
                            answer.setOptionLabel(String.valueOf((char)('A' + i)));
                            answer.setCorrect(answer.getOptionLabel().equals(correctAnswer));
                            answers.add(answer);
                        }
                    }
                    
                    question.setQuestionImage(getCellValue(row.getCell(8)));
                    question.setAudioFile(getCellValue(row.getCell(9)));
                    question.setAnswers(answers);
                    questions.add(question);
                    
                    // Kiểm tra số lượng câu hỏi
                    if (questions.size() > 100) {
                        throw new IllegalArgumentException("Số lượng câu hỏi không được vượt quá 100");
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Lỗi tại dòng " + (row.getRowNum() + 1) + ": " + e.getMessage());
                }
            }
            
            if (questions.isEmpty()) {
                throw new IllegalArgumentException("Không có câu hỏi nào được import");
            }
            
            return questions;
            
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }
    
    private static boolean isEmptyRow(Row row) {
        if (row == null) return true;
        for (int i = 0; i < 10; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
    
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Tránh hiển thị số dưới dạng khoa học (1.0E-4)
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
                return null;
            default:
                return null;
        }
    }
    
    public static void validateQuestions(List<Question> questions) throws IllegalArgumentException {
        // Kiểm tra số lượng câu hỏi
        if (questions.size() > 100) {
            throw new IllegalArgumentException("Số lượng câu hỏi không được vượt quá 100");
        }
        
        for (Question question : questions) {
            // Kiểm tra các trường bắt buộc
            if (question.getQuestionText() == null || question.getQuestionText().trim().isEmpty()) {
                throw new IllegalArgumentException("Nội dung câu hỏi không được để trống");
            }
            
            if (question.getQuestionType() == null || 
                (!question.getQuestionType().equals("multiple_choice") && 
                 !question.getQuestionType().equals("short_answer"))) {
                throw new IllegalArgumentException("Loại câu hỏi không hợp lệ");
            }
            
            if (question.getQuestionMark() <= 0) {
                throw new IllegalArgumentException("Điểm số phải là số dương");
            }
            
            // Kiểm tra câu hỏi trắc nghiệm
            if ("multiple_choice".equals(question.getQuestionType())) {
                List<Answer> answers = question.getAnswers();
                
                if (answers == null || answers.size() != 4) {
                    throw new IllegalArgumentException("Câu hỏi trắc nghiệm phải có đủ 4 lựa chọn");
                }
                
                boolean hasCorrectAnswer = false;
                for (Answer answer : answers) {
                    if (answer.isCorrect()) {
                        hasCorrectAnswer = true;
                        break;
                    }
                    
                    // Kiểm tra nội dung câu trả lời không được trống
                    if (answer.getAnswerText() == null || answer.getAnswerText().trim().isEmpty()) {
                        throw new IllegalArgumentException("Nội dung câu trả lời không được để trống");
                    }
                }
                
                if (!hasCorrectAnswer) {
                    throw new IllegalArgumentException("Câu hỏi trắc nghiệm phải có ít nhất 1 đáp án đúng");
                }
            }
        }
    }
} 