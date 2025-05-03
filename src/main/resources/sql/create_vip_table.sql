-- Tạo bảng Vip
CREATE TABLE Vip (
    vipID INT IDENTITY(1,1) PRIMARY KEY,
    vipName NVARCHAR(100) NOT NULL,
    description NVARCHAR(500),
    price DECIMAL(10,2) NOT NULL,
    yearlyPrice DECIMAL(10,2) NOT NULL,
    createAt DATETIME DEFAULT GETDATE(),
    vipType VARCHAR(20) NOT NULL CHECK (vipType IN ('FREE', 'POPULAR', 'PREMIUM')),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE')),
    vip_img VARCHAR(255),
    features NVARCHAR(MAX), -- Lưu trữ danh sách tính năng dưới dạng JSON
    duration INT NOT NULL, -- Thời hạn tính bằng ngày
    isYearly BIT DEFAULT 0 -- 0: monthly, 1: yearly
);

-- Tạo bảng Vip_User để lưu thông tin người dùng đăng ký VIP
CREATE TABLE Vip_User (
    vipUserID INT IDENTITY(1,1) PRIMARY KEY,
    learnerID INT NOT NULL,
    vipID INT NOT NULL,
    startDate DATETIME DEFAULT GETDATE(),
    endDate DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'EXPIRED', 'CANCELLED')),
    paymentID VARCHAR(50),
    FOREIGN KEY (learnerID) REFERENCES Learner(learnerID),
    FOREIGN KEY (vipID) REFERENCES Vip(vipID)
);

-- Insert dữ liệu mẫu cho 3 gói VIP
INSERT INTO Vip (vipName, description, price, yearlyPrice, vipType, status, vip_img, features, duration, isYearly)
VALUES 
('Dùng thử miễn phí', 'Học thử miễn phí với các bài học cơ bản', 0, 0, 'FREE', 'ACTIVE', 'free_vip.png',
'["Truy cập các bài học cơ bản", "Thử nghiệm với các bài tập đơn giản"]', 30, 0),

('Phổ biến', 'Khóa học tiếng Hàn cơ bản và nâng cao', 70000, 300000, 'POPULAR', 'ACTIVE', 'popular_vip.png',
'["Truy cập đầy đủ bài học", "Được hỗ trợ giải đáp thắc mắc qua chat", "Các bài học nâng cao về ngữ pháp và từ vựng"]', 30, 0),

('Premium', 'Khóa học tiếng Hàn toàn diện cho mọi trình độ', 100000, 450000, 'PREMIUM', 'ACTIVE', 'premium_vip.png',
'["Truy cập tất cả bài học và bài tập", "Hỗ trợ học viên trực tuyến 24/7", "Giải đáp thắc mắc qua chat và email", "Tài liệu học mở rộng và đề thi mẫu", "Chứng chỉ hoàn thành khóa học"]', 30, 0); 