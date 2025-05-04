// Logic for adding flashcards via AJAX (manual and individual modes)
document.addEventListener('DOMContentLoaded', function() {
    const flashcardForm = document.getElementById('flashcardForm');
    if (flashcardForm) {
        flashcardForm.addEventListener('submit', function(event) {
            event.preventDefault();
            let resultContainer = flashcardForm.parentNode.querySelector('.result-container');
            if (!resultContainer) {
                resultContainer = document.createElement('div');
                resultContainer.className = 'result-container';
                flashcardForm.parentNode.insertBefore(resultContainer, flashcardForm);
            }
            resultContainer.innerHTML = '';
            const isManual = window.isManualMode;
            let body = '';
            if (isManual) {
                const topic = document.getElementById('manualTopic').value.trim();
                const flashCards = document.getElementById('manualFlashCards').value.trim();
                if (!topic || !flashCards) {
                    resultContainer.innerHTML = `<div class="error-list"><p>Vui lòng nhập đầy đủ topic và flashcard.</p></div>`;
                    return;
                }
                body = `mode=manual&manualTopic=${encodeURIComponent(topic)}&manualFlashCards=${encodeURIComponent(flashCards)}`;
            } else {
                const topic = document.getElementById('individualTopic').value.trim();
                const word = document.getElementById('word').value.trim();
                const mean = document.getElementById('mean').value.trim();
                if (!topic || !word || !mean) {
                    resultContainer.innerHTML = `<div class="error-list"><p>Vui lòng nhập đầy đủ topic, từ và nghĩa.</p></div>`;
                    return;
                }
                body = `mode=individual&individualTopic=${encodeURIComponent(topic)}&word=${encodeURIComponent(word)}&mean=${encodeURIComponent(mean)}`;
            }
            fetch(`${window.contextPath}/ajaxAddFlashCard`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                },
                body: body
            })
            .then(response => response.json())
            .then(data => {
                resultContainer.innerHTML = '';
                if (typeof data.success === 'undefined') {
                    resultContainer.innerHTML = `<div class=\"error-list\"><p>Lỗi: Dữ liệu trả về không hợp lệ hoặc server trả về sai endpoint.</p></div>`;
                    return;
                }
                if (data.success) {
                    const successDiv = document.createElement('div');
                    successDiv.className = 'success';
                    successDiv.innerHTML = '<p>Thêm thành công:</p><ul></ul>';
                    const ul = successDiv.querySelector('ul');
                    data.flashcards.forEach(card => {
                        const li = document.createElement('li');
                        li.textContent = `${card.word}: ${card.mean}`;
                        ul.appendChild(li);
                    });
                    resultContainer.appendChild(successDiv);
                    flashcardForm.reset();
                } else {
                    const errorDiv = document.createElement('div');
                    errorDiv.className = 'error-list';
                    errorDiv.innerHTML = '<p>Lỗi:</p><ul></ul>';
                    const ul = errorDiv.querySelector('ul');
                    (data.errorMessages || []).forEach(msg => {
                        const li = document.createElement('li');
                        li.textContent = msg;
                        ul.appendChild(li);
                    });
                    resultContainer.appendChild(errorDiv);
                }
            })
            .catch(error => {
                resultContainer.innerHTML = `<div class=\"error-list\"><p>Lỗi: ${error.message}</p></div>`;
            });
        });
    }
}); 