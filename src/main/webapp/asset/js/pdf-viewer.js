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
        // Khởi tạo PDF.js
        pdfjsLib.GlobalWorkerOptions.workerSrc = 'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.4.120/pdf.worker.min.js';
        
        // Tạo canvas
        this.canvas = document.createElement('canvas');
        this.canvas.className = 'pdf-canvas';
        this.ctx = this.canvas.getContext('2d');
        
        // Thêm canvas vào container
        const viewer = this.container.querySelector('.pdf-viewer');
        viewer.appendChild(this.canvas);
        
        // Load PDF
        try {
            this.pdfDoc = await pdfjsLib.getDocument(this.pdfUrl).promise;
            this.renderPage(this.pageNum);
            this.updatePageInfo();
            this.setupControls();
        } catch (error) {
            console.error('Error loading PDF:', error);
            this.showError('Không thể tải tài liệu PDF');
        }
    }

    setupControls() {
        // Setup zoom controls
        const zoomInBtn = this.container.querySelector('.zoom-in');
        const zoomOutBtn = this.container.querySelector('.zoom-out');
        const prevPageBtn = this.container.querySelector('.prev-page');
        const nextPageBtn = this.container.querySelector('.next-page');
        const searchInput = this.container.querySelector('.pdf-search-input');
        const searchResults = this.container.querySelector('.pdf-search-results');

        zoomInBtn.addEventListener('click', () => this.zoom(0.1));
        zoomOutBtn.addEventListener('click', () => this.zoom(-0.1));
        prevPageBtn.addEventListener('click', () => this.prevPage());
        nextPageBtn.addEventListener('click', () => this.nextPage());
        
        // Setup search
        let searchTimeout;
        searchInput.addEventListener('input', (e) => {
            clearTimeout(searchTimeout);
            searchTimeout = setTimeout(() => {
                this.search(e.target.value);
            }, 300);
        });

        // Setup annotation tools
        const highlightBtn = this.container.querySelector('.highlight-btn');
        const noteBtn = this.container.querySelector('.note-btn');
        
        highlightBtn.addEventListener('click', () => this.startHighlighting());
        noteBtn.addEventListener('click', () => this.startNoteTaking());
    }

    async renderPage(num) {
        this.pageRendering = true;
        
        try {
            const page = await this.pdfDoc.getPage(num);
            const viewport = page.getViewport({ scale: this.scale });
            
            this.canvas.height = viewport.height;
            this.canvas.width = viewport.width;
            
            const renderContext = {
                canvasContext: this.ctx,
                viewport: viewport
            };
            
            await page.render(renderContext).promise;
            this.pageRendering = false;
            
            if (this.pageNumPending !== null) {
                this.renderPage(this.pageNumPending);
                this.pageNumPending = null;
            }
            
            this.updatePageInfo();
        } catch (error) {
            console.error('Error rendering page:', error);
            this.showError('Không thể hiển thị trang');
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
        if (this.pageNum >= this.pdfDoc.numPages) return;
        this.pageNum++;
        this.queueRenderPage(this.pageNum);
    }

    zoom(delta) {
        this.scale += delta;
        if (this.scale < 0.5) this.scale = 0.5;
        if (this.scale > 3) this.scale = 3;
        this.renderPage(this.pageNum);
    }

    updatePageInfo() {
        const pageInfo = this.container.querySelector('.pdf-page-info');
        pageInfo.textContent = `Trang ${this.pageNum} / ${this.pdfDoc.numPages}`;
    }

    async search(text) {
        if (!text) {
            this.clearSearch();
            return;
        }

        this.searchResults = [];
        this.currentSearchIndex = -1;

        for (let i = 1; i <= this.pdfDoc.numPages; i++) {
            const page = await this.pdfDoc.getPage(i);
            const textContent = await page.getTextContent();
            const matches = textContent.items.filter(item => 
                item.str.toLowerCase().includes(text.toLowerCase())
            );
            
            if (matches.length > 0) {
                this.searchResults.push({
                    page: i,
                    matches: matches
                });
            }
        }

        this.displaySearchResults();
    }

    displaySearchResults() {
        const resultsContainer = this.container.querySelector('.pdf-search-results');
        resultsContainer.innerHTML = '';
        
        if (this.searchResults.length === 0) {
            resultsContainer.style.display = 'none';
            return;
        }

        this.searchResults.forEach((result, index) => {
            const item = document.createElement('div');
            item.className = 'pdf-search-result-item';
            item.textContent = `Trang ${result.page}: ${result.matches[0].str.substring(0, 50)}...`;
            item.addEventListener('click', () => {
                this.pageNum = result.page;
                this.renderPage(this.pageNum);
                this.highlightMatch(result.matches[0]);
            });
            resultsContainer.appendChild(item);
        });

        resultsContainer.style.display = 'block';
    }

    clearSearch() {
        this.searchResults = [];
        this.currentSearchIndex = -1;
        const resultsContainer = this.container.querySelector('.pdf-search-results');
        resultsContainer.style.display = 'none';
        this.renderPage(this.pageNum);
    }

    highlightMatch(match) {
        // Implementation for highlighting text
        // This is a simplified version - actual implementation would require
        // more complex text layer handling
    }

    startHighlighting() {
        // Implementation for text highlighting
    }

    startNoteTaking() {
        // Implementation for adding notes
    }

    showError(message) {
        const errorDiv = document.createElement('div');
        errorDiv.className = 'pdf-error';
        errorDiv.textContent = message;
        this.container.appendChild(errorDiv);
    }
}

// Initialize PDF viewer when document is ready
document.addEventListener('DOMContentLoaded', () => {
    const pdfContainers = document.querySelectorAll('.pdf-container');
    pdfContainers.forEach(container => {
        const pdfUrl = container.dataset.pdfUrl;
        if (pdfUrl) {
            new PDFViewer(container.id, pdfUrl);
        }
    });
}); 