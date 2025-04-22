<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Làm bài tập - <c:out value="${assignment.assignmentTitle}"/></title>
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
            <h1><c:out value="${assignment.assignmentTitle}"/></h1>
            <p class="assignment-description"><c:out value="${assignment.description}"/></p>
        </div>

        <form action="submit-assignment" method="post" id="assignmentForm">
            <input type="hidden" name="assignmentID" value="${assignment.assignmentID}">
            <input type="hidden" name="assignTakenID" value="${taken.assignTakenID}">
            
            <c:forEach items="${assignment.assignmentQuestions}" var="question" varStatus="loop">
                <c:set var="questionId" value="${question.assignQuesID}"/>
                <c:set var="questionMark" value="${question.questionMark}"/>
                
                <div class="question-container" data-question-id="<c:out value="${questionId}"/>">
                    <input type="hidden" name="assignQuesID" value="<c:out value="${questionId}"/>">
                    <input type="hidden" name="answerLabel_<c:out value="${questionId}"/>" 
                           id="answerLabel_<c:out value="${questionId}"/>">
                    <input type="hidden" name="isCorrect_<c:out value="${questionId}"/>" 
                           id="isCorrect_<c:out value="${questionId}"/>">
                    <input type="hidden" name="mark_<c:out value="${questionId}"/>" 
                           id="mark_<c:out value="${questionId}"/>" 
                           value="<c:out value="${questionMark}"/>">

                    <div class="question-header">
                        <span class="question-number">Câu <c:out value="${loop.index + 1}"/></span>
                        <span class="question-mark"><c:out value="${questionMark}"/> điểm</span>
                    </div>

                    <div class="question-text">
                        <c:out value="${question.questionText}"/>
                    </div>

                    <c:if test="${not empty question.questionImg || not empty question.audioFile}">
                        <div class="question-media">
                            <c:if test="${not empty question.questionImg}">
                                <img src="<c:out value="${question.questionImg}"/>" alt="Question Image" class="question-image">
                            </c:if>
                            
                            <c:if test="${not empty question.audioFile}">
                                <div class="audio-container">
                                    <audio controls class="audio-player">
                                        <source src="<c:out value="${question.audioFile}"/>" type="audio/mpeg">
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
                                    <c:set var="optionLabel" value="${answer.optionLabel}"/>
                                    <c:set var="answerText" value="${answer.answerText}"/>
                                    <c:set var="isCorrect" value="${answer.correct}"/>
                                    
                                    <div class="option-item">
                                        <label class="option-label">
                                            <input type="radio" 
                                                   name="answer_<c:out value="${question.questionID}"/>" 
                                                   value="<c:out value="${optionLabel}"/>"
                                                   class="option-input"
                                                   data-is-correct="<c:out value="${isCorrect}"/>"
                                                   data-question-mark="<c:out value="${questionMark}"/>"
                                                   >
                                            <span class="option-text">
                                                <c:out value="${optionLabel}"/>. <c:out value="${answerText}"/>
                                            </span>
                                        </label>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="form-group">
                                    <textarea name="answer_<c:out value="${question.questionID}"/>" 
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
                        <span class="me-3">Đã trả lời: <span id="answeredCount">0</span>/<span id="totalQuestions"><c:out value="${assignment.assignmentQuestions.size()}"/></span></span>
                        <span>Chưa trả lời: <span id="unansweredCount"><c:out value="${assignment.assignmentQuestions.size()}"/></span></span>
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
            const totalQuestions = <c:out value="${assignment.assignmentQuestions.size()}"/>;
            const unansweredQuestions = totalQuestions;

            // Cập nhật số câu đã trả lời và chưa trả lời
            function updateQuestionCounts() {
                $('#answeredCount').text(answeredQuestions);
                $('#unansweredCount').text(totalQuestions - answeredQuestions);
            }

            // Kiểm tra khi người dùng thay đổi câu trả lời
            $('input[type="radio"], textarea').on('change', function() {
                const questionContainer = $(this).closest('.question-container');
                const questionId = questionContainer.data('question-id');
                const isAnswered = questionContainer.find('input[type="radio"]:checked, textarea').filter(function() {
                    return $(this).val().trim() !== '';
                }).length > 0;

                // Đếm lại tất cả các câu đã trả lời
                answeredQuestions = $('.question-container').filter(function() {
                    return $(this).find('input[type="radio"]:checked, textarea').filter(function() {
                        return $(this).val().trim() !== '';
                    }).length > 0;
                }).length;

                updateQuestionCounts();
                updateSubmitButton();

                // Nếu là radio button, cập nhật các trường ẩn
                if ($(this).is('input[type="radio"]')) {
                    updateHiddenFields($(this));
                }
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

            // Cập nhật các trường ẩn cho câu hỏi
            function updateHiddenFields(radioElement) {
                const questionContainer = radioElement.closest('.question-container');
                const questionId = questionContainer.data('question-id');
                const selectedAnswer = radioElement.val();
                const isCorrect = radioElement.data('is-correct');
                
                // Log để debug giá trị
                console.log(`=== Cập nhật câu hỏi ${questionId} ===`);
                console.log('Selected Answer:', selectedAnswer);
                console.log('Is Correct:', isCorrect);
                
                // Lấy điểm số từ question-mark span
                const questionMarkText = questionContainer.find('.question-mark').text();
                const maxMark = parseFloat(questionMarkText) || 0;
                const mark = isCorrect ? maxMark : 0;
                console.log('Mark:', mark, '(Max:', maxMark, ')');

                // Cập nhật các trường ẩn
                $(`#answerLabel_${questionId}`).val(selectedAnswer);
                $(`#isCorrect_${questionId}`).val(isCorrect);
                $(`#mark_${questionId}`).val(mark);
            }

            // Kiểm tra ban đầu
            $('.question-container').each(function() {
                if ($(this).find('input[type="radio"]:checked, textarea').filter(function() {
                    return $(this).val().trim() !== '';
                }).length > 0) {
                    answeredQuestions++;
                }
            });
            updateQuestionCounts();
            updateSubmitButton();

            // Xử lý khi submit form
            $('#assignmentForm').on('submit', function(e) {
                e.preventDefault();

                // Kiểm tra dữ liệu bắt buộc
                const assignmentID = $('input[name="assignmentID"]').val();
                const assignTakenID = $('input[name="assignTakenID"]').val();
                
                // Debug thông tin form
                console.log('=== DEBUG FORM DATA ===');
                console.log('Form action:', $('#assignmentForm').attr('action'));
                console.log('Form method:', $('#assignmentForm').attr('method'));
                console.log('assignmentID input:', $('input[name="assignmentID"]').length, 'found');
                console.log('assignmentID value:', assignmentID);
                console.log('assignTakenID input:', $('input[name="assignTakenID"]').length, 'found');
                console.log('assignTakenID value:', assignTakenID);
                
                if (!assignmentID || !assignTakenID) {
                    console.error('Missing required fields:');
                    console.error('assignmentID:', assignmentID);
                    console.error('assignTakenID:', assignTakenID);
                    alert('Lỗi: Không tìm thấy thông tin bài tập. Vui lòng thử lại.');
                    return false;
                }

                // Thu thập dữ liệu từ form
                const formData = {
                    assignmentID: assignmentID,
                    assignTakenID: assignTakenID,
                    assignQuesID: [],
                    answerLabels: [],
                    isCorrects: [],
                    marks: []
                };

                // Thu thập dữ liệu từ form
                $('.question-container').each(function() {
                    const questionId = $(this).data('question-id');
                    console.log(`Processing question ${questionId}`);
                    
                    if (!questionId) {
                        console.error('Thiếu question ID cho câu hỏi');
                        return;
                    }

                    // Thêm ID câu hỏi vào mảng
                    formData.assignQuesID.push(questionId);
                    
                    // Tìm radio button được chọn trong câu hỏi này
                    const selectedRadio = $(this).find('input[type="radio"]:checked');
                    console.log(`Found selected radio for ${questionId}:`, selectedRadio.length > 0);
                    
                    // Lấy câu trả lời được chọn
                    let answerLabel = 'SKIPPED';  // Giá trị mặc định là SKIPPED
                    let isCorrect = false;
                    let mark = 0;
                    
                    if (selectedRadio.length > 0) {
                        answerLabel = selectedRadio.val();
                        isCorrect = selectedRadio.data('is-correct') === true;
                        // Lấy điểm số từ question-mark span
                        const questionMarkText = $(this).find('.question-mark').text();
                        const questionMark = parseFloat(questionMarkText) || 0;
                        // Nếu câu trả lời đúng thì được điểm tối đa của câu hỏi
                        mark = isCorrect ? questionMark : 0;
                        
                        console.log(`Question ${questionId} data:`, {
                            answerLabel,
                            isCorrect,
                            mark,
                            questionMark,
                            radioValue: selectedRadio.val(),
                            isCorrectData: selectedRadio.data('is-correct')
                        });
                    }

                    // Thêm thông tin vào các mảng tương ứng
                    formData.answerLabels.push(answerLabel);
                    formData.isCorrects.push(isCorrect);
                    formData.marks.push(mark);

                    // Log thông tin chi tiết cho mỗi câu hỏi
                    console.log(`=== Chi tiết câu hỏi ${questionId} ===`);
                    console.log('Answer Label:', answerLabel);
                    console.log('Is Correct:', isCorrect);
                    console.log('Mark:', mark);
                });

                // Log toàn bộ dữ liệu form
                console.log('=== Dữ liệu form trước khi gửi ===');
                console.log(JSON.stringify(formData, null, 2));

                // Chuẩn bị dữ liệu để gửi
                const requestData = new URLSearchParams();
                requestData.append('assignmentID', formData.assignmentID);
                requestData.append('assignTakenID', formData.assignTakenID);
                
                // Thêm các mảng dữ liệu
                formData.assignQuesID.forEach((id, index) => {
                    requestData.append('assignQuesID[]', id);
                    requestData.append('answerLabel[]', formData.answerLabels[index]);
                    requestData.append('isCorrect[]', formData.isCorrects[index]);
                    requestData.append('mark[]', formData.marks[index]);
                });

                // Log dữ liệu cuối cùng sẽ gửi
                console.log('=== Dữ liệu gửi đi ===');
                for (let [key, value] of requestData.entries()) {
                    console.log(key + ': ' + value);
                }

                // Hiển thị loading
                $('.loading').show();
                $('#submitBtn').prop('disabled', true);

                // Gửi request bằng AJAX
                $.ajax({
                    url: 'submit-assignment',
                    method: 'POST',
                    data: requestData.toString(),
                    contentType: 'application/x-www-form-urlencoded',
                    success: function(response) {
                        $('.loading').hide();
                        $('#submitBtn').prop('disabled', false);

                        try {
                            if (response.success) {
                                alert('Nộp bài thành công!');
                                window.location.href = 'view-assignment-result?assignmentID=' + formData.assignmentID;
                            } else {
                                alert(response.message || 'Có lỗi xảy ra khi nộp bài. Vui lòng thử lại.');
                            }
                        } catch (e) {
                            console.error('Lỗi khi xử lý phản hồi:', e);
                            alert('Có lỗi xảy ra khi xử lý phản hồi từ server');
                        }
                    },
                    error: function(xhr, status, error) {
                        $('.loading').hide();
                        $('#submitBtn').prop('disabled', false);
                        
                        console.error('Lỗi AJAX:', {
                            status: status,
                            error: error,
                            response: xhr.responseText
                        });

                        let errorMessage = 'Có lỗi xảy ra khi nộp bài';
                        try {
                            const response = JSON.parse(xhr.responseText);
                            if (response.message) {
                                errorMessage = response.message;
                            }
                        } catch (e) {
                            console.error('Lỗi khi parse JSON response:', e);
                        }

                        alert(errorMessage);
                    }
                });
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
                            updateHiddenFields(questionContainer.find(`input[type="radio"][value="${state.answer}"]`));
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