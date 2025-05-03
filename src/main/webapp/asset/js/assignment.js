document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('assignmentForm');
    const submitBtn = document.getElementById('submitBtn');
    const backBtn = document.getElementById('backBtn');
    const alertDiv = document.getElementById('alertDiv');
    
    // Kiểm tra xem tất cả câu hỏi đã được trả lời chưa
    function checkAllQuestionsAnswered() {
        const questionCount = parseInt(document.getElementById('questionCount').value);
        let allAnswered = true;
        
        for (let i = 1; i <= questionCount; i++) {
            const selectedOption = document.querySelector(`input[name="answer${i}"]:checked`);
            if (!selectedOption) {
                allAnswered = false;
                break;
            }
        }
        
        return allAnswered;
    }
    
    // Xử lý sự kiện submit form
    form.addEventListener('submit', function(e) {
        e.preventDefault();
        
        if (!checkAllQuestionsAnswered()) {
            showAlert('Vui lòng trả lời tất cả các câu hỏi trước khi nộp bài!', 'danger');
            return;
        }
        
        // Lấy dữ liệu từ form
        const formData = new FormData(form);
        const data = {
            assignmentID: formData.get('assignmentID'),
            contentID: formData.get('contentID'),
            answers: []
        };
        
        // Lấy tất cả câu trả lời
        for (let i = 1; i <= parseInt(formData.get('questionCount')); i++) {
            data.answers.push({
                questionID: i,
                selectedOption: formData.get(`answer${i}`)
            });
        }
        
        // Gửi dữ liệu lên server
        fetch('${pageContext.request.contextPath}/submit-assignment', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                showAlert('Nộp bài thành công! Điểm của bạn: ' + result.score, 'success');
                submitBtn.disabled = true;
                
                // Cập nhật tiến độ học tập
                updateProgress(result.contentID);
            } else {
                showAlert('Có lỗi xảy ra: ' + result.message, 'danger');
            }
        })
        .catch(error => {
            showAlert('Có lỗi xảy ra khi nộp bài: ' + error.message, 'danger');
        });
    });
    
    // Xử lý sự kiện nút quay lại
    backBtn.addEventListener('click', function() {
        window.location.href = '${pageContext.request.contextPath}/course-content?courseID=${param.courseID}';
    });
    
    // Hiển thị thông báo
    function showAlert(message, type) {
        alertDiv.innerHTML = `
            <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        `;
    }
    
    // Cập nhật tiến độ học tập
    function updateProgress(contentID) {
        fetch('${pageContext.request.contextPath}/update-progress', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                contentID: contentID,
                status: 'completed'
            })
        })
        .then(response => response.json())
        .then(result => {
            if (!result.success) {
                console.error('Lỗi khi cập nhật tiến độ:', result.message);
            }
        })
        .catch(error => {
            console.error('Lỗi khi cập nhật tiến độ:', error);
        });
    }
}); 