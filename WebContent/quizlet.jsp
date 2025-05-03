<style>
    /* Tab styles */
    .nav-tabs {
        border-bottom: 1px solid #e0e0e0;
        padding: 0 10px;
    }
    
    .nav-tabs .nav-link {
        border: 1px solid #e0e0e0;
        border-bottom: none;
        border-radius: 4px 4px 0 0;
        padding: 8px 16px;
        margin-right: 4px;
        background: #f5f5f5;
        color: #666;
        font-size: 14px;
        transition: all 0.2s ease;
        position: relative;
        top: 1px;
    }
    
    .nav-tabs .nav-link:hover {
        background: #fff;
        transform: scale(1.02);
        box-shadow: 0 -2px 4px rgba(0,0,0,0.05);
    }
    
    .nav-tabs .nav-link.active {
        background: #fff;
        border-bottom: 2px solid #007bff;
        color: #007bff;
        font-weight: 500;
    }
    
    .nav-tabs .nav-link.active:hover {
        transform: none;
        box-shadow: none;
    }
    
    /* Existing styles */
    .flashcard-container {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
        padding: 20px;
    }
</style> 