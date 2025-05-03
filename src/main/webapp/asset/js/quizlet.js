function toggleInputMode() {
    const manualInput = document.querySelector('.manual-input');
    const individualInput = document.querySelector('.individual-input');
    const toggleBtn = document.querySelector('.toggle-btn');
    if (window.isManualMode) {
        manualInput.classList.remove('active');
        individualInput.classList.add('active');
        toggleBtn.textContent = 'Chuyển sang nhập thủ công';
        document.getElementById('manualTopic').removeAttribute('required');
        document.getElementById('manualFlashCards').removeAttribute('required');
        document.getElementById('individualTopic').setAttribute('required', '');
        document.getElementById('word').setAttribute('required', '');
        document.getElementById('mean').setAttribute('required', '');
    } else {
        manualInput.classList.add('active');
        individualInput.classList.remove('active');
        toggleBtn.textContent = 'Chuyển sang nhập từng ô';
        document.getElementById('manualTopic').setAttribute('required', '');
        document.getElementById('manualFlashCards').setAttribute('required', '');
        document.getElementById('individualTopic').removeAttribute('required');
        document.getElementById('word').removeAttribute('required');
        document.getElementById('mean').removeAttribute('required');
    }
    window.isManualMode = !window.isManualMode;
}
window.toggleInputMode = toggleInputMode;
window.isManualMode = true;

// Helper to show error on topic-box
function showErrorOnTopic(type, topic, message) {
    const box = document.querySelector(`.topic-box[data-type="${type}"][data-topic="${topic}"]`);
    if (box) {
        let errorElem = box.querySelector('.topic-error');
        if (!errorElem) {
            errorElem = document.createElement('span');
            errorElem.className = 'topic-error';
            errorElem.style.color = 'red';
            errorElem.style.display = 'block';
            box.appendChild(errorElem);
        }
        errorElem.textContent = message;
    }
}

// Update fetchFlashcardCount to show error if needed
async function fetchFlashcardCount(type, topic) {
    try {
        const response = await fetch(`${window.contextPath}/quizlet`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            },
            body: `action=getFlashCardCount&type=${encodeURIComponent(type)}&topic=${encodeURIComponent(topic)}`
        });
        if (!response.ok) {
            throw new Error('Không thể lấy số lượng');
        }
        const data = await response.json();
        if (typeof data.count !== 'number') {
            throw new Error(data.error || 'Dữ liệu trả về không hợp lệ');
        }
        return data.count;
    } catch (error) {
        showErrorOnTopic(type, topic, error.message);
        return 0;
    }
}

