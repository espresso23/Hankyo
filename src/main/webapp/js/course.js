const courseDetail = {
    c_id: 0,//id
    c_name: "",//name
    c_category: "",//category
    c_status: "",//status
    c_des: "",//description
    c_lastUpdate: "",//lastUpdate
    expert: {
        expertID: 0,
        fullName: "",
        gmail: "",
        phone: "",
        certificate: "",
        avatar: "",
        gender: "",
        honour: [],
    },
    numOfStudent: 0,
    rating: 0,
    c_img: "",
    price: 0,
    originalPrice: 0,
    ratingCount: 0,
    enrolled: false,
    c_content: []
}


async function fetchDetailCourse(courseId) {

    try {
        console.log('Bắt đầu lấy thông tin khóa học với ID:', courseId);

        const response = await $.ajax({
            url: 'course-details',
            type: 'GET',
            data: {
                courseID: courseId
            },
            dataType: 'json'
        });

        console.log('Dữ liệu nhận được từ server:', response);

        // Cập nhật đối tượng courseDetail với dữ liệu từ server
        courseDetail.c_id = response.c_id;
        courseDetail.c_name = response.c_name;
        courseDetail.c_category = response.c_category;
        courseDetail.c_status = response.c_status;
        courseDetail.c_des = response.c_des;
        courseDetail.c_lastUpdate = response.c_lastUpdate;
        courseDetail.c_img = response.c_img;
        courseDetail.price = response.price;
        courseDetail.originalPrice = response.originalPrice;
        courseDetail.numOfStudent = response.numOfStudent;
        courseDetail.rating = response.rating;
        courseDetail.ratingCount = response.ratingCount;
        courseDetail.enrolled = response.enrolled;

        // Cập nhật thông tin expert
        if (response.expert) {
            courseDetail.expert = {
                expertID: response.expert.expertID,
                fullName: response.expert.fullName,
                gmail: response.expert.gmail,
                phone: response.expert.phone,
                certificate: response.expert.certificate,
                avatar: response.expert.avatar,
                gender: response.expert.gender
            };
        }

        // Cập nhật nội dung khóa học
        if (response.c_content) {
            courseDetail.c_content = response.c_content;
        }

        console.log('Đối tượng courseDetail sau khi cập nhật:', courseDetail);

        // Cập nhật UI
        updateCourseUI();

        return courseDetail;
    } catch (error) {
        console.error('Lỗi khi tải thông tin khóa học:', error);
        $('#error-message')
            .text('Không thể tải thông tin khóa học. Vui lòng thử lại sau.')
            .show();
        throw error;
    }
}

function updateCourseUI() {
    console.log('Bắt đầu cập nhật giao diện');

    // Cập nhật thông tin cơ bản
    $('#course-title').text(courseDetail.c_name);
    $('#course-category').text(courseDetail.c_category);
    $('#course-description').text(courseDetail.c_des);
    // Ảnh khóa học
    if (courseDetail.c_img && courseDetail.c_img.trim() !== "") {
        $('#course-image').attr('src', courseDetail.c_img);
    } else {
        $('#course-image').attr('src', 'img/default-course.jpg'); // ảnh mặc định
    }

// Avatar giảng viên
    if (courseDetail.expert.avatar && courseDetail.expert.avatar.trim() !== "") {
        $('#expert-avatar').attr('src', courseDetail.expert.avatar);
    } else {
        $('#expert-avatar').attr('src', 'img/default-avatar.png'); // ảnh mặc định
    }


    // Cập nhật thông tin expert
    if (courseDetail.expert) {
        $('#expert-name').text(courseDetail.expert.fullName);
        $('#expert-certificate').text(courseDetail.expert.certificate);
        if (courseDetail.expert.avatar) {
            $('#expert-avatar').attr('src', courseDetail.expert.avatar);
        }
        $('#expert-contact').html(`
            <div>Email: ${courseDetail.expert.gmail}</div>
            <div>SĐT: ${courseDetail.expert.phone}</div>
        `);
    }

    // Cập nhật thông tin học viên và đánh giá
    $('#student-count').text(courseDetail.numOfStudent + ' học viên');
    $('#student-count-stats').text(courseDetail.numOfStudent + ' học viên');
    $('#rating').text(courseDetail.rating.toFixed(1));
    $('#rating-count').text('(' + courseDetail.ratingCount + ' đánh giá)');
    $('#rating-stats').text(courseDetail.rating.toFixed(1) + ' (' + courseDetail.ratingCount + ' đánh giá)');
    $('#last-update').text(formatDate(courseDetail.c_lastUpdate));

    // Cập nhật giá
    if (courseDetail.originalPrice > courseDetail.price) {
        $('#original-price').text(formatPrice(courseDetail.originalPrice));
        $('#current-price').text(formatPrice(courseDetail.price));
        $('#discount-badge').show();
    } else {
        $('#current-price').text(formatPrice(courseDetail.price));
        $('#original-price').hide();
        $('#discount-badge').hide();
    }

    // Cập nhật trạng thái đăng ký
    if (courseDetail.enrolled) {
        $('#enroll-button').text('Đã đăng ký').prop('disabled', true);
    } else {
        $('#enroll-button').text('Đăng ký ngay').prop('disabled', false);
    }

    console.log('Cập nhật giao diện hoàn tất');
}

function formatPrice(price) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(price);
}

function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('vi-VN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

function updateCourseContentUI() {
    console.log('Bắt đầu cập nhật giao diện nội dung khóa học');

    const contentContainer = $('#course-content');
    contentContainer.empty();

    if (!courseDetail.c_content || courseDetail.c_content.length === 0) {
        console.log('Không có nội dung khóa học');
        contentContainer.html('<p class="text-muted">Chưa có nội dung khóa học</p>');
        return;
    }

    courseDetail.c_content.forEach((content, index) => {
        console.log('Thêm nội dung:', content);

        const contentItem = `
            <div class="content-item">
                <h3>${index + 1}. ${content.title}</h3>
                <div class="content-media">
                    ${content.media}
                </div>
            </div>
        `;
        contentContainer.append(contentItem);
    });

    console.log('Cập nhật giao diện nội dung hoàn tất');
}

// Hàm khởi tạo
$(document).ready(function () {
    console.log('Trang đã sẵn sàng');

    const urlParams = new URLSearchParams(window.location.search);
    const courseId = urlParams.get('courseID');

    console.log('CourseID từ URL:', courseId);

    if (courseId) {
        fetchDetailCourse(courseId)
            .then(() => fetchCourseContent(courseId))
            .catch(error => {
                console.error('Lỗi khi tải dữ liệu khóa học:', error);
                $('#error-message')
                    .text('Không thể tải thông tin khóa học. Vui lòng thử lại sau.')
                    .show();
            });
    } else {
        console.error('Không tìm thấy courseID trong URL');
        $('#error-message')
            .text('Không tìm thấy thông tin khóa học. Vui lòng thử lại sau.')
            .show();
    }
});