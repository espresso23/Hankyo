let character = {
    characterID: 0, characterName: "", diamond: 0
}
let paymentID = 0;
let paymentHistory = [{
    paymentID: 0,
    amount: 0,
    amountDiamonds: 0,
    description: "",
    date: ""
}];
async function fetchNewestPaymentID(){
    return $.ajax({
        url: 'http://localhost:8080/Hankyo/payment',
        type: 'GET',
        data: {action: 'getPaymentID'},
        dataType: 'json',
        xhrFields: {
            withCredentials: true
        },
        success: function (data) {
            paymentID = data
            console.log(paymentID)
        },
        error: function (error) {
            console.error('There has been a problem with your fetch operation:', error);
        }
    })
}
async function fetchCharacterData() {

    return $.ajax({
        url: 'http://localhost:8080/Struggle/payment',
        type: 'GET',
        data: {action: 'getCharacter'},
        dataType: 'json',
        xhrFields: {
            withCredentials: true
        },
        success: function (characterData) {
            character.characterID = characterData.characterID;
            character.characterName = characterData.characterName;
            character.diamond = characterData.diamond;
            console.log(character)
        },
        error: function (error) {
            console.error('There has been a problem with your fetch operation:', error);
        }
    });
}//ok
async function getDiamondItem() {//ở đây tạo một getCourse item và list toàn bộ danh sách course hiện tại
    return $.ajax({
        url: 'http://localhost:8080/Struggle/payment',
        type: 'GET',
        data: {action: 'getDiamondItem'},
        dataType: 'json',
        xhrFields: {
            withCredentials: true
        },
        success: function (data) {
            payObj = data.map((item) => {
                return {
                    id: item.id,
                    name: item.name,
                    diamonds: item.diamondAmount,
                    amount: item.amount
                };
            });
            console.log(payObj);
        },
        error: function (error) {
            console.error('There has been a problem with your fetch operation:', error);
        }
    });
}


// Call the function to fetch data when the page loads
window.onload = fetchCharacterData;
window.onload = getDiamondItem;
window.onload = getPaymentHistory;
window.onload = fetchNewestPaymentID;
let payObj = [{
    id: "",
    name: "",
    diamonds: 0,
    amount: 0
}]

let MY_BANK = {
    BANK_ID: "ICB", ACCOUNT_NO: "105883049913"
}
let intervalID;
let isPaymentSuccessful = false;
document.addEventListener("DOMContentLoaded", async () => {
    await getDiamondItem();
    await getPaymentHistory();
    await fetchCharacterData();
    await fetchNewestPaymentID();
    const home = document.getElementById('home');
    home.href = `character?action=home&characterID=${character.characterID}`;
    const wallet = document.getElementById('walletAmount');
    wallet.innerHTML = `${character.diamond}`;
    console.log(character.diamond);
    const payInner = document.querySelector('.payment-inner');
    let payRenderUI = "";
    payObj.forEach((pay, index) => {
        payRenderUI += `<div class="payment-item">
    <img src="img/diamond.png" alt="diamond">
    <p class="diamond-amount">${pay.name}</p>
    <p class="diamond-amount">Price: ${pay.amount} VND</p>
    <a class="payment-button">Buy Now</a>
  </div>`
    })
    payInner.innerHTML = payRenderUI;
//---------------------------------------------------------------------------------------
    const btns = document.querySelectorAll('.payment-button');
    const overlay = document.querySelector('.payment-overlay');
    const closeButton = document.querySelector('.close-btn');
    const pay_content = document.getElementById('pay_content');
    const pay_amount = document.getElementById('pay_amount');
    const pay_qr = document.querySelector('.pay-qr');
    const paymentHistoryTab = document.querySelector('.payment-history');
    const paymentQR = document.querySelector('.payment-qr');
    const paymentHistoryTable = document.querySelector('.payment-history-table');
    btns.forEach((item, index) => {
        item.addEventListener("click", () => {
            paymentQR.style.display = 'block';
            paymentHistoryTable.style.display = 'none';
            const payContent = `${payObj[index].id}${character.characterID}`
            const payAmount = payObj[index].amount
            let QR = `https://img.vietqr.io/image/${MY_BANK.BANK_ID}-${MY_BANK.ACCOUNT_NO}-compact.png?amount=${payAmount}&addInfo=${payContent}`;
            pay_qr.src = QR;
            pay_content.innerHTML = payContent;//1 can change by characterID or characterName who buy it
            pay_amount.innerHTML = payAmount;
            overlay.style.display = 'flex';
            //---------------------------------------------------------------------
            startCountdown();
            if (!intervalID) {
                setTimeout(() => {
                    intervalID = setInterval(() => {
                        checkPaid(payContent, payAmount);
                    }, 1500);
                }, 15000);
            }
        });//code trên dùng để load ra mã QR
    })
    closeButton.addEventListener('click', function () {
        overlay.style.display = 'none';
        clearInterval(countdownInterval);
        clearInterval(intervalID);
        resetCheckPaid();
    });

    // Đóng overlay khi nhấn ra ngoài phần QR
    overlay.addEventListener('click', function (event) {
        if (event.target === overlay) {
            overlay.style.display = 'none';
            clearInterval(countdownInterval);
            clearInterval(intervalID);
            resetCheckPaid();
        }
    });

    paymentHistoryTab.addEventListener('click', function () {
        displayTransactionHistory();
    });


});
let isSuccess = false;

