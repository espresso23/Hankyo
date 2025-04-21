function saveChanges() {
    const formData = new FormData(document.getElementById('updateForm'));
    
    fetch('${pageContext.request.contextPath}/updateProfile', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok) {
            return response.text();
        }
        throw new Error('Network response was not ok');
    })
    .then(avatarPath => {
        // Update avatar image immediately
        const avatarImg = document.querySelector('.avatar-img');
        if (avatarImg) {
            // Ensure forward slashes in the path
            const cleanPath = avatarPath.replace(/\\/g, '/');
            // Construct the full URL
            avatarImg.src = '${pageContext.request.contextPath}/' + cleanPath;
            // Force image reload by adding timestamp
            avatarImg.src = avatarImg.src + '?rand=' + new Date().getTime();
            console.log('Setting image src to:', avatarImg.src);
        }
        // Show success message
        alert('Profile updated successfully!');
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error updating profile: ' + error.message);
    });
} 