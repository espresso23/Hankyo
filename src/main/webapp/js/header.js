function getNotificationIcon(type) {
    switch (type) {
        case 'Forum':
            return 'fas fa-comments';
        case 'Comment':
            return 'fas fa-comment-dots';
        case 'Course':
            return 'fas fa-graduation-cap';
        case 'Honour':
            return 'fas fa-award';
        default:
            return 'fas fa-bell';
    }
}

function formatTime(dateString) {
    const date = new Date(dateString);
    const now = new Date();
    const diffInSeconds = Math.floor((now - date) / 1000);
    const diffInMinutes = Math.floor(diffInSeconds / 60);
    const diffInHours = Math.floor(diffInMinutes / 60);
    const diffInDays = Math.floor(diffInHours / 24);

    if (diffInSeconds < 60) {
        return 'Vừa xong';
    } else if (diffInMinutes < 60) {
        return `${diffInMinutes} phút trước`;
    } else if (diffInHours < 24) {
        return `${diffInHours} giờ trước`;
    } else if (diffInDays < 7) {
        return `${diffInDays} ngày trước`;
    } else {
        return date.toLocaleDateString('vi-VN', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    }
}

function getNotificationLink(notification) {
    switch (notification.typeName) {
        case 'Forum':
            return `post-details?id=${notification.sourceID}`;
        case 'Comment':
            return `post-details?id=${notification.sourceID}#comment-${notification.sourceID}`;
        case 'Course':
            return `course-details?id=${notification.sourceID}`;
        default:
            return '#';
    }
}

function renderNotifications(notifications) {
    const notificationList = document.getElementById('notificationList');
    
    if (!notificationList) {
        console.error('Notification list element not found');
        return;
    }

    // Clear existing content
    notificationList.innerHTML = '';

    if (!notifications || notifications.length === 0) {
        const emptyDiv = document.createElement('div');
        emptyDiv.className = 'empty-notifications';
        emptyDiv.textContent = 'Không có thông báo mới';
        notificationList.appendChild(emptyDiv);
        return;
    }

    notifications.forEach(notification => {
        const notificationDiv = document.createElement('div');
        notificationDiv.className = `notification-item ${notification.isRead === 0 ? 'unread' : ''}`;
        
        const link = document.createElement('a');
        link.href = getNotificationLink(notification);
        link.style.textDecoration = 'none';
        link.style.color = 'inherit';
        link.style.display = 'flex';
        link.style.alignItems = 'flex-start';
        link.style.gap = '12px';
        link.style.width = '100%';
        
        // Create icon container
        const iconDiv = document.createElement('div');
        iconDiv.className = 'notification-icon';
        const icon = document.createElement('i');
        icon.className = getNotificationIcon(notification.typeName);
        iconDiv.appendChild(icon);
        
        // Create content container
        const contentWrapper = document.createElement('div');
        contentWrapper.style.flex = '1';
        
        const contentDiv = document.createElement('div');
        contentDiv.className = 'notification-content';
        contentDiv.textContent = notification.description;
        
        const timeDiv = document.createElement('div');
        timeDiv.className = 'notification-time';
        timeDiv.textContent = formatTime(notification.dateGiven);
        
        contentWrapper.appendChild(contentDiv);
        contentWrapper.appendChild(timeDiv);
        
        link.appendChild(iconDiv);
        link.appendChild(contentWrapper);
        notificationDiv.appendChild(link);
        
        notificationDiv.onclick = (e) => {
            if (!notification.isRead) {
                e.preventDefault();
                markAsRead(notification.significationID, notificationDiv).then(() => {
                    window.location.href = link.href;
                });
            }
        };
        
        notificationList.appendChild(notificationDiv);
    });
}

function toggleNotifications(event) {
    if (event) {
        event.stopPropagation();
    }
    const dropdown = document.querySelector('.notification-dropdown');
    if (!dropdown) {
        console.error('Notification dropdown not found');
        return;
    }
    
    dropdown.classList.toggle('show');
    
    if (dropdown.classList.contains('show')) {
        loadNotifications();
    }
}

// Event listeners
document.addEventListener('DOMContentLoaded', () => {
    // Mobile menu
    const mobileMenuBtn = document.getElementById('mobileMenuButton');
    if (mobileMenuBtn) {
        mobileMenuBtn.addEventListener('click', toggleMobileMenu);
    }

    // Notifications
    const notificationBell = document.querySelector('.notification-bell');
    if (notificationBell) {
        notificationBell.addEventListener('click', toggleNotifications);
    }

    const markAllReadBtn = document.querySelector('.mark-all-read');
    if (markAllReadBtn) {
        markAllReadBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            markAllAsRead();
        });
    }

    // Load initial notification count
    loadNotificationCount();

    // Refresh notification count every 30 seconds
    setInterval(loadNotificationCount, 30000);

    // Close dropdowns when clicking outside
    document.addEventListener('click', (event) => {
        const notificationDropdown = document.querySelector('.notification-dropdown');
        const notificationBell = document.querySelector('.notification-bell');

        if (notificationDropdown && notificationBell) {
            if (!notificationDropdown.contains(event.target) && !notificationBell.contains(event.target)) {
                notificationDropdown.classList.remove('show');
            }
        }
    });
}); 