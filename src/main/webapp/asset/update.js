function openOverlay(field, value) {
    document.getElementById("overlay").style.display = "block";

    // Ẩn tất cả các tab trước khi hiển thị tab cần chỉnh sửa
    document.querySelectorAll(".overlay-tab").forEach(tab => tab.style.display = "none");
    document.getElementById("editTitle").innerText = "Chỉnh sửa thông tin";

    if (field === "dateOfBirth") {
        document.getElementById("editTitle").innerText = "Chỉnh sửa ngày sinh";
        document.getElementById("date-tab").style.display = "block";
        //document.getElementById("editDateInput").value = value;
    }
    else if (field === "password") {
        document.getElementById("editTitle").innerText = "Đổi mật khẩu";
        document.getElementById("password-tab").style.display = "block";
    }
    else if (field === "avatar") {
        document.getElementById("editTitle").innerText = "Chỉnh sửa ảnh đại diện";
        document.getElementById("avatar-tab").style.display = "block";
    }
    else if (field === "name") {
        document.getElementById("editTitle").innerText = "Chỉnh sửa tên";
        document.getElementById("name-tab").style.display = "block";
        //document.getElementById("editNameInput").value = value;
    }
    else if (field === "gmail") {
        document.getElementById("editTitle").innerText = "Chỉnh sửa Gmail";
        document.getElementById("gmail-tab").style.display = "block";
        //document.getElementById("editGmailInput").value = value;
    }
    else if (field === "phone") {
        document.getElementById("editTitle").innerText = "Chỉnh sửa số điện thoại";
        document.getElementById("phone-tab").style.display = "block";
        //document.getElementById("editPhoneInput").value = value;
    }

    document.getElementById("editInput").setAttribute("data-field", field);
}

function closeOverlay() {
    document.getElementById("overlay").style.display = "none";
}

function saveChanges() {
    let field = document.getElementById("editInput").getAttribute("data-field");
    let formData = new FormData();
    formData.append("action", "save");

    if (field === "password") {
        let oldPassword = document.getElementById("oldPassword").value;
        let newPassword = document.getElementById("newPassword").value;
        let reNewPassword = document.getElementById("reNewPassword").value;

        if (!oldPassword || !newPassword || !reNewPassword) {
            alert("Vui lòng nhập đầy đủ thông tin mật khẩu!");
            return;
        }
        if (newPassword !== reNewPassword) {
            alert("Mật khẩu mới không khớp!");
            return;
        }

        formData.append("oldPassword", oldPassword);
        formData.append("newPassword", newPassword);
    }
    else if (field === "avatar") {
        let avatarFile = document.getElementById("editAvatar").files[0];
        if (!avatarFile) {
            alert("Vui lòng chọn ảnh đại diện!");
            return;
        }
        formData.append("avatar", avatarFile);
    }
    else if (field === "dateOfBirth") {
        let dateValue = document.getElementById("editDateInput").value;
        if (!dateValue) {
            alert("Vui lòng chọn ngày sinh!");
            return;
        }
        formData.append("dateOfBirth", dateValue);
    }
    else {
        let newValue = document.getElementById("editInput").value.trim();
        if (!newValue) {
            alert("Giá trị không được để trống!");
            return;
        }
        formData.append(field, newValue);
    }

    // Gửi dữ liệu đến servlet
    fetch("update", {
        method: "POST",
        body: formData
    })
        .then(response => response.text())
        .then(data => {
            console.log("Cập nhật thành công:", data);

            if (field !== "password" && field !== "avatar") {
                let element = document.getElementById(field);
                if (element) {
                    element.textContent = field + ": " + (field === "dateOfBirth" ? dateValue : newValue);
                }
            }

            alert("Cập nhật thành công!");
            closeOverlay();
        })
        .catch(error => {
            console.error("Lỗi khi cập nhật:", error);
            alert("Có lỗi xảy ra, vui lòng thử lại!");
        });
}

