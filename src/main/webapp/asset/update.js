function openOverlay(field, value) {
    document.getElementById("overlay").style.display = "block";

    // Ẩn tất cả các tab trước khi hiển thị cái cần thiết
    document.querySelectorAll(".overlay-tab").forEach(tab => tab.style.display = "none");

    if (field === "dateOfBirth") {
        document.getElementById("editTitle").innerText = "Chỉnh sửa ngày sinh";
        document.getElementById("date-tab").style.display = "block";
        document.getElementById("editDateInput").style.display = "block";
        document.getElementById("editDateInput").value = value;
    } else if (field === "password") {
        document.getElementById("editTitle").innerText = "Đổi mật khẩu";
        document.getElementById("password-tab").style.display = "block";
    } else if (field === "avatar") {
        document.getElementById("editTitle").innerText = "Chỉnh sửa ảnh đại diện";
        document.getElementById("avatar-tab").style.display = "block";
    } else {
        document.getElementById("editTitle").innerText = "Chỉnh sửa thông tin";
        document.getElementById("text-tab").style.display = "block";
        document.getElementById("editInput").value = value;
    }
    document.getElementById("editInput").setAttribute("data-field", field);
}


function closeOverlay() {
    document.getElementById("overlay").style.display = "none";
}

function saveChanges() {
    let field = document.getElementById("editInput").getAttribute("data-field");
    let newValue = document.getElementById("editInput").value.trim();
    let oldPassword = document.getElementById("oldPassword").value;
    let newPassword = document.getElementById("newPassword").value;
    let reNewPassword = document.getElementById("reNewPassword").value;
    let avatarFile = document.getElementById("editAvatar").files[0];
    let dateValue = document.getElementById("editDateInput") ? document.getElementById("editDateInput").value : "";
    if (field === "password") {
        if (!oldPassword || !newPassword || !reNewPassword) {
            alert("Vui lòng nhập đầy đủ thông tin mật khẩu!");
            return;
        }
        if (newPassword !== reNewPassword) {
            alert("Mật khẩu mới không khớp!");
            return;
        }
    } else if (field === "avatar") {
        if (!avatarFile) {
            alert("Vui lòng chọn ảnh đại diện!");
            return;
        }
    } else if (field === "dateOfBirth") {
        let dateValue = document.getElementById("editDateInput").value;
        if (!dateValue) {
            alert("Vui lòng chọn ngày sinh!");
            return;
        }

    } else {
        if (!newValue) {
            alert("Giá trị không được để trống!");
            return;
        }
    }

    let formData = new FormData();
    formData.append("action", "save");

    if (field === "password") {
        formData.append("oldPassword", oldPassword);
        formData.append("newPassword", newPassword);
        formData.append("reNewPassword", reNewPassword);
    } else if (field === "avatar") {
        formData.append("action", "updateAvatar");
        formData.append("avatar", avatarFile);
    } else {
        formData.append(field, newValue);
    }

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
                } else {
                    console.warn("Không tìm thấy phần tử với ID:", field);
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
