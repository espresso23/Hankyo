class PDFViewer {
    constructor(containerId, pdfUrl) {
        this.container = document.getElementById(containerId);
        this.pdfUrl = pdfUrl;
        this.pdfDoc = null;
        this.pageNum = 1;
        this.pageRendering = false;
        this.pageNumPending = null;
        this.scale = 1.0;
        this.canvas = null;
        this.ctx = null;
        this.annotations = [];
        this.searchResults = [];
        this.currentSearchIndex = -1;
        
        this.init();
    }

    async init() {
        try {
            // Khởi tạo PDF.js
            pdfjsLib.GlobalWorkerOptions.workerSrc = 'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.4.120/pdf.worker.min.js';
            
            // Tìm canvas có sẵn thay vì tạo mới
            this.canvas = this.container.querySelector('#pdf-canvas');
            if (!this.canvas) {
                console.error('Canvas element not found');
                return;
            }
            this.ctx = this.canvas.getContext('2d');
            
            // Load PDF
            this.pdfDoc = await pdfjsLib.getDocument(this.pdfUrl).promise;
            
            // Cập nhật UI
            this.updatePageCount();
            this.renderPage(this.pageNum);
            this.setupControls();
            this.setupSearch();
            
            // Hiển thị toolbar
            this.container.querySelector('.pdf-toolbar').style.display = 'flex';
            
        } catch (error) {
            console.error('Error initializing PDF viewer:', error);
            this.showError('Không thể tải tài liệu PDF. Vui lòng thử lại sau.');
        }
    }

    updatePageCount() {
        if (!this.pdfDoc) return;
        
        const currentPage = this.container.querySelector('.current-page');
        const totalPages = this.container.querySelector('.total-pages');
        
        if (currentPage) currentPage.textContent = this.pageNum;
        if (totalPages) totalPages.textContent = this.pdfDoc.numPages;
        
        // Cập nhật trạng thái nút
        const prevBtn = this.container.querySelector('.prev-page');
        const nextBtn = this.container.querySelector('.next-page');
        
        if (prevBtn) prevBtn.disabled = this.pageNum <= 1;
        if (nextBtn) nextBtn.disabled = this.pageNum >= this.pdfDoc.numPages;
    }

    setupControls() {
        // Điều hướng trang
        this.container.querySelector('.prev-page')?.addEventListener('click', () => this.prevPage());
        this.container.querySelector('.next-page')?.addEventListener('click', () => this.nextPage());
        
        // Zoom
        this.container.querySelector('.zoom-in')?.addEventListener('click', () => this.zoom(0.25));
        this.container.querySelector('.zoom-out')?.addEventListener('click', () => this.zoom(-0.25));
        
        // Fullscreen
        this.container.querySelector('.fullscreen-btn')?.addEventListener('click', () => this.toggleFullscreen());
        
        // Download
        this.container.querySelector('.download-btn')?.addEventListener('click', () => this.downloadPDF());
    }

    setupSearch() {
        const searchInput = this.container.querySelector('.pdf-search-input');
        const searchClear = this.container.querySelector('.search-clear');
        
        if (searchInput) {
            let searchTimeout;
            searchInput.addEventListener('input', (e) => {
                clearTimeout(searchTimeout);
                const text = e.target.value.trim();
                
                if (text === '') {
                    this.clearSearch();
                    return;
                }
                
                searchTimeout = setTimeout(() => this.search(text), 300);
            });
        }
        
        if (searchClear) {
            searchClear.addEventListener('click', () => {
                if (searchInput) searchInput.value = '';
                this.clearSearch();
            });
        }
    }

    async renderPage(num) {
        if (!this.pdfDoc || num < 1 || num > this.pdfDoc.numPages) return;
        
        this.pageRendering = true;
        
        try {
            const page = await this.pdfDoc.getPage(num);
            const viewport = page.getViewport({ scale: this.scale });
            
            // Điều chỉnh kích thước canvas
            this.canvas.height = viewport.height;
            this.canvas.width = viewport.width;
            
            // Render PDF page
            const renderContext = {
                canvasContext: this.ctx,
                viewport: viewport
            };
            
            await page.render(renderContext).promise;
            
            this.pageRendering = false;
            this.updatePageCount();
            
            // Xử lý trang đang chờ
            if (this.pageNumPending !== null) {
                this.renderPage(this.pageNumPending);
                this.pageNumPending = null;
            }
            
        } catch (error) {
            console.error('Error rendering page:', error);
            this.pageRendering = false;
            this.showError('Không thể hiển thị trang. Vui lòng thử lại.');
        }
    }

    queueRenderPage(num) {
        if (this.pageRendering) {
            this.pageNumPending = num;
        } else {
            this.renderPage(num);
        }
    }

    prevPage() {
        if (this.pageNum <= 1) return;
        this.pageNum--;
        this.queueRenderPage(this.pageNum);
    }

    nextPage() {
        if (!this.pdfDoc || this.pageNum >= this.pdfDoc.numPages) return;
        this.pageNum++;
        this.queueRenderPage(this.pageNum);
    }

    zoom(delta) {
        const newScale = this.scale + delta;
        if (newScale >= 0.25 && newScale <= 3) {
            this.scale = newScale;
            this.renderPage(this.pageNum);
            
            // Cập nhật hiển thị tỷ lệ zoom
            const zoomLevel = this.container.querySelector('.zoom-level');
            if (zoomLevel) {
                zoomLevel.textContent = `${Math.round(this.scale * 100)}%`;
            }
        }
    }

    async search(text) {
        if (!text || !this.pdfDoc) return;
        
        const searchResults = this.container.querySelector('.search-results');
        const searchCount = this.container.querySelector('.search-count');
        
        try {
            const page = await this.pdfDoc.getPage(this.pageNum);
            const textContent = await page.getTextContent();
            
            const matches = textContent.items.filter(item => 
                item.str.toLowerCase().includes(text.toLowerCase())
            );
            
            // Hiển thị kết quả
            if (searchCount) {
                searchCount.textContent = `${matches.length} kết quả`;
            }
            
            if (searchResults) {
                searchResults.classList.add('show');
            }
            
        } catch (error) {
            console.error('Search error:', error);
        }
    }

    clearSearch() {
        const searchResults = this.container.querySelector('.search-results');
        const searchCount = this.container.querySelector('.search-count');
        
        if (searchResults) {
            searchResults.classList.remove('show');
        }
        
        if (searchCount) {
            searchCount.textContent = '0 kết quả';
        }
    }

    toggleFullscreen() {
        if (!document.fullscreenElement) {
            this.container.requestFullscreen().catch(err => {
                console.error('Error attempting to enable fullscreen:', err);
            });
        } else {
            document.exitFullscreen();
        }
    }

    downloadPDF() {
        const link = document.createElement('a');
        link.href = this.pdfUrl;
        link.download = 'document.pdf';
        link.click();
    }

    showError(message) {
        const errorDiv = document.createElement('div');
        errorDiv.className = 'pdf-error';
        errorDiv.innerHTML = `
            <div class="error-content">
                <i class="fas fa-exclamation-circle"></i>
                <span>${message}</span>
            </div>
        `;
        
        this.container.appendChild(errorDiv);
        
        // Tự động ẩn sau 5 giây
        setTimeout(() => {
            errorDiv.remove();
        }, 5000);
    }
}

// Khởi tạo PDF viewer khi trang đã load
document.addEventListener('DOMContentLoaded', () => {
    const pdfContainers = document.querySelectorAll('[id^="pdfViewer_"]');
    pdfContainers.forEach(container => {
        const pdfUrl = container.dataset.pdfUrl;
        if (pdfUrl) {
            new PDFViewer(container.id, pdfUrl);
        }
    });
}); 