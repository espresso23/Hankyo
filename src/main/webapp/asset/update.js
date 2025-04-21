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
    const formData = new FormData();
    const field = document.getElementById("editInput").getAttribute("data-field");
    formData.append("action", "save");
    formData.append("field", field);

    let newValue = "";
    let dateValue = "";

    switch (field) {
        case "avatar":
            const fileInput = document.getElementById("editAvatar");
            if (fileInput.files.length > 0) {
                const file = fileInput.files[0];
                // Validate file type
                if (!file.type.match(/image\/(png|jpg|jpeg|gif|webp)/)) {
                    alert("Chỉ chấp nhận file ảnh (png, jpg, jpeg, gif, webp)");
                    return;
                }
                // Validate file size (max 5MB)
                if (file.size > 5 * 1024 * 1024) {
                    alert("Kích thước file quá lớn. Vui lòng chọn file nhỏ hơn 5MB");
                    return;
                }
                formData.append("avatar", file);
            } else {
                alert("Vui lòng chọn ảnh đại diện");
                return;
            }
            break;
        case "password":
            let oldPassword = document.getElementById("oldPassword").value;
            let newPassword = document.getElementById("newPassword").value;
            let reNewPassword = document.getElementById("reNewPassword").value;

            if (!oldPassword || !newPassword || !reNewPassword) {
                alert("Vui lòng nhập đầy đủ thông tin mật khẩu!");
                return;
            }

            formData.append("oldPassword", oldPassword);
            formData.append("newPassword", newPassword);
            formData.append("reNewPassword", reNewPassword);
            break;
        case "dateOfBirth":
            dateValue = document.getElementById("editDateInput").value;
            if (!dateValue) {
                alert("Vui lòng chọn ngày sinh!");
                return;
            }
            formData.append("dateOfBirth", dateValue);
            break;
        default:
            let inputId = "edit" + field.charAt(0).toUpperCase() + field.slice(1) + "Input";
            newValue = document.getElementById(inputId).value.trim();
            if (!newValue) {
                alert("Giá trị không được để trống!");
                return;
            }
            formData.append(field, newValue);
    }

    // Show loading indicator
    const overlay = document.getElementById("overlay");
    const loadingIndicator = document.createElement("div");
    loadingIndicator.className = "loading-indicator";
    loadingIndicator.innerHTML = "Đang cập nhật...";
    overlay.appendChild(loadingIndicator);

    // Send data to servlet
    fetch("update-profile", {
        method: "POST",
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(text || 'Network response was not ok');
            });
        }
        return response.text();
    })
    .then(data => {
        console.log("Cập nhật thành công:", data);

        if (field === "avatar") {
            // Update avatar image
            const avatarImg = document.querySelector('.profile-picture img');
            if (avatarImg) {
                // The server returns the full path including context path
                avatarImg.src = data + '?t=' + new Date().getTime();
                console.log('Setting avatar src to:', avatarImg.src);
                
                // Force image reload
                avatarImg.onerror = function() {
                    console.error('Failed to load image:', this.src);
                    // Fallback to default avatar
                    this.src = '${pageContext.request.contextPath}/asset/png/avatar/monkey.jpg';
                };

                // Update header avatar
                const headerAvatar = document.querySelector('header img[onclick="togglePopup()"]');
                if (headerAvatar) {
                    headerAvatar.src = data + '?t=' + new Date().getTime();
                    headerAvatar.onerror = function() {
                        this.src = '${pageContext.request.contextPath}/asset/png/avatar/monkey.jpg';
                    };
                }

                // Reload the page to update session
                window.location.reload();
            }
        } else if (field === "password") {
            // Clear password fields on success
            document.getElementById("oldPassword").value = "";
            document.getElementById("newPassword").value = "";
            document.getElementById("reNewPassword").value = "";
        } else {
            // Update the displayed value in the section
            const section = document.querySelector(`.section-content:has(button[onclick*="${field}"])`);
            if (section) {
                const valueDiv = section.querySelector('div');
                if (valueDiv) {
                    if (field === "dateOfBirth") {
                        valueDiv.textContent = dateValue;
                    } else if (field === "gmail") {
                        valueDiv.textContent = "Email: " + newValue;
                    } else if (field === "phone") {
                        valueDiv.textContent = newValue;
                    } else if (field === "name") {
                        valueDiv.textContent = newValue;
                    }
                }
            }
        }

        alert("Cập nhật thành công!");
        closeOverlay();
    })
    .catch(error => {
        console.error("Lỗi khi cập nhật:", error);
        alert("Có lỗi xảy ra: " + error.message);
    })
    .finally(() => {
        // Remove loading indicator
        if (loadingIndicator.parentNode) {
            loadingIndicator.parentNode.removeChild(loadingIndicator);
        }
    });
}

