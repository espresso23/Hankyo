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

    // Lấy số lượng flashcard từ server
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
            return data.count || 0;
        } catch (error) {
            console.error(`Error fetching count for ${type}/${topic}:`, error);
            return 0;
        }
    }

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

    // Xử lý toggle chế độ nhập
    let isManualMode = true;

    window.toggleInputMode = function() {
        const manualInput = document.querySelector('.manual-input');
        const individualInput = document.querySelector('.individual-input');
        const toggleBtn = document.querySelector('.toggle-btn');

        if (isManualMode) {
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
        isManualMode = !isManualMode;
    };

    // Xử lý thêm flashcard qua AJAX
    document.getElementById('flashcardForm').addEventListener('submit', function(event) {
        event.preventDefault();

        const form = event.target;
        const resultContainer = document.createElement('div');
        resultContainer.className = 'result-container';
        const existingResult = form.parentNode.querySelector('.result-container');
        if (existingResult) {
            existingResult.remove();
        }
        form.parentNode.insertBefore(resultContainer, form);

        let body;
        let flashcardCount = 0;
        if (isManualMode) {
            const topic = document.getElementById('manualTopic').value.trim();
            const flashCards = document.getElementById('manualFlashCards').value.trim();
            if (!topic || !flashCards) {
                resultContainer.innerHTML = `<div class="error-list"><p>Lỗi: Topic và flashcard không được để trống.</p></div>`;
                return;
            }
            flashcardCount = flashCards.split(';').filter(pair => pair.trim()).length;
            body = `action=add&mode=manual&topic=${encodeURIComponent(topic)}&flashCards=${encodeURIComponent(flashCards)}`;
        } else {
            const topic = document.getElementById('individualTopic').value.trim();
            const word = document.getElementById('word').value.trim();
            const mean = document.getElementById('mean').value.trim();
            if (!topic || !word || !mean) {
                resultContainer.innerHTML = `<div class="error-list"><p>Lỗi: Topic, từ và nghĩa không được để trống.</p></div>`;
                return;
            }
            flashcardCount = 1;
            body = `action=add&mode=individual&topic=${encodeURIComponent(topic)}&word=${encodeURIComponent(word)}&mean=${encodeURIComponent(mean)}`;
        }

        console.log('Sending add request:', body);
        fetch(`addFlashCard`,    {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            },
            body: body
        })
            .then(response => {
                console.log('Add response:', response.status, response.statusText);
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(text || 'Thêm thất bại');
                    });
                }
                return response.json();
            })
            .then(data => {
                resultContainer.innerHTML = '';
                if (data.success) {
                    const successDiv = document.createElement('div');
                    successDiv.className = 'success';
                    successDiv.innerHTML = '<p>Thêm thành công:</p><ul></ul>';
                    const ul = successDiv.querySelector('ul');
                    data.flashcards.forEach(card => {
                        const li = document.createElement('li');
                        li.textContent = `${card.word}:${card.mean}`;
                        ul.appendChild(li);
                    });
                    resultContainer.appendChild(successDiv);

                    // Cập nhật tab Custom
                    const topic = isManualMode
                        ? document.getElementById('manualTopic').value.trim()
                        : document.getElementById('individualTopic').value.trim();
                    updateCustomTopics(topic, flashcardCount);

                    // Reset form
                    form.reset();
                    if (!isManualMode) {
                        document.getElementById('individualTopic').value = '';
                        document.getElementById('word').value = '';
                        document.getElementById('mean').value = '';
                    }
                    alert('Thêm thành công');

                    // Cập nhật số lượng cho tab Custom nếu đang active
                    if (document.querySelector('.tab.active').dataset.tab === 'custom') {
                        const customScroll = document.querySelector('#custom .topics-scroll');
                        loadVisibleFlashcardCounts(customScroll);
                    }
                } else {
                    const errorDiv = document.createElement('div');
                    errorDiv.className = 'error-list';
                    errorDiv.innerHTML = '<p>Lỗi:</p><ul></ul>';
                    const ul = errorDiv.querySelector('ul');
                    data.errorMessages.forEach(msg => {
                        const li = document.createElement('li');
                        li.textContent = msg;
                        ul.appendChild(li);
                    });
                    resultContainer.appendChild(errorDiv);
                }
            })
            .catch(error => {
                console.error('Error adding flashcard:', error);
                resultContainer.innerHTML = `<div class="error-list"><p>Lỗi: ${error.message}</p></div>`;
            });
    });

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