// Khởi tạo biến
let userID = null;
let fullName = null;
let socket = null;
let reconnectAttempts = 0;
const maxReconnectAttempts = 5;
let selectedImage = null;

// Khởi tạo khi trang load
$(document).ready(function() {
    // Lấy thông tin user
    userID = $("#userID").val();
    fullName = $("#fullName").val();
    
    console.log('Initial userID:', userID);
    console.log('Initial fullName:', fullName);
    
    // Kiểm tra và chuyển đổi userID thành số
    if (userID) {
        userID = parseInt(userID);
        if (isNaN(userID)) {
            console.error("Invalid userID format");
            return;
        }
    }
    
    if (!userID || !fullName) {
        console.error("Missing user information");
        return;
    }
    
    console.log('Validated userID:', userID);
    
    // Kết nối WebSocket
    connectToChat();
    
    // Khởi tạo emoji
    initEmoji();

    // Xử lý Enter key
    $('#message-input').on('keypress', function(e) {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            $('#message-form').submit();
        }
    });

    // Xử lý upload ảnh
    $('#image-upload').on('change', function(e) {
        const file = e.target.files[0];
        if (file) {
            if (file.size > 5 * 1024 * 1024) { // 5MB limit
                alert('Image size should be less than 5MB');
                return;
            }

            const reader = new FileReader();
            reader.onload = function(e) {
                const base64Data = e.target.result;
                if (base64Data.length > 2 * 1024 * 1024) { // 2MB base64 limit
                    alert('Image is too large. Please choose a smaller image.');
                    return;
                }
                selectedImage = base64Data;
                // Xóa preview cũ nếu có
                $('.image-preview').remove();
                
                // Tạo preview phía trên thanh chat
                const preview = $('<div class="image-preview" style="position: absolute; bottom: 100%; left: 50px; background: #fff; border: 1px solid #ddd; border-radius: 8px; padding: 15px; margin: 0 auto 10px; min-height: 200px; display: flex; align-items: center; justify-content: center; box-shadow: 0 2px 6px rgba(0,0,0,0.1);"><div style="position: relative; width: 100%; height: 100%; display: flex; justify-content: center; align-items: center;"><img src="' + selectedImage + '" alt="Preview" style="max-width: 100%; max-height: 400px; object-fit: contain;"><button onclick="removeImage()" style="position: absolute; top: -20px; right: -20px; background: #ff4444; color: white; border: none; border-radius: 50%; width: 30px; height: 30px; cursor: pointer; box-shadow: 0 2px 4px rgba(0,0,0,0.2); display: flex; align-items: center; justify-content: center; font-size: 18px;">×</button></div></div>');
                
                // Thêm preview vào form chat với position relative
                const messageForm = $('#message-form');
                messageForm.css('position', 'relative');
                messageForm.prepend(preview);
                
                // Cuộn xuống để hiển thị ảnh
                $('html, body').animate({
                    scrollTop: preview.offset().top
                }, 500);
            };
            reader.readAsDataURL(file);
        }
    });
});

function removeImage() {
    selectedImage = null;
    $('.image-preview').remove();
    $('#message-form').css('position', ''); // Reset position
    $('#image-upload').val('');
}

function initEmoji() {
    const emojis = ['😀', '😁', '😂', '😃', '😄', '😅', '😆', '😇', '😈', '😉', '😊', '😋', '😌', '😍', '😎', '😏'];
    const container = $('#emoji-container');
    
    emojis.forEach(emoji => {
        container.append(`<span class="emoji" onclick="insertEmoji('${emoji}')">${emoji}</span>`);
    });
}

