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
    } catch (e) {
        console.error('Error parsing flashCardsJson:', e);
    }
    FlashcardStore.init(initialData);

    // Xử lý chuyển đổi tab
    const tabs = document.querySelectorAll('.tab');
    const tabContents = document.querySelectorAll('.tab-content');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            tabContents.forEach(c => c.classList.remove('active'));

            tab.classList.add('active');
            document.getElementById(`${tab.dataset.tab}-tab`).classList.add('active');

            if (tab.dataset.tab === 'edit') {
                FlashcardStore.renderTable();
            }
        });
    });

    // Xử lý sự kiện flashcard (Tab Flashcard)
    document.querySelector('.nextButton').addEventListener('click', () => {
        FlashcardStore.nextFlashcard();
    });

    document.querySelector('.previousButton').addEventListener('click', () => {
        FlashcardStore.previousFlashcard();
    });

    document.querySelector('.flashcard-inner').addEventListener('click', () => {
        FlashcardStore.toggleFlip();
    });

    // Xử lý sự kiện bảng (Tab Edit)
    document.querySelector('.wordTable').addEventListener('click', function(e) {
        const target = e.target;
        const row = target.closest('tr');
        if (!row) return;

        const cfcid = row.dataset.cfcid;
        if (!cfcid) {
            console.error('Missing cfcid for row:', row);
            alert('Lỗi: Không tìm thấy ID flashcard');
            return;
        }

        if (target.classList.contains('delete-btn')) {
            const word = target.dataset.word;
            if (confirm(`Bạn có chắc muốn xóa từ "${word}"?`)) {
                console.log('Sending delete request:', { cfcid });
                fetch(`${window.contextPath}/flashCard`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
                    body: `action=delete&cfcid=${cfcid}`
                })
                    .then(response => {
                        console.log('Delete response:', response.status, response.statusText);
                        if (response.ok) {
                            alert('Xóa thành công');
                            window.location.reload();
                        } else {
                            return response.text().then(text => {
                                throw new Error(text || 'Xóa thất bại');
                            });
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

            console.log('Sending update request:', { cfcid, newWord, newMean });
            fetch(`${window.contextPath}/flashCard`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
                body: `action=update&cfcid=${cfcid}&word=${encodeURIComponent(newWord)}&mean=${encodeURIComponent(newMean)}`
            })
                .then(response => {
                    console.log('Update response:', response.status, response.statusText);
                    if (response.ok) {
                        FlashcardStore.updateFlashcard(cfcid, newWord, newMean);
                        FlashcardStore.renderTable();
                        alert('Lưu thành công');
                    } else {
                        return response.text().then(text => {
                            throw new Error(text || 'Cập nhật thất bại');
                        });
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
        }

        console.log('Sending add request:', body);
        fetch(`${window.contextPath}/flashCard`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
            body: body
        })
            .then(response => {
                console.log('Add response:', response.status, response.statusText);
                if (response.ok) {
                    return response.json(); // Expect JSON with new flashcards
                } else {
                    return response.text().then(text => {
                        throw new Error(text || 'Thêm thất bại');
                    });
                }
            })
            .then(data => {
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

    // Debug log
    console.log('FlashcardStore initialized with:', FlashcardStore.flashcards);
});