<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Làm bài tập - ${assignment.assignmentTitle}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .assignment-container {
            max-width: 900px;
            margin: 2rem auto;
            padding: 2rem;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .assignment-header {
            margin-bottom: 2rem;
            padding-bottom: 1rem;
            border-bottom: 1px solid #e9ecef;
        }

        .assignment-header h1 {
            color: #2c3e50;
            font-size: 1.8rem;
            margin-bottom: 1rem;
        }

        .assignment-description {
            color: #666;
            font-size: 1.1rem;
            line-height: 1.6;
        }

        .question-container {
            margin-bottom: 2rem;
            padding: 1.5rem;
            background: #f8f9fa;
            border-radius: 8px;
            border: 1px solid #dee2e6;
        }

        .question-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1rem;
            padding-bottom: 0.5rem;
            border-bottom: 1px solid #dee2e6;
        }

        .question-number {
            font-size: 1.1rem;
            font-weight: 600;
            color: #1a73e8;
        }

        .question-mark {
            background: #e8f0fe;
            color: #1967d2;
            padding: 0.3rem 0.8rem;
            border-radius: 20px;
            font-size: 0.9rem;
            font-weight: 500;
        }

        .question-text {
            font-size: 1.1rem;
            margin-bottom: 1.5rem;
            color: #202124;
            line-height: 1.5;
        }

        .question-media {
            margin-bottom: 1.5rem;
        }

        .question-image {
            max-width: 100%;
            border-radius: 8px;
            margin-bottom: 1rem;
        }

        .audio-container {
            background: #f1f3f4;
            padding: 1rem;
            border-radius: 8px;
            margin-bottom: 1rem;
        }

        .audio-player {
            width: 100%;
        }

        .options-container {
            background: white;
            border-radius: 8px;
            padding: 1rem;
        }

        .option-item {
            margin-bottom: 1rem;
            padding: 0.8rem;
            border: 1px solid #e0e0e0;
            border-radius: 6px;
            transition: all 0.2s ease;
        }

        .option-item:hover {
            background-color: #f8f9fa;
            border-color: #1a73e8;
        }

        .option-item:last-child {
            margin-bottom: 0;
        }

        .option-label {
            display: flex;
            align-items: center;
            margin: 0;
            cursor: pointer;
            width: 100%;
        }

        .option-input {
            margin-right: 1rem;
        }

        .option-text {
            flex: 1;
            color: #202124;
        }

        .submit-container {
            margin-top: 2rem;
            text-align: center;
        }

        .submit-btn {
            background: #1a73e8;
            color: white;
            border: none;
            padding: 0.8rem 2rem;
            font-size: 1.1rem;
            border-radius: 8px;
            transition: all 0.3s ease;
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
        }

        .submit-btn:hover {
            background: #1557b0;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(26, 115, 232, 0.3);
        }

        @media (max-width: 768px) {
            .assignment-container {
                margin: 1rem;
                padding: 1rem;
            }
        }
    </style>
