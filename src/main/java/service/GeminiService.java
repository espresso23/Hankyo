package service;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONArray;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GeminiService {
    private final String API_KEY;
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-2.0-flash:generateContent";

    public GeminiService() {
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Không thể tìm thấy file config.properties");
            }
            prop.load(input);
            
            API_KEY = prop.getProperty("google.api.key");
            if (API_KEY == null || API_KEY.isEmpty()) {
                throw new RuntimeException("API key không được cấu hình");
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi đọc file cấu hình", e);
        }
    }
    
    public String generateResponse(String prompt) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(GEMINI_API_URL + "?key=" + API_KEY);
            
            // Tạo request body đúng chuẩn Gemini 2.0 Flash
            JSONObject requestBody = new JSONObject();
            JSONArray contents = new JSONArray();
            JSONObject content = new JSONObject();
            content.put("role", "user");
            JSONArray parts = new JSONArray();
            parts.put(new JSONObject().put("text", prompt));
            content.put("parts", parts);
            contents.put(content);
            requestBody.put("contents", contents);

            StringEntity entity = new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity responseEntity = response.getEntity();
                String result = EntityUtils.toString(responseEntity, "UTF-8");
                System.out.println("Gemini API response: " + result);
                
                JSONObject jsonResponse = new JSONObject(result);
                if (jsonResponse.has("candidates")) {
                    JSONArray candidates = jsonResponse.getJSONArray("candidates");
                    if (candidates.length() > 0) {
                        JSONObject candidate = candidates.getJSONObject(0);
                        if (candidate.has("content")) {
                            JSONObject candidateContent = candidate.getJSONObject("content");
                            if (candidateContent.has("parts")) {
                                JSONArray candidateParts = candidateContent.getJSONArray("parts");
                                if (candidateParts.length() > 0) {
                                    JSONObject part = candidateParts.getJSONObject(0);
                                    if (part.has("text")) {
                                        return part.getString("text");
                                    }
                                }
                            }
                        }
                    }
                }
                return "Xin lỗi, tôi không thể tạo phản hồi lúc này.";
            }
        }
    }
    
    // Phương thức hỗ trợ cho việc tạo câu hỏi tiếng Hàn
    public String generateKoreanQuestion(String topic, String level) throws IOException {
        String prompt = String.format("Tạo một câu hỏi tiếng Hàn về chủ đề '%s' ở trình độ '%s'. " +
                "Câu hỏi phải có 4 đáp án và chỉ một đáp án đúng.", topic, level);
        return generateResponse(prompt);
    }
    
    // Phương thức hỗ trợ cho việc giải thích ngữ pháp
    public String explainGrammar(String grammarPoint) throws IOException {
        String prompt = String.format("Giải thích chi tiết về ngữ pháp tiếng Hàn '%s'. " +
                "Bao gồm cấu trúc, cách sử dụng và ví dụ.", grammarPoint);
        return generateResponse(prompt);
    }
    
    // Phương thức hỗ trợ cho việc dịch câu
    public String translateSentence(String sentence, String fromLang, String toLang) throws IOException {
        String prompt = String.format("Dịch câu sau từ %s sang %s: '%s'", fromLang, toLang, sentence);
        return generateResponse(prompt);
    }

    /**
     * Phân tích bài tập và đưa ra gợi ý giải
     * @param exerciseContent Nội dung bài tập
     * @return Gợi ý giải bài tập
     */
    public String analyzeExercise(String exerciseContent) throws IOException {
        String prompt = "Hãy đọc bài tập sau và đưa ra gợi ý giải chi tiết:\n" + exerciseContent;
        return generateResponse(prompt);
    }

    /**
     * Tóm tắt nội dung từ một đoạn văn bản
     * @param content Nội dung cần tóm tắt
     * @return Nội dung đã được tóm tắt
     */
    public String summarizeContent(String content) throws IOException {
        String prompt = "Hãy tóm tắt nội dung chính từ đoạn văn sau:\n" + content;
        return generateResponse(prompt);
    }

    /**
     * Trả lời câu hỏi dựa trên nội dung cho trước
     * @param content Nội dung tham khảo
     * @param question Câu hỏi cần trả lời
     * @return Câu trả lời dựa trên nội dung
     */
    public String answerQuestionAboutContent(String content, String question) throws IOException {
        String prompt = "Dựa vào nội dung sau, hãy trả lời câu hỏi: " + question + "\n\n" +
                       "Nội dung tham khảo:\n" + content;
        return generateResponse(prompt);
    }

    /**
     * Phân tích và đưa ra gợi ý cho bài tập tiếng Hàn
     * @param exerciseContent Nội dung bài tập tiếng Hàn
     * @param exerciseType Loại bài tập (ngữ pháp, từ vựng, đọc hiểu...)
     * @return Gợi ý và giải thích chi tiết
     */
    public String analyzeKoreanExercise(String exerciseContent, String exerciseType) throws IOException {
        String prompt = "Hãy phân tích bài tập tiếng Hàn sau (loại: " + exerciseType + ") " +
                       "và đưa ra gợi ý giải chi tiết:\n" + exerciseContent;
        return generateResponse(prompt);
    }

    /**
     * Kiểm tra và sửa lỗi trong câu tiếng Hàn
     * @param sentence Câu tiếng Hàn cần kiểm tra
     * @return Phân tích lỗi và gợi ý sửa
     */
    public String checkKoreanSentence(String sentence) throws IOException {
        String prompt = "Hãy kiểm tra câu tiếng Hàn sau và chỉ ra lỗi (nếu có), " +
                       "đồng thời đưa ra cách sửa:\n" + sentence;
        return generateResponse(prompt);
    }
} 