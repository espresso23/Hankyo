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
    fetch('/notifications')
        .then(response => response.json())
        .then(data => {
            updateNotificationCount(data.length);
            renderNotifications(data);
        })
        .catch(error => console.error('Error loading notifications:', error));
}

function loadNotificationCount() {
    fetch('/notifications?action=count')
        .then(response => response.json())
        .then(count => {
            updateNotificationCount(count);
        })
        .catch(error => console.error('Error loading notification count:', error));
}

function updateNotificationCount(count) {
    const countElement = document.getElementById('notificationCount');
    countElement.textContent = count;
    countElement.style.display = count > 0 ? 'block' : 'none';
}

function renderNotifications(notifications) {
    const notificationList = document.getElementById('notificationList');

    if (notifications.length === 0) {
        notificationList.innerHTML = '<div class="empty-notifications">Không có thông báo mới</div>';
        return;
    }

    notificationList.innerHTML = notifications.map(notification => `
    <div class="notification-item ${notification.read ? '' : 'unread'}" 
         onclick="markAsRead(${notification.significationID}, this)">
      <div class="notification-content">${notification.content}</div>
      <div class="notification-time">${formatTime(notification.createdAt)}</div>
    </div>
  `).join('');
}

function formatTime(timestamp) {
    const date = new Date(timestamp);
    return date.toLocaleString();
}

function markAsRead(significationID, element) {
    fetch('/notifications', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `action=markRead&significationID=${significationID}`
    })
        .then(response => response.json())
        .then(() => {
            element.classList.remove('unread');
            loadNotificationCount();
        })
        .catch(error => console.error('Error marking notification as read:', error));
}

function markAllAsRead() {
    fetch('/notifications?action=markAllRead', {
        method: 'POST'
    })
        .then(response => response.json())
        .then(() => {
            loadNotifications();
            loadNotificationCount();
        })
        .catch(error => console.error('Error marking all notifications as read:', error));
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
});

// Close popups when clicking outside
document.addEventListener('click', function(event) {
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