function connectToChat() {
    try {
        if (!userID || typeof userID !== 'number') {
            console.error('Invalid userID for WebSocket connection:', userID);
            return;
        }
        
        // Tạo URL WebSocket
        const wsProtocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
        const wsUrl = `${wsProtocol}//${window.location.host}/Hankyo/chat/${userID}`;
        console.log('Connecting to WebSocket:', wsUrl);

        socket = new WebSocket(wsUrl);

        socket.onopen = function() {
            console.log('WebSocket connection opened for global chat. UserID:', userID);
            reconnectAttempts = 0;
        };

        socket.onmessage = function(event) {
            try {
                const response = JSON.parse(event.data);
                console.log('Received message:', response);

                if (response.error) {
                    console.error('Server error:', response.error);
                    alert('Error: ' + response.error);
                    return;
                }

                if (response.warning) {
                    console.warn('Server warning:', response.warning);
                    return;
                }

                if (response.message === "You are banned from chatting.") {
                    alert(response.message);
                    return;
                }
                
                displayMessage(response);
            } catch (error) {
                console.error('Error parsing message:', error);
            }
        };

        socket.onclose = function(event) {
            console.log('WebSocket connection closed. Code:', event.code, 'Reason:', event.reason);
            if (reconnectAttempts < maxReconnectAttempts) {
                reconnectAttempts++;
                console.log('Reconnecting... Attempt', reconnectAttempts, 'of', maxReconnectAttempts);
                setTimeout(connectToChat, 3000);
            } else {
                console.error('Max reconnection attempts reached');
                alert('Connection lost. Please refresh the page.');
            }
        };

        socket.onerror = function(error) {
            console.error('WebSocket error:', error);
        };
    } catch (error) {
        console.error('Error in connectToChat:', error);
    }
}

function displayMessage(content) {
    const messageContainer = $('#message-container ul');
    if (!messageContainer.length) {
        console.error('Message container not found');
        return;
    }

    // Check if message already exists (to prevent duplicates)
    const existingMessage = $(`li[data-message-id="${content.chatID}"]`);
    if (existingMessage.length > 0) {
        return; // Message already displayed
    }

    const censoredMessage = censorBadWords(content.message);
    const isMyMessage = content.userID === parseInt(userID);
    
    let imageHtml = '';
    if (content.pictureSend) {
        imageHtml = `<div class="message-image"><img src="${content.pictureSend}" alt="Sent image" style="max-width: 200px; max-height: 200px;"></div>`;
    }
    
    const messageHtml = `
        <li class="${isMyMessage ? 'my-message' : 'other-message'}" data-message-id="${content.chatID || Date.now()}">
            <span class="sender-name">${content.fullName}</span>
            <div class="message-content">
                <div class="message">
                    ${censoredMessage}
                    ${imageHtml}
                    <span class="timestamp-tooltip">${content.sendAt || new Date().toLocaleTimeString()}</span>
                </div>
                ${!isMyMessage ? `<button class="report-button" onclick="showReportForm('${content.chatID}', '${content.userID}')">Report</button>` : ''}
            </div>
        </li>
    `;

    messageContainer.append(messageHtml);
    $('#message-container').scrollTop($('#message-container')[0].scrollHeight);
}

// Xử lý form submit
$('#message-form').on('submit', function(e) {
    e.preventDefault();
    
    if (!socket || socket.readyState !== WebSocket.OPEN) {
        console.error('WebSocket is not connected');
        alert('Not connected to server. Please wait...');
        return;
    }

    const messageInput = $('#message-input').val().trim();
    if (!messageInput && !selectedImage) return;

    const censoredMessage = censorBadWords(messageInput);
    
    // Create message object with proper image handling
    const message = {
        userID: parseInt(userID),
        fullName: fullName,
        message: censoredMessage,
        sendAt: new Date().toLocaleTimeString(),
        chatID: Date.now()
    };

    if (selectedImage) {
        if (selectedImage.startsWith('http://') || selectedImage.startsWith('https://')) {
            message.pictureSend = selectedImage;
        } else if (selectedImage.startsWith('data:image')) {
            message.pictureSend = selectedImage;
        } else {
            console.error('Invalid image format');
            alert('Invalid image format');
            return;
        }
    }

    try {
        // Display the message immediately for the sender
        displayMessage(message);

        // Send message to server
        socket.send(JSON.stringify(message));

        // Clear input, image preview and reset image
        $('#message-input').val('');
        removeImage();
        
        // Cuộn xuống tin nhắn mới nhất
        const messageContainer = $('#message-container');
        messageContainer.animate({
            scrollTop: messageContainer[0].scrollHeight
        }, 500);
    } catch (error) {
        console.error('Error sending message:', error);
        alert('Error sending message. Please try again.');
    }
});

