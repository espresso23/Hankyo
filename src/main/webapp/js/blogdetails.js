/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */



// Get elements
const moreOptionsBtn = document.getElementById("more-options");
const dropdownMenu = document.getElementById("dropdown-menu");

// Toggle dropdown visibility
moreOptionsBtn.addEventListener("click", function (event) {
    event.stopPropagation(); // Prevent event from bubbling up
    dropdownMenu.style.display = dropdownMenu.style.display === "block" ? "none" : "block";
});

// Hide dropdown when clicking outside
window.addEventListener("click", function (event) {
    if (dropdownMenu.style.display === "block" && !event.target.closest('.dropdown')) {
        dropdownMenu.style.display = "none"; // Hide the dropdown
    }
});

// Add edit and delete functionality
document.getElementById("editOption").addEventListener("click", function () {
    // Handle edit action (e.g., open a modal or redirect to an edit page)
    alert("Edit option clicked! Implement your edit functionality here.");
    // For example, you could open a modal or navigate to an edit page
    // window.location.href = '/editPost?id=' + postId; // Adjust postId as necessary
});

document.getElementById("deleteOption").addEventListener("click", function () {
    // Handle delete action (e.g., show confirmation dialog)
    const confirmDelete = confirm("Are you sure you want to delete this post?");
    if (confirmDelete) {
        // Proceed with deletion logic
        alert("Delete option clicked! Implement your delete functionality here.");
        // For example, you could submit a form to delete the post
        // document.getElementById('deleteForm').submit(); // Adjust form submission as necessary
    }
});

// Modal functionality
const modal = document.getElementById("myModal");
const btn = document.getElementById("openModalBtn");
const span = document.getElementsByClassName("close")[0];

btn.onclick = function () {
    modal.style.display = "block"; // Show modal
}

span.onclick = function () {
    modal.style.display = "none"; // Close modal
}

window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none"; // Close modal if clicked outside
    }
}

// Confirm deletion
function confirmDelete(postID) {
    const confirmAction = confirm("Are you sure you want to delete this post?");
    if (confirmAction) {
        // If confirmed, submit the form
        document.getElementById('postID').value = postID;
        document.getElementById('deletePost').submit();
    }
}

// Toggle dropdown visibility
document.getElementById("more-options").addEventListener("click", function (event) {
    event.stopPropagation(); // Prevent window click from firing
    const dropdown = document.getElementById("dropdown-menu");
    dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
});

// Hide dropdown when clicking outside
window.addEventListener("click", function (event) {
    const dropdown = document.getElementById("dropdown-menu");
    if (dropdown.style.display === "block" && !event.target.closest('.dropdown-container')) {
        dropdown.style.display = "none";
    }
});