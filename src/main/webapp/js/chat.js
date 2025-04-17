// Khởi tạo biến
let userID = null;
let fullName = null;
let socket = null;
let reconnectAttempts = 0;
const maxReconnectAttempts = 5;

// Khởi tạo khi trang load
$(document).ready(function() {
    // Lấy thông tin user
    userID = $("#userID").val();
    fullName = $("#fullName").val();
    
    if (!userID || !fullName) {
        console.error("Missing user information");
        return;
    }
    
    // Kết nối WebSocket
    connectToChat();
    
    // Khởi tạo emoji
    initEmoji();
});

function initEmoji() {
    const emojis = ['😀', '😁', '😂', '😃', '😄', '😅', '😆', '😇', '😈', '😉', '😊', '😋', '😌', '😍', '😎', '😏'];
    const container = $('#emoji-container');
    
    emojis.forEach(emoji => {
        container.append(`<span class="emoji" onclick="insertEmoji('${emoji}')">${emoji}</span>`);
    });
}

function connectToChat() {
    try {
        // Tạo URL WebSocket
        const wsProtocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
        const wsUrl = `${wsProtocol}//${window.location.host}/Hankyo/chat/${userID}`;
        console.log('Connecting to WebSocket:', wsUrl);

        socket = new WebSocket(wsUrl);

        socket.onopen = function() {
            console.log('WebSocket connection opened for global chat.');
            reconnectAttempts = 0;
        };

        socket.onmessage = function(event) {
            try {
                const response = JSON.parse(event.data);
                console.log('Received message:', response);

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
            console.log('WebSocket connection closed. Reconnecting...');
            if (reconnectAttempts < maxReconnectAttempts) {
                reconnectAttempts++;
                setTimeout(connectToChat, 3000);
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

    const censoredMessage = censorBadWords(content.message);
    const isMyMessage = content.userID === userID;
    
    const messageHtml = `
        <li class="${isMyMessage ? 'my-message' : 'other-message'}">
            <div class="message-wrapper">
                <span class="sender-name">${content.fullName}</span>
                <div class="message">
                    ${censoredMessage}
                    <span class="timestamp-tooltip">${content.timestamp || new Date().toLocaleTimeString()}</span>
                </div>
                ${!isMyMessage ? `<button onclick="showReportForm(${content.chatID})" class="report-button" style="display: block;">Report</button>` : ''}
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
        return;
    }

    const messageInput = $('#message-input').val().trim();
    if (!messageInput) return;

    const censoredMessage = censorBadWords(messageInput);
    
    const message = {
        userID: userID,
        fullName: fullName,
        message: censoredMessage,
        timestamp: new Date().toISOString()
    };

    // Hiển thị message ngay lập tức
    displayMessage({
        ...message,
        chatID: Date.now() // Tạm thời sử dụng timestamp làm chatID
    });

    // Gửi message qua WebSocket
    socket.send(JSON.stringify(message));

    // Xóa input
    $('#message-input').val('');
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
function showReportForm(chatID) {
    document.getElementById('report-chatID').value = chatID;
    document.getElementById('report-overlay').style.display = 'flex';
}

function closeReportForm() {
    document.getElementById('report-overlay').style.display = 'none';
    document.getElementById('report-form').reset();
}

function submitReport() {
    const chatID = document.getElementById('report-chatID').value;
    const userID = document.getElementById('report-userID').value;
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
    window.location.href = '/Hankyo/character?action=home&userID=' + userID;
}