// Xử lý emoji
$('#emoji-button').on('click', function() {
    $('#emoji-container').toggle();
});

function insertEmoji(emoji) {
    const messageInput = $('#message-input');
    messageInput.val(messageInput.val() + emoji);
}

// Danh sách từ ngữ cần lọc
function censorBadWords(message) {
    const badWords = [
        "cc", "Cc", "CC", "cC",
        "vcl", "Vcl", "VCl", "vCl", "VCL",
        "cl", "CL",
        "dcm", "Dcm", "DcM", "DCM",
        "dcmm", "Dcmm", "DcMM", "DCMM", "dCmM",
        "dit", "Dit", "DIT", "diT", "dIt", "dIT", "DIt",
        "me", "Me", "ME", "mE",
        "lon", "Lon", "LON", "lOn", "lON", "loN",
        "cac", "Cac", "CAC", "CaC", "cAC", "caC",
        "địt", "Địt", "ĐỊT", "ĐỊt", "Địt",
        "mẹ", "Mẹ", "MẸ", "mẹ", "ME", "mE",
        "lồn", "Lồn", "LỒN", "Lồn", "LoN",
        "cặc", "Cặc", "CẶC", "CăC", "cẶC",
        "má", "Má", "MÁ",
        "đụ", "Đụ", "ĐỤ", "đỤ", "dỤ",
        "đéo", "Đéo", "ĐÉO", "Đéo", "ĐÉo",
        "chó", "Chó", "CHÓ", "C chó",
        "mả cha", "Mả cha", "Mả Cha", "MẢ CHA",
        "cmnr", "CMnr", "CMNR", "cmnR", "CmNr", "Cmnr",
        "chịch", "Chịch", "CHỊCH", "ChịCH", "cHịch",
        "CLGT", "Clgt", "clgt", "clGT", "CLgt",
        "CĐGT", "cđgt", "Cđgt", "Cdgt", "CDGT",
        "cdgt", "cDGT", "CdGt", "Cdgt", "CdgT",
        "fuck", "Fuck", "FUck", "FUCk", "FUCK", "fUCK", "fuCK",
        "cứt", "Cứt", "CỨT", "CứT",
        "cut", "Cut", "CUT", "cUt", "CuT",
        "cu", "CU", "Cu", "cU",
        "phò", "Phò", "PHò", "PHÒ", "pho", "Pho", "PHo", "pHo",
        "đệt", "Đệt", "ĐỆT", "ĐỆt",
        "dí", "Dí", "DÍ",
        "đĩ", "Đĩ", "ĐĨ",
        "chó đẻ", "Chó Đẻ", "CHÓ ĐẺ", "chó Đẻ",
        "khùng", "Khùng", "KHÙNG",
        "khung", "Khung", "KHUNG",
        "điên", "Điên", "ĐIÊN",
        "dien", "Dien", "DIEN",
        "diên", "Diên", "DIÊN",
        "mọi", "Mọi", "MỌI",
        "moi", "Moi", "MOI",
        "súc vật", "Súc Vật", "SÚC VẬT", "súc Vật",
        "suc vat", "Suc Vat", "SUC VAT", "suc VAT",
        "sv", "Sv", "SV",
        "svat", "Svat", "SVAT",
        "buoi", "Buoi", "BUOI",
        "buồi", "Buồi", "BUỒI",
        "xạo lồn", "Xạo Lồn", "XẠO LỒN",
        "xao lon", "Xao Lon", "XAO LON",
        "xạo cặc", "Xạo Cặc", "XẠO CẶC",
        "xao cac", "Xao Cac", "XAO CAC",
        "chết mẹ", "Chết Mẹ", "CHẾT MẸ",
        "chet me", "Chet Me", "CHET ME",
        "xạo lìn", "Xạo Lìn", "XẠO LÌN",
        "xao lin", "Xao Lin", "XAO LIN",
        "á đù", "Á Đù", "Á ĐÙ",
        "a du", "A Du", "A DU",
        "á du", "Á Du", "Á DU",
        "a đù", "A Đù", "A ĐÙ",
        "damn", "Damn", "DAMN",
        "ỉa", "Ỉa", "ỈA",
        "ia", "Ia", "IA",
        "đái", "Đái", "ĐÁI",
        "dái", "Dái", "DÁI",
        "bìu", "Bìu", "BÌU",
        "biu", "Biu", "BIU",
        "đis", "Đis", "ĐIS",
        "dis", "Dis", "DIS"
    ];

    let censoredMessage = message;
    badWords.forEach(function(badWord) {
        const regex = new RegExp("\\b" + badWord + "\\b", "gi");
        const replacement = "*".repeat(badWord.length);
        censoredMessage = censoredMessage.replace(regex, replacement);
    });

    return censoredMessage;
}

