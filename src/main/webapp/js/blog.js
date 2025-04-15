/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

var modal = document.getElementById("myModal");
var btn = document.getElementById("openModalBtn");
var span = document.getElementsByClassName("close")[0];

btn.onclick = function () {
    modal.style.display = "block"; // Make sure modal is displayed
}

span.onclick = function () {
    modal.style.display = "none"; // Close modal on close button click
}

window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none"; // Close modal if clicked outside
    }
}


document.addEventListener("DOMContentLoaded", function () {
    let loadMoreButton = document.getElementById("loadMore");
    let posts = document.querySelectorAll(".post-row .card-post");
    let currentPosts = 6;

    for (let i = currentPosts; i < posts.length; i++) {
        posts[i].style.display = "none";
    }

    loadMoreButton.addEventListener("click", function (e) {
        e.preventDefault();
        let nextPosts = currentPosts + 6;
        for (let i = currentPosts; i < nextPosts && i < posts.length; i++) {
            posts[i].style.display = "flex";
        }
        currentPosts += 6;

        if (currentPosts >= posts.length) {
            loadMoreButton.style.display = "none";
        }
    });
});