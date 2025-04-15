/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.PostDAO;
import dao.UserDAO;
import model.Post;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CommunityBlog", urlPatterns = {"/blog"})
public class CommunityBlogServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("blog.jsp").forward(request, response);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostDAO postDAO = new PostDAO();
        UserDAO userDAO = new UserDAO();
        List<Post> postList = null;
        Post newPost = null;
        String fullNameNewPost = null;
        String avatarURL = null;
        String avatarURLNewPost = null;

        try {
            boolean isConnected = postDAO.checkConnection();
            if (isConnected) {
                postList = postDAO.getAllPostsHaveFullNameAndAvtImg();
                newPost = postDAO.getLatestPost();

                if (newPost != null) {
                    avatarURLNewPost = userDAO.getAvatarByUserId(newPost.getUserID());
                    fullNameNewPost = userDAO.getFullNameByUserId(newPost.getUserID());
                    avatarURL = userDAO.getAvatarByUserId(newPost.getUserID());
                    newPost.setAvtUserImg(avatarURL);  // Set the avatar URL in the Post object
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("postList", postList);
        request.setAttribute("newPost", newPost);
        request.setAttribute("fullNameNewPost", fullNameNewPost);
        request.setAttribute("avatarURLNewPost", avatarURLNewPost);

        System.out.println(avatarURL);
        request.setAttribute("user", request.getSession().getAttribute("user"));

        RequestDispatcher dispatcher = request.getRequestDispatcher("blog.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
