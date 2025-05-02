document.addEventListener('DOMContentLoaded', function() {
    const sidebarToggle = document.getElementById('sidebarToggle');
    const courseSidebar = document.getElementById('courseSidebar');
    const courseContent = document.getElementById('courseContent');
    
    // Lưu trạng thái sidebar vào localStorage
    const sidebarState = localStorage.getItem('sidebarState');
    if (sidebarState === 'hidden') {
        courseSidebar.classList.add('sidebar-hidden');
        courseContent.classList.add('full-width');
        sidebarToggle.classList.add('rotated');
    }

    if (sidebarToggle && courseSidebar && courseContent) {
        sidebarToggle.addEventListener('click', function() {
            courseSidebar.classList.toggle('sidebar-hidden');
            courseContent.classList.toggle('full-width');
            this.classList.toggle('rotated');
            
            // Lưu trạng thái mới
            const isHidden = courseSidebar.classList.contains('sidebar-hidden');
            localStorage.setItem('sidebarState', isHidden ? 'hidden' : 'visible');
            
            // Toggle icon
            const icon = this.querySelector('i');
            if (icon) {
                icon.classList.toggle('fa-chevron-right');
                icon.classList.toggle('fa-chevron-left');
            }
        });

        // Xử lý responsive
        function handleResize() {
            const isMobile = window.innerWidth < 768;
            
            if (isMobile) {
                courseSidebar.classList.remove('sidebar-hidden');
                courseSidebar.classList.add('sidebar-visible');
                courseContent.classList.add('full-width');
                sidebarToggle.classList.remove('rotated');
            } else {
                courseSidebar.classList.remove('sidebar-visible');
                // Khôi phục trạng thái từ localStorage khi quay lại desktop
                const sidebarState = localStorage.getItem('sidebarState');
                if (sidebarState === 'hidden') {
                    courseSidebar.classList.add('sidebar-hidden');
                    courseContent.classList.add('full-width');
                    sidebarToggle.classList.add('rotated');
                } else {
                    courseSidebar.classList.remove('sidebar-hidden');
                    courseContent.classList.remove('full-width');
                    sidebarToggle.classList.remove('rotated');
                }
            }
        }

        // Xử lý scroll
        let lastScrollTop = 0;
        document.addEventListener('scroll', function() {
            const st = window.pageYOffset || document.documentElement.scrollTop;
            if (st > lastScrollTop && window.innerWidth < 768) {
                // Scrolling down on mobile - hide sidebar
                courseSidebar.classList.remove('sidebar-visible');
            }
            lastScrollTop = st <= 0 ? 0 : st;
        }, false);

        // Gọi hàm khi resize
        window.addEventListener('resize', handleResize);
        handleResize(); // Gọi lần đầu khi load trang
    }
}); 