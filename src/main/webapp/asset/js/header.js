// Toggle Popup Container
function togglePopup() {
    const popup = document.getElementById('popupContainer');
    popup.style.display = popup.style.display === 'block' ? 'none' : 'block';
}

// Toggle Mobile Menu
function toggleMobileMenu() {
    const menu = document.getElementById('mobileVerticalMenu');
    const btn = document.getElementById('mobileMenuButton');

    if (menu.style.display === 'block') {
        menu.style.animation = 'fadeIn 0.3s ease-out reverse';
        setTimeout(() => {
            menu.style.display = 'none';
        }, 250);
        btn.classList.remove('active');
    } else {
        menu.style.display = 'block';
        menu.style.animation = 'fadeIn 0.3s ease-out';
        btn.classList.add('active');
    }
}

// Notification functions
function loadNotifications() {
    fetch(window.location.origin + '/Hankyo/notifications')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            renderNotifications(data);
        })
        .catch(error => console.error('Error loading notifications:', error));
}

function loadNotificationCount() {
    fetch(window.location.origin + '/Hankyo/notifications?action=count')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(count => {
            const countElement = document.getElementById('notificationCount');
            if (countElement) {
                countElement.textContent = count;
                countElement.style.display = count > 0 ? 'block' : 'none';
            }
        })
        .catch(error => console.error('Error loading notification count:', error));
}

function renderNotifications(notifications) {
    const notificationList = document.getElementById('notificationList');
    const dropdown = document.querySelector('.notification-dropdown');

    if (!notificationList) {
        console.error('Notification list element not found');
        return;
    }

    const header = notificationList.querySelector('.notification-header');
    notificationList.innerHTML = '';
    if (header) {
        notificationList.appendChild(header);
    }

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
        notificationDiv.onclick = () => handleNotificationClick(notification);

        const contentDiv = document.createElement('div');
        contentDiv.className = 'notification-content';
        contentDiv.textContent = notification.description;

        const timeDiv = document.createElement('div');
        timeDiv.className = 'notification-time';
        timeDiv.textContent = formatTime(notification.dateGiven);

        notificationDiv.appendChild(contentDiv);
        notificationDiv.appendChild(timeDiv);
        notificationList.appendChild(notificationDiv);
    });
}

function handleNotificationClick(notification) {
    markAsRead(notification.notificationID, () => {
        loadNotificationCount();
        switch (notification.typeID) {
            case 1:
                window.location.href = `${window.location.origin}/Hankyo/postDetails?postID=${notification.sourceID}`;
                break;
            case 2:
                fetch(`${window.location.origin}/Hankyo/postDetails?action=getPostID&commentID=${notification.sourceID}`)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(data => {
                        if (data && data.postID) {
                            window.location.href = `${window.location.origin}/Hankyo/postDetails?postID=${data.postID}#comment-${notification.sourceID}`;
                        }
                    })
                    .catch(error => console.error('Error getting post for comment:', error));
                break;
            case 3:
                window.location.href = `${window.location.origin}/Hankyo/course-details-learner?postID=${notification.sourceID}`;
                break;
            case 4:
                window.location.href = `${window.location.origin}/Hankyo/listHonour`;
                break;
            default:
                console.log('Unknown notification type:', notification.typeID);
        }
    });
}

function formatTime(timestamp) {
    const date = new Date(timestamp);
    const options = {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    };
    return date.toLocaleString('vi-VN', options);
}

function markAsRead(notificationID, callback) {
    fetch(window.location.origin + '/Hankyo/notifications', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `action=markRead&notificationID=${notificationID}`
    })
    .then(response => {
        if (response.ok && typeof callback === 'function') callback();
    })
    .catch(error => console.error('Error marking notification as read:', error));
}

function deleteAllNotifications() {
    fetch(window.location.origin + '/Hankyo/notifications?action=deleteAll', {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(() => {
            loadNotificationCount();
            loadNotifications();
        })
        .catch(error => console.error('Error deleting all notifications:', error));
}

function toggleNotifications(event) {
    event.stopPropagation();
    const dropdown = document.querySelector('.notification-dropdown');
    dropdown.classList.toggle('show');

    if (dropdown.classList.contains('show')) {
        loadNotifications();
    }
}

// Event listeners
document.addEventListener('DOMContentLoaded', () => {
    const mobileMenuBtn = document.getElementById('mobileMenuButton');
    if (mobileMenuBtn) {
        mobileMenuBtn.addEventListener('click', toggleMobileMenu);
    }

    const notificationBell = document.querySelector('.notification-bell');
    if (notificationBell) {
        notificationBell.addEventListener('click', toggleNotifications);
    }

    const deleteAllBtn = document.querySelector('.mark-all-read');
    if (deleteAllBtn) {
        deleteAllBtn.textContent = 'Xóa tất cả Thông báo';
        deleteAllBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            deleteAllNotifications();
        });
    }

    loadNotificationCount();
    setInterval(loadNotificationCount, 30000);
});

// Close popups when clicking outside
document.addEventListener('click', function (event) {
    const popup = document.getElementById('popupContainer');
    const avatar = document.querySelector('header img[onclick="togglePopup()"]');
    const menu = document.getElementById('mobileVerticalMenu');
    const menuBtn = document.getElementById('mobileMenuButton');
    const notificationDropdown = document.querySelector('.notification-dropdown');
    const notificationBell = document.querySelector('.notification-bell');

    if (!popup.contains(event.target) && !avatar.contains(event.target)) {
        popup.style.display = 'none';
    }

    if (!menu.contains(event.target) && !menuBtn.contains(event.target)) {
        menu.style.animation = 'fadeIn 0.3s ease-out reverse';
        setTimeout(() => {
            menu.style.display = 'none';
        }, 250);
        menuBtn.classList.remove('active');
    }

    if (!notificationDropdown.contains(event.target) && !notificationBell.contains(event.target)) {
        notificationDropdown.classList.remove('show');
    }
});
