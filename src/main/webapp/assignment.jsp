<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bài tập</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <link href="asset/css/assignment.css" rel="stylesheet">
    </head>
    <body>
        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="card glass-card">
                        <div class="card-header bg-primary text-white">
                            <h4 class="mb-0">${assignment.title}</h4>
                        </div>
                        <div class="card-body">
                            <c:if test="${not empty error}">
                                <div class="alert alert-danger">${error}</div>
                            </c:if>
                            
                            <form action="assignment" method="POST">
                                <input type="hidden" name="assignmentID" value="${assignment.assignmentID}">
                                <input type="hidden" name="contentID" value="${param.contentID}">
                                <input type="hidden" name="questionCount" value="${assignment.questions.size()}">
                                
                                <div class="mb-4">
                                    <p class="text-muted">${assignment.description}</p>
                                </div>
                                
                                <c:forEach var="question" items="${assignment.questions}" varStatus="status">
                                    <div class="question-card mb-4">
                                        <h5 class="question-title">Câu ${status.index + 1}: ${question.questionText}</h5>
                                        <div class="options">
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="answer${status.index + 1}" 
                                                       id="option1_${status.index + 1}" value="A" required>
                                                <label class="form-check-label" for="option1_${status.index + 1}">
                                                    A. ${question.optionA}
                                                </label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="answer${status.index + 1}" 
                                                       id="option2_${status.index + 1}" value="B" required>
                                                <label class="form-check-label" for="option2_${status.index + 1}">
                                                    B. ${question.optionB}
                                                </label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="answer${status.index + 1}" 
                                                       id="option3_${status.index + 1}" value="C" required>
                                                <label class="form-check-label" for="option3_${status.index + 1}">
                                                    C. ${question.optionC}
                                                </label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="answer${status.index + 1}" 
                                                       id="option4_${status.index + 1}" value="D" required>
                                                <label class="form-check-label" for="option4_${status.index + 1}">
                                                    D. ${question.optionD}
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                                
                                <div class="d-flex justify-content-between mt-4">
                                    <a href="learn-course.jsp?contentID=${param.contentID}" class="btn btn-secondary">
                                        <i class="fas fa-arrow-left me-2"></i>Quay lại
                                    </a>
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fas fa-paper-plane me-2"></i>Nộp bài
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 