// Xử lý report message
function showReportForm(chatID, userID) {
    document.getElementById('report-chatID').value = chatID;
    document.getElementById('report-reporterID').value = userID;
    document.getElementById('report-reportedUserID').value = userID;
    document.getElementById('report-overlay').style.display = 'flex';
}

function closeReportForm() {
    document.getElementById('report-overlay').style.display = 'none';
    document.getElementById('report-form').reset();
}

function submitReport() {
    const chatID = document.getElementById('report-chatID').value;
    const userID = document.getElementById('report-reporterID').value;
    const reportType = document.getElementById('report-type').value;
    const description = document.getElementById('report-description').value;

    if (!reportType || !description) {
        alert('Please fill in all required fields');
        return;
    }

    const formData = new FormData();
    formData.append('chatID', chatID);
    formData.append('userID', userID);
    formData.append('reportType', reportType);
    formData.append('description', description);

    fetch('/Hankyo/ChatReportServlet', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        if (data.error) {
            alert(data.error);
        } else {
            alert('Report submitted successfully');
            closeReportForm();
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to submit report. Please try again.');
    });
}

function closeReportOverlay(chatID) {
    const overlay = $(`#report-chat-overlay-${chatID}`);
    if (overlay.length) {
        overlay.css('display', 'none');
    }
}

function showReportDetails(chatID, reportType) {
    const detailsSection = $(`#report-details-${chatID}`);
    if (detailsSection.length) {
        detailsSection.css('display', reportType ? 'block' : 'none');
    }
}

function ajaxReportSubmit(btn) {
    const button = $(btn);
    const reportForm = button.closest('form');
    const chatID = reportForm.find('input[name="chatID"]').val();
    const userID = reportForm.find('input[name="userID"]').val();
    const reportType = reportForm.find('select[name="reportType"]').val();
    const description = reportForm.find('textarea[name="description"]').val();

    $.ajax({
        type: 'POST',
        url: 'ChatReportServlet',
        data: {
            chatID: chatID,
            userID: userID,
            reportType: reportType,
            description: description
        },
        dataType: 'json',
        success: function(response) {
            if (response.message) {
                alert(response.message);
                closeReportOverlay(chatID);
                refreshChat();
            } else {
                alert('Submitted Successfully');
            }
        },
        error: function() {
            alert('Error submitting report');
        }
    });
}

function refreshChat() {
    // Implement refresh logic if needed
}

function closeChat() {
    window.location.href = '/Hankyo/home.jsp';
}