function resetCheckPaid() {
    isSuccess = false;  // Đặt lại trạng thái để chuẩn bị cho lần thanh toán mới
}

let hasUpdated = false;  // Biến cờ để theo dõi trạng thái cập nhật

async function checkPaid(content, amount) {//Sau khi quét sẽ check trong api
    if (isPaymentSuccessful || hasUpdated || isSuccess) {
        console.log('Payment already processed. Stopping interval.');
        clearInterval(intervalID);
        return;  // Ngăn không cho kiểm tra nếu thanh toán đã thành công hoặc đã cập nhật
    }

    try {
        const response = await fetch("https://script.google.com/macros/s/AKfycbzH-_ZGMQFv8LEwdb6Cm-uHrjonKWtPO6ig11zU88_kms7RgBe61wCmA5p0KideqfQD/exec", {
            method: 'GET'
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        const lastPaid = data.data[data.data.length - 1];

        const lastPrice = lastPaid["Giá trị"];
        const lastContent = lastPaid["Mô tả"];
        const lastID = lastPaid["Mã GD"];
        console.log(`Kiểm tra thanh toán: Last Price: ${lastPrice}, Required Amount: ${amount}, Last Content: ${lastContent}`);

        // Kiểm tra điều kiện thanh toán
        if (lastPrice >= amount && lastContent.includes(content) && lastID !== paymentID) {
            console.log(`Điều kiện thanh toán thỏa mãn: ${lastPrice} >= ${amount} và "${lastContent}" bao gồm "${content}"`);

            const diamondInfo = payObj.find(item => item.amount === amount);
            if (diamondInfo) {
                const diamondAmount = diamondInfo.diamonds;
                console.log(`Số lượng kim cương: ${diamondAmount}`);

                // Gọi hàm updateCharacterAndPayment và chờ kết quả
                await updateCharacterAndPayment(character.characterID, diamondAmount, lastPrice, lastID, lastContent);// ở đây thông báo mua thành công và thể hiện trạng thái paid chi còn chờ học sinh enroll
                isPaymentSuccessful = true;
                hasUpdated = true;
                isSuccess = true;
                // Đánh dấu là đã cập nhật
                clearInterval(intervalID);  // Dừng interval
                const qrContainer = document.querySelector('.payment-qr'); // container chứa mã QR
                const successMessage = `
                        <div class="success-message">
                            <h1>Thanh toán thành công!</h1>
                            <p>Cảm ơn bạn đã thanh toán.</p>
                            <div class="goBack">
                                <div><a href="/Struggle/paymentpage">Mua thêm</a></div>
                                <div class="view-payment" onclick="displayTransactionHistory()">Xem lịch sử giao dịch</div>
                                <div><a href="character?action=home&characterID=${character.characterID}">Trở về trang chủ</a></div>
                            </div>
                        </div>`;
                qrContainer.innerHTML = successMessage;
                console.log("Thanh toán thành công và thông điệp đã được hiển thị.");

            } else {
                console.log("Không tìm thấy thông tin kim cương cho số tiền này.");
            }
        } else {
            console.log("Điều kiện thanh toán không thỏa mãn.");
        }
        console.log(data.data);
    } catch (error) {
        console.log("Error:", error.message);
    }
}


function startCountdown() {
    let countdownTime = 10 * 60; // 10 phút = 600 giây
    let totalTime = countdownTime; // Tổng thời gian để tính phần trăm
    let countdownElement = document.querySelector('.payment-time');
    let progressBar = document.querySelector('.progress');

    countdownInterval = setInterval(function () {
        countdownTime--;

        let minutes = Math.floor(countdownTime / 60);
        let seconds = countdownTime % 60;

        countdownElement.textContent = `${minutes < 10 ? '0' : ''}${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
        let progressPercent = (countdownTime / totalTime) * 100;
        progressBar.style.width = progressPercent + '%';

        if (countdownTime <= 0) {
            clearInterval(countdownInterval);
            countdownElement.textContent = "Hết thời gian";
            progressBar.style.width = '0%';
        }
    }, 1000);
}

async function updateCharacterAndPayment(characterID, diamondAmount, amount, paymentID, description) {
    try {
        const response = await fetch("/Struggle/payment", {
            method: 'POST', headers: {
                'Content-Type': 'application/json'  // Sử dụng JSON để dễ dàng xử lý
            }, body: JSON.stringify({
                characterID: characterID,
                diamondAmount: diamondAmount,
                description: description,
                amount: amount,
                paymentID: paymentID,
            })
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const result = await response.text();  // Nhận phản hồi
        console.log(result);  // Xử lý kết quả trả về
        await fetchCharacterData();
        const wallet = document.getElementById('walletAmount');
        wallet.innerHTML = `${character.diamond}`;
        // Cập nhật lại thông tin nhân vật

    } catch (error) {
        console.error('Error:', error);  // Xử lý lỗi
    }
}

function closeOverlay() {
    // Fetch the latest payment history and character data before closing the overlay
    Promise.all([getPaymentHistory(), fetchCharacterData()])
        .then(() => {
            document.querySelector('.payment-overlay').style.display = 'none';
        })
        .catch(error => {
            console.error('Error fetching data:', error);
            // Even if there's an error, close the overlay
            document.querySelector('.payment-overlay').style.display = 'none';
        });
}

async function displayTransactionHistory(currentPage = 1, rowsPerPage = 10) {
    const overlay = document.querySelector('.payment-overlay');
    const paymentHistoryTable = document.querySelector('.payment-history-table');
    const paymentQR = document.querySelector('.payment-qr');
    overlay.style.display = 'flex';
    paymentHistoryTable.style.display = 'block';
    paymentQR.style.display = 'none';

    // Clear the old content of the table before adding new
    let historyUI = document.getElementById("payment-history-data");
    historyUI.innerHTML = ""; // Reset old content

    // Calculate the start and end index for the current page
    const startIndex = (currentPage - 1) * rowsPerPage;
    const endIndex = startIndex + rowsPerPage;

    // Filter the payment history based on the current page
    const currentPageData = paymentHistory.slice(startIndex, endIndex);

    // Add header tags only once
    let historyRender = `
            <tr>
                <th>PaymentID</th>
                <th>Description</th>
                <th>Amount</th>
                <th>Diamond</th>
                <th>Date</th>
            </tr>`;

    // Loop through the currentPageData to add data rows
    currentPageData.forEach((item) => {
        historyRender += `
                <tr>
                    <td>${item.paymentID}</td>
                    <td>${item.description}</td>
                    <td>${item.amount}</td>
                    <td>${item.diamondAmount}</td>
                    <td>${item.date}</td>
                </tr>`;
    });

    // After creating the entire table content, add it to the table
    historyUI.innerHTML = historyRender;

    // Render pagination buttons
    renderPagination(currentPage, rowsPerPage);
}

function renderPagination(currentPage, rowsPerPage) {
    const paginationContainer = document.querySelector('.pagination');
    paginationContainer.innerHTML = ""; // Reset old content

    // Calculate the total number of pages
    const totalPages = Math.ceil(paymentHistory.length / rowsPerPage);

    // Render previous button
    if (currentPage > 1) {
        paginationContainer.innerHTML += `<button class="pagination-button" onclick="displayTransactionHistory(${currentPage - 1}, ${rowsPerPage})">Previous</button>`;
    }

    // Render page buttons
    for (let i = 1; i <= totalPages; i++) {
        if (i === currentPage) {
            paginationContainer.innerHTML += `<button class="pagination-button active" onclick="displayTransactionHistory(${i}, ${rowsPerPage})">${i}</button>`;
        } else {
            paginationContainer.innerHTML += `<button class="pagination-button" onclick="displayTransactionHistory(${i}, ${rowsPerPage})">${i}</button>`;
        }
    }

    // Render next button
    if (currentPage < totalPages) {
        paginationContainer.innerHTML += `<button class="pagination-button" onclick="displayTransactionHistory(${currentPage + 1}, ${rowsPerPage})">Next</button>`;
    }
}
async function getPaymentHistory() {
    try {
        const data = await $.ajax({
            url: '/Struggle/payment',
            type: 'GET',
            data: {action: 'getPaymentHistory'},
            dataType: 'json',
        });

        // Update the paymentHistory variable with the fetched data
        paymentHistory = data.map(payment => ({
            paymentID: payment.paymentID,
            amount: payment.amount,
            diamondAmount: payment.diamondAmount,
            description: payment.description,
            characterID: payment.characterID,
            date: payment.date // Ngày có thể định dạng ISO
        }));

        console.log(paymentHistory); // Log the updated payment history

    } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
    }
}