</head>
<body>
    <div class="assignment-container">
        <div class="assignment-header">
            <h1>${assignment.assignmentTitle}</h1>
            <p class="assignment-description">${assignment.description}</p>
        </div>

        <form action="submit-assignment" method="post" id="assignmentForm">
            <input type="hidden" name="assignmentID" value="${assignment.assignmentID}">
            <input type="hidden" name="assignTakenID" value="${taken.assignTakenID}">
            
            <c:forEach items="${assignment.assignmentQuestions}" var="question" varStatus="loop">
                <input type="hidden" name="assignQuesID" value="${question.assignQuesID}">
                <div class="question-container">
                    <div class="question-header">
                        <span class="question-number">Câu ${loop.index + 1}</span>
                        <span class="question-mark">${question.questionMark} điểm</span>
                    </div>

                    <div class="question-text">
                        ${question.questionText}
                    </div>

                    <c:if test="${not empty question.questionImg || not empty question.audioFile}">
                        <div class="question-media">
                            <c:if test="${not empty question.questionImg}">
                                <img src="${question.questionImg}" alt="Question Image" class="question-image">
                            </c:if>
                            
                            <c:if test="${not empty question.audioFile}">
                                <div class="audio-container">
                                    <audio controls class="audio-player">
                                        <source src="${question.audioFile}" type="audio/mpeg">
                                        Trình duyệt của bạn không hỗ trợ phát audio.
                                    </audio>
                                </div>
                            </c:if>
                        </div>
                    </c:if>

                    <div class="options-container">
                        <c:choose>
                            <c:when test="${question.questionType eq 'multiple_choice'}">
                                <c:forEach items="${question.answers}" var="answer">
                                    <div class="option-item">
                                        <label class="option-label">
                                            <input type="radio" 
                                                   name="answer_${question.questionID}" 
                                                   value="${answer.optionLabel}"
                                                   class="option-input" 
                                                   >
                                            <span class="option-text">
                                                ${answer.optionLabel}. ${answer.answerText}
                                            </span>
                                        </label>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="form-group">
                                    <textarea name="answer_${question.questionID}" 
                                              class="form-control" 
                                              rows="4" 
                                              placeholder="Nhập câu trả lời của bạn"
                                              ></textarea>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>

            <div class="submit-container">
                <div class="alert alert-warning mb-3" id="warningAlert" style="display: none;">
                    <i class="fas fa-exclamation-triangle me-2"></i>
                    <span id="warningMessage">Bạn chưa trả lời bất kỳ câu hỏi nào. Bạn có chắc chắn muốn nộp bài không?</span>
                </div>
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <div class="progress-info">
                        <span class="me-3">Đã trả lời: <span id="answeredCount">0</span>/<span id="totalQuestions">${assignment.assignmentQuestions.size()}</span></span>
                        <span>Chưa trả lời: <span id="unansweredCount">${assignment.assignmentQuestions.size()}</span></span>
                    </div>
                    <button type="submit" class="submit-btn" id="submitBtn">
                        <i class="fas fa-paper-plane"></i>
                        Nộp bài
                    </button>
                </div>
            </div>
        </form>
    </div>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        $(document).ready(function() {
            let answeredQuestions = 0;
            const totalQuestions = ${assignment.assignmentQuestions.size()};
            const unansweredQuestions = totalQuestions;

            // Cập nhật số câu đã trả lời và chưa trả lời
            function updateQuestionCounts() {
                $('#answeredCount').text(answeredQuestions);
                $('#unansweredCount').text(totalQuestions - answeredQuestions);
            }

            // Kiểm tra khi người dùng thay đổi câu trả lời
            $('input[type="radio"], textarea').on('change', function() {
                const questionContainer = $(this).closest('.question-container');
                const questionId = questionContainer.find('.question-number').text();
                const isAnswered = questionContainer.find('input[type="radio"]:checked, textarea').length > 0;
                
                if (isAnswered) {
                    answeredQuestions++;
                } else {
                    answeredQuestions--;
                }

                updateQuestionCounts();
                updateSubmitButton();
            });

            function updateSubmitButton() {
                if (answeredQuestions === 0) {
                    $('#warningAlert').show();
                    $('#warningMessage').text('Bạn chưa trả lời bất kỳ câu hỏi nào. Bạn có chắc chắn muốn nộp bài không?');
                } else if (answeredQuestions < totalQuestions) {
                    $('#warningAlert').show();
                    $('#warningMessage').text(`Bạn mới trả lời ${answeredQuestions}/${totalQuestions} câu hỏi. Bạn có chắc chắn muốn nộp bài không?`);
                } else {
                    $('#warningAlert').hide();
                }
            }

            // Kiểm tra ban đầu
            $('.question-container').each(function() {
                if ($(this).find('input[type="radio"]:checked, textarea').length > 0) {
                    answeredQuestions++;
                }
            });
            updateQuestionCounts();
            updateSubmitButton();

            // Xử lý khi submit form
            $('#assignmentForm').on('submit', function(e) {
                if (answeredQuestions === 0) {
                    e.preventDefault();
                    alert('Vui lòng trả lời ít nhất một câu hỏi trước khi nộp bài.');
                    return false;
                }
                
                if (answeredQuestions < totalQuestions) {
                    return confirm(`Bạn mới trả lời ${answeredQuestions}/${totalQuestions} câu hỏi. Bạn có chắc chắn muốn nộp bài không?`);
                }
            });

            // Lưu trạng thái câu hỏi vào localStorage
            function saveQuestionState(questionId, state) {
                const states = JSON.parse(localStorage.getItem('questionStates') || '{}');
                states[questionId] = state;
                localStorage.setItem('questionStates', JSON.stringify(states));
            }

            // Khôi phục trạng thái câu hỏi từ localStorage
            function restoreQuestionStates() {
                const states = JSON.parse(localStorage.getItem('questionStates') || '{}');
                Object.entries(states).forEach(([questionId, state]) => {
                    const questionContainer = $(`.question-container[data-question-id="${questionId}"]`);
                    if (state.answer) {
                        if (state.type === 'radio') {
                            questionContainer.find(`input[type="radio"][value="${state.answer}"]`).prop('checked', true);
                        } else {
                            questionContainer.find('textarea').val(state.answer);
                        }
                    }
                });
                updateQuestionCounts();
                updateSubmitButton();
            }

            // Lưu trạng thái khi người dùng thay đổi câu trả lời
            $('input[type="radio"], textarea').on('change', function() {
                const questionContainer = $(this).closest('.question-container');
                const questionId = questionContainer.data('question-id');
                const answer = $(this).val();
                const type = $(this).attr('type') || 'textarea';
                
                saveQuestionState(questionId, {
                    answer: answer,
                    type: type
                });
            });

            // Khôi phục trạng thái khi trang được tải
            restoreQuestionStates();
        });
    </script>
</body>
</html> 