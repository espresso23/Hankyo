# Kế Hoạch Tái Cấu Trúc MVC cho Dự Án Hankyo

## 1. Tái Cấu Trúc Package Structure

### Hiện tại:
```
src/main/java/
├── controller/
├── service/
├── dao/
├── model/
├── dto/
├── websocket/
└── ...
```

### Mục tiêu:
```
src/main/java/com/hankyo/
├── controller/
│   ├── servlet/
│   │   ├── admin/
│   │   ├── auth/
│   │   ├── course/
│   │   ├── expert/
│   │   └── learner/
│   └── websocket/
│       ├── ChatWebSocket.java
│       ├── ChatEncoder.java
│       └── ChatDecoder.java
├── service/
│   ├── impl/
│   │   ├── AdminServiceImpl.java
│   │   ├── CourseServiceImpl.java
│   │   └── UserServiceImpl.java
│   ├── AdminService.java
│   ├── CourseService.java
│   └── UserService.java
├── repository/
│   ├── impl/
│   │   ├── UserDAOImpl.java
│   │   └── CourseDAOImpl.java
│   ├── UserDAO.java
│   └── CourseDAO.java
├── model/
│   ├── entity/
│   │   ├── User.java
│   │   ├── Course.java
│   │   └── ...
│   └── dto/
│       ├── DashboardStatsDTO.java
│       └── ...
├── config/
│   ├── DatabaseConfig.java
│   └── SecurityConfig.java
├── exception/
│   ├── BusinessException.java
│   └── ValidationException.java
├── util/
├── filter/
└── constants/
```

## 2. Tái Cấu Trúc Webapp

### Hiện tại:
```
src/main/webapp/
├── *.jsp (lộn xộn)
├── admin/
├── asset/
└── ...
```

### Mục tiêu:
```
src/main/webapp/
├── WEB-INF/
│   ├── views/
│   │   ├── admin/
│   │   │   ├── dashboard.jsp
│   │   │   ├── users.jsp
│   │   │   └── courses.jsp
│   │   ├── auth/
│   │   │   ├── login.jsp
│   │   │   └── register.jsp
│   │   ├── course/
│   │   │   ├── list.jsp
│   │   │   └── detail.jsp
│   │   ├── expert/
│   │   │   ├── dashboard.jsp
│   │   │   └── courses.jsp
│   │   ├── learner/
│   │   │   ├── dashboard.jsp
│   │   │   └── courses.jsp
│   │   └── common/
│   │       ├── header.jsp
│   │       ├── footer.jsp
│   │       └── sidebar.jsp
│   ├── templates/
│   └── web.xml
├── static/
│   ├── css/
│   ├── js/
│   ├── images/
│   └── fonts/
└── uploads/
```

## 3. Các Bước Thực Hiện

### Bước 1: Tạo Package Structure Mới
1. Tạo các package mới theo cấu trúc MVC
2. Di chuyển các file vào đúng vị trí
3. Cập nhật import statements

### Bước 2: Tạo Interface Pattern
1. Tạo interface cho các Service
2. Tạo interface cho các DAO
3. Implement các interface

### Bước 3: Tái Cấu Trúc View
1. Tạo thư mục views trong WEB-INF
2. Di chuyển JSP files vào đúng module
3. Cập nhật servlet mapping

### Bước 4: Thêm Exception Handling
1. Tạo custom exceptions
2. Implement global exception handler
3. Cập nhật error pages

### Bước 5: Cập Nhật Configuration
1. Tạo configuration classes
2. Cập nhật web.xml
3. Thêm dependency injection

## 4. Lợi Ích Sau Khi Tái Cấu Trúc

1. **Tính Module Hóa**: Mỗi module có cấu trúc riêng biệt
2. **Dễ Bảo Trì**: Code được tổ chức theo chức năng
3. **Dễ Mở Rộng**: Thêm tính năng mới dễ dàng
4. **Dễ Test**: Có interface pattern
5. **Chuẩn MVC**: Tuân thủ đúng mô hình MVC
6. **Separation of Concerns**: Tách biệt rõ ràng các layer

## 5. Checklist Hoàn Thành

- [ ] Tạo package structure mới
- [ ] Di chuyển tất cả files
- [ ] Cập nhật import statements
- [ ] Tạo interface cho Service
- [ ] Tạo interface cho DAO
- [ ] Tái cấu trúc View layer
- [ ] Thêm exception handling
- [ ] Cập nhật configuration
- [ ] Test toàn bộ ứng dụng
- [ ] Cập nhật documentation 