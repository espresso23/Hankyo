document.addEventListener('DOMContentLoaded', function() {
    // Flashcard Store để quản lý trạng thái
    const FlashcardStore = {
        flashcards: [],
        currentIndex: 0,
        flipped: false,

        init(data) {
            this.flashcards = Array.isArray(data) ? data : [];
            this.currentIndex = 0;
            this.flipped = false;
            this.renderFlashcard();
        },

        addFlashcard(cfcid, word, mean) {
            this.flashcards.push({ CFCID: cfcid, word, mean });
            this.renderFlashcard();
        },

        updateFlashcard(cfcid, newWord, newMean) {
            const index = this.flashcards.findIndex(card => card.CFCID == cfcid);
            if (index !== -1) {
                this.flashcards[index] = { ...this.flashcards[index], word: newWord, mean: newMean };
                this.renderFlashcard();
            }
        },

        nextFlashcard() {
            this.currentIndex = (this.currentIndex + 1) % Math.max(1, this.flashcards.length);
            this.flipped = false;
            this.renderFlashcard();
        },

        previousFlashcard() {
            this.currentIndex = (this.currentIndex - 1 + this.flashcards.length) % Math.max(1, this.flashcards.length);
            this.flipped = false;
            this.renderFlashcard();
        },

        toggleFlip() {
            this.flipped = !this.flipped;
            this.renderFlashcard();
        },

        renderFlashcard() {
            const frontElement = document.querySelector('.flashcard-front');
            const backElement = document.querySelector('.flashcard-back');
            const flashcardInner = document.querySelector('.flashcard-inner');

            if (!frontElement || !backElement || !flashcardInner) {
                console.error('Flashcard elements not found');
                return;
            }

            if (!this.flashcards.length) {
                frontElement.textContent = 'Không có thẻ nào';
                backElement.textContent = 'Vui lòng thêm thẻ mới';
                flashcardInner.classList.remove('flipped');
                return;
            }

            const card = this.flashcards[this.currentIndex] || {};
            frontElement.textContent = card.word || 'Không có từ';
            backElement.textContent = card.mean || 'Không có nghĩa';
            flashcardInner.classList.toggle('flipped', this.flipped);
        },

        renderTable() {
            const tbody = document.querySelector('.wordTable tbody');
            if (!tbody) {
                console.error('Table body not found');
                return;
            }
            tbody.innerHTML = this.flashcards.map(card => `
                <tr data-cfcid="${card.CFCID}">
                    <td class="word-cell">${card.word}</td>
                    <td class="mean-cell">${card.mean}</td>
                    <td class="action-cell">
                        <button class="edit-btn" data-word="${card.word}" data-mean="${card.mean}">Edit</button>
                        <button class="delete-btn" data-word="${card.word}" data-mean="${card.mean}">X</button>
                    </td>
                </tr>
            `).join('');
        }
    };

    // Khởi tạo store với dữ liệu từ window.flashCardsJson
    let initialData = [];
    try {
        initialData = JSON.parse(JSON.stringify(window.flashCardsJson));
        console.log('FlashcardStore initialized with:', initialData);
    } catch (e) {
        console.error('Error parsing flashCardsJson:', e);
    }
    FlashcardStore.init(initialData);

    // Xử lý chuyển đổi tab
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');

    function switchTab(tabId) {
        // Remove active class from all buttons and contents
        tabButtons.forEach(button => button.classList.remove('active'));
        tabContents.forEach(content => content.classList.remove('active'));

        // Add active class to selected button and content
        const selectedButton = document.querySelector(`.tab-button[data-tab="${tabId}"]`);
        const selectedContent = document.getElementById(`${tabId}-tab`);

        if (selectedButton && selectedContent) {
            selectedButton.classList.add('active');
            selectedContent.classList.add('active');

            // If edit tab is activated, render the table
            if (tabId === 'edit') {
                FlashcardStore.renderTable();
            }
        }
    }

    // Add click event listeners to tab buttons
    tabButtons.forEach(button => {
        button.addEventListener('click', () => {
            const tabId = button.dataset.tab;
            switchTab(tabId);
        });
    });

    // Xử lý sự kiện flashcard
    const nextButton = document.querySelector('.nextButton');
    const previousButton = document.querySelector('.previousButton');
    const flashcardInner = document.querySelector('.flashcard-inner');

    if (nextButton) {
        nextButton.addEventListener('click', () => FlashcardStore.nextFlashcard());
    }

    if (previousButton) {
        previousButton.addEventListener('click', () => FlashcardStore.previousFlashcard());
    }

    if (flashcardInner) {
        flashcardInner.addEventListener('click', () => FlashcardStore.toggleFlip());
    }

    // Xử lý sự kiện bảng
    const wordTable = document.querySelector('.wordTable');
    if (wordTable) {
        wordTable.addEventListener('click', function(e) {
            const target = e.target;
            const row = target.closest('tr');
            if (!row) return;

            const cfcid = row.dataset.cfcid;
            if (!cfcid) {
                console.error('Missing cfcid for row');
                return;
            }

            if (target.classList.contains('delete-btn')) {
                const word = target.dataset.word;
                if (confirm(`Bạn có chắc muốn xóa từ "${word}"?`)) {
                    fetch(`${window.contextPath}/flashCard`, {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
                        body: `action=delete&cfcid=${cfcid}`
                    })
                    .then(response => {
                        if (response.ok) {
                            alert('Xóa thành công');
                            window.location.reload();
                        } else {
                            throw new Error('Xóa thất bại');
                        }
                    })
                    .catch(error => {
                        console.error('Error deleting flashcard:', error);
                        alert(`Có lỗi xảy ra khi xóa: ${error.message}`);
                    });
                }
            }

            if (target.classList.contains('edit-btn')) {
                const wordCell = row.querySelector('.word-cell');
                const meanCell = row.querySelector('.mean-cell');
                const actionCell = row.querySelector('.action-cell');
                const originalWord = wordCell.textContent;
                const originalMean = meanCell.textContent;

                wordCell.innerHTML = `<input type="text" class="edit-input" value="${originalWord}">`;
                meanCell.innerHTML = `<input type="text" class="edit-input" value="${originalMean}">`;
                actionCell.innerHTML = `
                    <button class="save-btn">Lưu</button>
                    <button class="cancel-btn">Hủy</button>
                `;
            }

            if (target.classList.contains('save-btn')) {
                const wordCell = row.querySelector('.word-cell');
                const meanCell = row.querySelector('.mean-cell');
                const newWord = wordCell.querySelector('.edit-input').value.trim();
                const newMean = meanCell.querySelector('.edit-input').value.trim();

                if (!newWord || !newMean) {
                    alert('Từ và nghĩa không được để trống');
                    return;
                }

                fetch(`${window.contextPath}/flashCard`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
                    body: `action=update&cfcid=${cfcid}&word=${encodeURIComponent(newWord)}&mean=${encodeURIComponent(newMean)}`
                })
                .then(response => {
                    if (response.ok) {
                        FlashcardStore.updateFlashcard(cfcid, newWord, newMean);
                        FlashcardStore.renderTable();
                        alert('Lưu thành công');
                    } else {
                        throw new Error('Cập nhật thất bại');
                    }
                })
                .catch(error => {
                    console.error('Error updating flashcard:', error);
                    alert(`Có lỗi xảy ra khi cập nhật: ${error.message}`);
                    FlashcardStore.renderTable();
                });
            }

            if (target.classList.contains('cancel-btn')) {
                FlashcardStore.renderTable();
            }
        });
    }

    // Xử lý toggle chế độ nhập
    let isManualMode = true;
    window.toggleInputMode = function() {
        const manualInput = document.querySelector('.manual-input');
        const individualInput = document.querySelector('.individual-input');
        const toggleBtn = document.querySelector('.toggle-btn');

        if (!manualInput || !individualInput || !toggleBtn) {
            console.error('Input mode elements not found');
            return;
        }

        if (isManualMode) {
            manualInput.classList.remove('active');
            individualInput.classList.add('active');
            toggleBtn.textContent = 'Chuyển sang nhập thủ công';
            document.getElementById('manualFlashCards').removeAttribute('required');
            document.getElementById('word').setAttribute('required', '');
            document.getElementById('mean').setAttribute('required', '');
        } else {
            manualInput.classList.add('active');
            individualInput.classList.remove('active');
            toggleBtn.textContent = 'Chuyển sang nhập từng ô';
            document.getElementById('manualFlashCards').setAttribute('required', '');
            document.getElementById('word').removeAttribute('required');
            document.getElementById('mean').removeAttribute('required');
        }
        isManualMode = !isManualMode;
    };

    // Xử lý thêm flashcard
    window.addFlashcard = function() {
        const topic = document.querySelector('.container').dataset.topic;
        let body;

        if (isManualMode) {
            const flashCardsInput = document.getElementById('manualFlashCards').value.trim();
            if (!flashCardsInput) {
                alert('Vui lòng nhập flashcard');
                return;
            }
            body = `action=add&mode=manual&topic=${encodeURIComponent(topic)}&flashCards=${encodeURIComponent(flashCardsInput)}`;
        } else {
            const word = document.getElementById('word').value.trim();
            const mean = document.getElementById('mean').value.trim();
            if (!word || !mean) {
                alert('Từ và nghĩa không được để trống');
                return;
            }
            body = `action=add&mode=individual&topic=${encodeURIComponent(topic)}&word=${encodeURIComponent(word)}&mean=${encodeURIComponent(mean)}`;
            console.log('Sending individual mode data:', {
                topic,
                word,
                mean,
                body
            });
        }

        fetch(`${window.contextPath}/flashCard`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
            body: body
        })
        .then(response => {
            console.log('Response status:', response.status);
            if (response.ok) {
                return response.json();
            } else {
                return response.text().then(text => {
                    console.error('Error response:', text);
                    throw new Error(text || 'Thêm thất bại');
                });
            }
        })
        .then(data => {
            console.log('Success response:', data);
            data.forEach(card => {
                FlashcardStore.addFlashcard(card.CFCID, card.word, card.mean);
            });
            FlashcardStore.renderTable();
            alert('Thêm thành công');
            // Reset form
            document.getElementById('manualFlashCards').value = '';
            document.getElementById('word').value = '';
            document.getElementById('mean').value = '';
        })
        .catch(error => {
            console.error('Error adding flashcard:', error);
            alert(`Có lỗi xảy ra khi thêm: ${error.message}`);
        });
    };

    function togglePublic(cfcid, isPublic) {
        const url = `${window.contextPath}/flashCard`;
        
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: `action=togglePublic&cfcid=${cfcid}&isPublic=${isPublic}`
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                // Update the button state
                const button = document.querySelector(`.toggle-public-btn[data-cfcid="${cfcid}"]`);
                if (button) {
                    const newState = !isPublic;
                    button.setAttribute('data-public', newState);
                    const label = button.querySelector('.toggle-label');
                    if (label) {
                        label.textContent = newState ? 'Public' : 'Private';
                    }
                }
                // Show success message
                alert(data.message || 'Successfully updated flashcard visibility');
            } else {
                alert(data.error || 'Failed to update flashcard visibility');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while updating flashcard visibility');
        });
    }
});