document.addEventListener('DOMContentLoaded', function() {
    // Xử lý chuyển đổi tab
    const tabs = document.querySelectorAll('.tab');
    const tabContents = document.querySelectorAll('.tab-content');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            tabContents.forEach(c => c.classList.remove('active'));

            tab.classList.add('active');
            const tabContent = document.getElementById(tab.dataset.tab);
            tabContent.classList.add('active');
            loadFlashcardCounts(tab.dataset.tab);
        });
    });

    // Xử lý auto-scroll và nút cuộn cho topics
    const scrollContainers = document.querySelectorAll('.topics-scroll');
    const scrollAmount = 150;
    let autoScrolls = [];

    function startAutoScroll(container, index) {
        autoScrolls[index] = setInterval(() => {
            container.scrollLeft += scrollAmount;
            if (container.scrollLeft + container.clientWidth >= container.scrollWidth) {
                container.scrollLeft = 0;
            }
        }, 3000);
    }

    function stopAutoScroll(index) {
        clearInterval(autoScrolls[index]);
    }

    scrollContainers.forEach((container, index) => {
        const containerParent = container.closest('.topics-scroll-container');
        const leftBtn = containerParent.querySelector('.scroll-btn.left');
        const rightBtn = containerParent.querySelector('.scroll-btn.right');

        container.addEventListener('mouseenter', () => stopAutoScroll(index));
        container.addEventListener('mouseleave', () => startAutoScroll(container, index));
        container.addEventListener('click', () => {
            if (autoScrolls[index]) {
                stopAutoScroll(index);
                autoScrolls[index] = null;
            } else {
                startAutoScroll(container, index);
            }
        });

        leftBtn.addEventListener('click', () => {
            container.scrollLeft -= scrollAmount;
            loadVisibleFlashcardCounts(container);
        });

        rightBtn.addEventListener('click', () => {
            container.scrollLeft += scrollAmount;
            loadVisibleFlashcardCounts(container);
        });

        container.addEventListener('scroll', () => {
            loadVisibleFlashcardCounts(container);
        });

        startAutoScroll(container, index);
    });

    // Load số lượng flashcard cho topic hiển thị
    async function loadVisibleFlashcardCounts(container) {
        const topicBoxes = container.querySelectorAll('.topic-box');
        const containerRect = container.getBoundingClientRect();
        const visibleBoxes = Array.from(topicBoxes).filter(box => {
            const boxRect = box.getBoundingClientRect();
            return boxRect.left >= containerRect.left && boxRect.right <= containerRect.right;
        });

        for (const box of visibleBoxes) {
            const countElement = box.querySelector('.topic-count');
            if (countElement.textContent === '0 từ') { // Chỉ load nếu chưa có số lượng
                const topic = box.dataset.topic;
                const type = box.dataset.type;
                const count = await fetchFlashcardCount(type, topic);
                countElement.textContent = `${count} từ`;
            }
        }
    }

    // Load số lượng flashcard cho tab
    async function loadFlashcardCounts(tabId) {
        const container = document.querySelector(`#${tabId} .topics-scroll`);
        loadVisibleFlashcardCounts(container);
    }

    // Load số lượng cho tab mặc định (system)
    loadFlashcardCounts('system');

    // Xử lý thêm flashcard qua AJAX (mới)
    const flashcardForm = document.getElementById('flashcardForm');
    if (flashcardForm) {
        flashcardForm.addEventListener('submit', function(event) {
            event.preventDefault();
            // Xóa thông báo cũ
            let resultContainer = flashcardForm.parentNode.querySelector('.result-container');
            if (!resultContainer) {
                resultContainer = document.createElement('div');
                resultContainer.className = 'result-container';
                flashcardForm.parentNode.insertBefore(resultContainer, flashcardForm);
            }
            resultContainer.innerHTML = '';

            // Xác định mode
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
                // Kiểm tra dữ liệu trả về có đúng format không
                if (typeof data.success === 'undefined') {
                    resultContainer.innerHTML = `<div class="error-list"><p>Lỗi: Dữ liệu trả về không hợp lệ hoặc server trả về sai endpoint.</p></div>`;
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
                resultContainer.innerHTML = `<div class="error-list"><p>Lỗi: ${error.message}</p></div>`;
            });
        });
    }

    // Cập nhật danh sách topic trong tab Custom
    async function updateCustomTopics(newTopic, flashcardCount) {
        const customScroll = document.querySelector('#custom .topics-scroll');
        const existingTopics = Array.from(customScroll.querySelectorAll('.topic-box')).map(box => box.dataset.topic);

        if (!existingTopics.includes(newTopic)) {
            const topicBox = document.createElement('div');
            topicBox.className = 'topic-box';
            topicBox.dataset.topic = newTopic;
            topicBox.dataset.type = 'custom';
            topicBox.innerHTML = `
        <a href="flashCard?topic=${encodeURIComponent(newTopic)}&type=custom">${newTopic}</a>
        <span class="topic-count">${flashcardCount} từ</span>
      `;
            customScroll.appendChild(topicBox);
            // Cuộn đến topic mới nếu nằm ngoài viewport
            const containerRect = customScroll.getBoundingClientRect();
            const boxRect = topicBox.getBoundingClientRect();
            if (boxRect.right > containerRect.right) {
                customScroll.scrollLeft += boxRect.right - containerRect.right;
            }
        } else {
            const topicBox = customScroll.querySelector(`.topic-box[data-topic="${newTopic}"]`);
            const countElement = topicBox.querySelector('.topic-count');
            const currentCount = parseInt(countElement.textContent) || 0;
            countElement.textContent = `${currentCount + flashcardCount} từ`;
        }
    }
});