package controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NewspapersCrawlerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = request.getParameter("url"); // URL trang báo cần crawl

        try {
            // Kết nối và lấy HTML
            Document doc = Jsoup.connect(url).get();

            // Ví dụ: Lấy các tiêu đề bài báo (tùy thuộc cấu trúc HTML trang đích)
            Elements newsHeadlines = doc.select("h2.title a"); // Thay đổi selector phù hợp

            // Lưu kết quả vào request attribute
            request.setAttribute("headlines", newsHeadlines);

            // Forward sang JSP hiển thị
            RequestDispatcher dispatcher = request.getRequestDispatcher("/result.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Lỗi khi crawl dữ liệu: " + e.getMessage());
        }
    }
}