Dưới đây là tổng hợp các câu lệnh git phổ biến:

### Cấu hình Git
- `git config --global user.name "Tên của bạn"`: Cấu hình tên người dùng.
- `git config --global user.email "email@example.com"`: Cấu hình email người dùng.

### Khởi tạo và sao chép kho
- `git init`: Khởi tạo một kho git mới.
- `git clone <URL>`: Sao chép kho git từ URL.

### Quản lý tệp
- `git add <tên_tệp>`: Thêm tệp vào staging area.
- `git add .`: Thêm tất cả các tệp thay đổi vào staging area.
- `git rm <tên_tệp>`: Xóa tệp khỏi thư mục làm việc và staging area.
- `git mv <tên_cũ> <tên_mới>`: Đổi tên hoặc di chuyển tệp.

### Xác nhận thay đổi (Commit)
- `git commit -m "Thông điệp commit"`: Tạo một commit với thông điệp.
- `git commit --amend -m "Thông điệp mới"`: Sửa đổi commit gần nhất với thông điệp mới.

### Quản lý nhánh (Branch)
- `git branch`: Liệt kê các nhánh hiện có.
- `git branch <tên_nhánh>`: Tạo một nhánh mới.
- `git checkout <tên_nhánh>`: Chuyển đổi nhánh.
- `git merge <tên_nhánh>`: Gộp nhánh vào nhánh hiện tại.
- `git branch -d <tên_nhánh>`: Xóa nhánh.

### Theo dõi thay đổi (Tracking Changes)
- `git status`: Kiểm tra trạng thái kho hiện tại.
- `git diff`: So sánh các thay đổi trong thư mục làm việc với kho git.
- `git log`: Xem lịch sử commit.

### Làm việc với kho từ xa (Remote Repositories)
- `git remote add <tên_remote> <URL>`: Thêm một remote repository.
- `git fetch <tên_remote>`: Tải các thay đổi từ remote repository.
- `git pull <tên_remote> <nhánh>`: Kéo các thay đổi từ remote repository và hợp nhất với nhánh hiện tại.
- `git push <tên_remote> <nhánh>`: Đẩy các commit lên remote repository.
