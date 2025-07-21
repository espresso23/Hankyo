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
    const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 1));
    switch (notification.typeName) {
        case 'Forum':
            return `${contextPath}/postDetails?postID=${notification.sourceID}`;
        case 'Course':
            return `${contextPath}/course-details?postID=${notification.sourceID}`;
        default:
            return '#';
    }
}

function handleCommentNotification(notification) {
    const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 1));
    const url = `${contextPath}/CommentServlet?action=getComment&commentID=${notification.sourceID}`;
    console.log('Fetching comment post info from:', url);

    return fetch(url, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        if (data.success && data.postID) {
            window.location.href = `${contextPath}/postDetails?postID=${data.postID}`;
        } else {
            throw new Error(data.message || 'Failed to get post information');
        }
    })
    .catch(error => {
        console.error('Error getting post for comment:', error);
        // Redirect to home page or show error message
        window.location.href = contextPath;
    });
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
        link.href = '#';
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
            e.preventDefault();
            const handleClick = () => {
                if (notification.typeName === 'Comment') {
                    handleCommentNotification(notification)
                        .then(url => {
                            window.location.href = url;
                        })
                        .catch(error => {
                            console.error('Error handling comment notification:', error);
                        });
                } else {
                    window.location.href = getNotificationLink(notification);
                }
            };

            if (!notification.isRead) {
                markAsRead(notification.significationID, notificationDiv)
                    .then(handleClick)
                    .catch(error => {
                        console.error('Error marking notification as read:', error);
                        handleClick();
                    });
            } else {
                handleClick();
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

function handleNotificationClick(notification) {
    console.log('Handling notification click:', notification);
    
    // Get the context path
    const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 1));
    console.log('Context path:', contextPath);
    
    switch(notification.typeName) {
        case 'Comment':
            const url = `${contextPath}/getCommentPost?commentID=${notification.sourceID}`;
            console.log('Fetching from URL:', url);
            
            fetch(url, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                }
            })
            .then(response => {
                console.log('Response status:', response.status);
                console.log('Response headers:', Object.fromEntries(response.headers.entries()));
                
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                
                return response.text().then(text => {
                    console.log('Raw response:', text);
                    try {
                        return JSON.parse(text);
                    } catch (e) {
                        console.error('Failed to parse JSON:', e);
                        throw new Error('Invalid JSON response');
                    }
                });
            })
            .then(data => {
                console.log('Parsed response data:', data);
                if (data.success && data.postID) {
                    const redirectUrl = `${contextPath}/postDetails?postID=${data.postID}#comment-${notification.sourceID}`;
                    console.log('Redirecting to:', redirectUrl);
                    window.location.href = redirectUrl;
                } else {
                    console.error('Invalid response data:', data);
                }
            })
            .catch(error => {
                console.error('Error details:', {
                    message: error.message,
                    stack: error.stack,
                    notification: notification
                });
            });
            break;
        case 'Forum':
            window.location.href = `${contextPath}/postDetails?postID=${notification.sourceID}`;
            break;
        case 'Course':
            window.location.href = `${contextPath}/course-details?postID=${notification.sourceID}`;
            break;
        default:
            console.log('Unknown notification type:', notification.typeName);
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