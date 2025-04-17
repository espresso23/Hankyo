<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@ page import="model.Comment" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
  User user = (User) session.getAttribute("loggedUser");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Hankyo Forum</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
  <link rel="stylesheet" href="asset/font/themify-icons/themify-icons.css">
  <link rel="stylesheet" href="asset/css/style.css">
  <link rel="stylesheet" href="asset/css/blog.css">
  <link rel="stylesheet" href="asset/css/modal.css">
  <link rel="stylesheet" href="asset/css/blogdetails.css">
  <script src="./js/blogdetails.js"></script>
</head>
<body>
<!-- HEADER -->
<c:import url="header.jsp"/>
<!-- PAGE INFO -->
<div id="page-info">
  <div class="page-title">Hankyo Forum</div>
</div>

<!-- BLOG DETAILS -->
<div id="blog-details">
  <c:if test="${not empty post}">
    <div class="blog-details-title">
      <h2>${post.heading}</h2>
      <c:if test="${user != null && user.userID == post.userID}">
        <div class="dropdown-container">
          <i class="ti-more-alt" id="more-options"></i>
          <div class="dropdown-menu" id="dropdown-menu" style="display:none;">
            <a id="openModalBtn"><i class="ti-pencil"> Edit</i></a>
            <a href="#" id="delete" onclick="confirmDelete(${post.getPostID()})"><i class="ti-trash"> Delete</i></a>
          </div>
        </div>
        <!-- Hidden form for deletion -->
        <form id="deleteFormPost" action="postDetails" method="POST" style="display: none;">
          <input type="hidden" name="action" value="deletePost">
          <input type="hidden" name="postID" id="postID" value="${post.getPostID()}">
        </form>
      </c:if>
    </div>
    <div class="blog-details-author">
      <img src="${avatar}" alt="Author Avatar" onerror="this.onerror=null;this.src='https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg';" class="details-avatar">
      <p class="details-name">${fullName}</p>
      <div class="blog-details-date">${post.createdDate}</div>
    </div>
    <div class="blog-content">
      <img src="${post.imgURL}" alt="Post Image" class="blog-content-img">
      <p>${post.content}</p>
    </div>
  </c:if>
  <c:if test="${empty post}">
    <p>This post is not available.</p>
  </c:if>
</div>

<!-- COMMENT SECTION -->
<div class="comment-container">
  <h2 style="color: #333333; padding: 20px;">Comment Section</h2>
  <c:choose>
    <c:when test="${user != null}">
      <form action="postDetails" method="POST">
        <div class="comment-box">
          <textarea name="commentInput" rows="4" required></textarea><br><br>
          <input type="hidden" name="postID" value="${post.postID}">
          <input type="hidden" name="action" value="addComment">
          <input type="submit" value="Add Comment" class="btn btn-primary">
        </div>
      </form>
    </c:when>
    <c:otherwise>
      <div class="login-to-comment">
        <p>Please <a href="login.jsp">login</a> to add a comment.</p>
      </div>
    </c:otherwise>
  </c:choose>

  <!-- Display Comments Section -->
  <div class="comments-display" id="commentsDisplay">
    <c:if test="${not empty comments}">
      <c:forEach var="comment" items="${comments}">
        <div class="comment-section" id="comment-${comment.commentID}">
          <img src="${comment.userAvatar}" alt="Avatar" class="comment-avt"
               onerror="this.onerror=null;this.src='https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg';">
          <div class="comment-content">
            <div>
              <div class="comment-author">${comment.userFullName}</div>
              <div class="comment-time">${comment.createdDate}</div>
            </div>
            <div class="comment">${comment.content}</div>
          </div>
          <c:if test="${user != null && (user.userID == post.userID || user.userID == comment.userID)}">
            <div class="comment-options">
              <c:if test="${user.userID == comment.userID}">
                <a href="javascript:void(0);" class="edit-comment-option"
                   onclick="openEditCommentModal(${comment.commentID}, '${comment.content}')">
                  <i class="ti-pencil"> Edit</i>
                </a>
              </c:if>
              <a href="javascript:void(0);" class="delete-comment-option"
                 onclick="confirmDeleteComment(${comment.commentID})">
                <i class="ti-trash"> Delete</i>
              </a>
            </div>
          </c:if>
        </div>
      </c:forEach>
    </c:if>
    <c:if test="${empty comments}">
      <div class="no-comments">No comments yet. Be the first to comment!</div>
    </c:if>
  </div>
</div>

<!-- Edit Comment Modal -->
<div id="edit-comment-modal" class="edit-comment-modal modal" style="display:none;">
  <div class="modal-content modal-content-comment">
    <span class="close-modal">&times;</span>
    <form id="edit-comment-form" action="postDetails" method="POST">
      <h2>Edit Comment</h2>
      <textarea id="edit-comment-content" name="commentContent" rows="4" required></textarea><br><br>
      <input type="hidden" name="postID" value="${post.postID}">
      <input type="hidden" id="edit-comment-id" name="commentID">
      <input type="hidden" name="action" value="editComment">
      <button type="submit" class="btn btn-primary">Submit</button>
    </form>
  </div>
</div>

<!-- Edit Post Modal -->
<div id="myModal" class="modal">
  <div class="modal-content">
    <span class="close">&times;</span>
    <form action="postDetails" method="POST" enctype="multipart/form-data">
      <h2>Edit Post</h2>
      <label for="imgPost">Picture Cover:</label>
      <input id="imgPost" name="imgPost" type="file" class="input-field"/><br>
      <p class="form-note">Leave empty to keep the current image</p>

      <label for="title">Heading:</label>
      <input id="title" name="title" type="text" value="${post.heading}" required class="input-field"/><br>

      <label for="default">Description:</label>
      <textarea id="default" name="description">${post.content}</textarea><br>

      <input type="hidden" name="postID" value="${post.postID}">
      <input type="hidden" name="action" value="editPost">
      <button type="submit" class="submit-button">Update Post</button>
    </form>
  </div>
</div>

<!-- AD BLOG -->

<!-- FOOTER -->
<c:import url="footer.jsp"/>

<script>
  const modal = document.getElementById("myModal");
  const btn = document.getElementById("openModalBtn");
  const span = document.getElementsByClassName("close")[0];

  btn.onclick = function () {
    modal.style.display = "block";
  }

  span.onclick = function () {
    modal.style.display = "none";
  }

  window.onclick = function (event) {
    if (event.target == modal) {
      modal.style.display = "none";
    }
  }

  function confirmDelete(postID) {
    const confirmAction = confirm("Are you sure you want to delete this post?");
    if (confirmAction) {
      document.getElementById('postID').value = postID;
      document.getElementById('deleteFormPost').submit();
    }
  }

  document.getElementById("more-options").addEventListener("click", function (event) {
    event.stopPropagation(); // Prevent window click from firing
    const dropdown = document.getElementById("dropdown-menu");
    dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
  });

  window.addEventListener("click", function (event) {
    const dropdown = document.getElementById("dropdown-menu");
    if (dropdown.style.display === "block" && !event.target.closest('.dropdown-container')) {
      dropdown.style.display = "none";
    }
  });

  //Comment section
  function openEditCommentModal(commentID, commentContent) {
    document.getElementById("edit-comment-content").value = commentContent;
    document.getElementById("edit-comment-id").value = commentID;
    document.getElementById("edit-comment-modal").style.display = "block";
  }

  document.querySelector(".close-modal").onclick = function () {
    document.getElementById("edit-comment-modal").style.display = "none";
  }

  function confirmDeleteComment(commentID) {
    console.log(`Attempting to delete comment with ID: ${commentID}`);
    if (confirm("Are you sure you want to delete this comment?")) {
      const deleteForm = document.createElement("form");
      deleteForm.method = "POST";
      deleteForm.action = "postDetails";

      const postIdField = document.createElement("input");
      postIdField.type = "hidden";
      postIdField.name = "postID";
      postIdField.value = document.getElementsByName("postID")[0].value;

      const commentIdField = document.createElement("input");
      commentIdField.type = "hidden";
      commentIdField.name = "commentID";
      commentIdField.value = commentID;

      const actionField = document.createElement("input");
      actionField.type = "hidden";
      actionField.name = "action";
      actionField.value = "deleteComment";

      deleteForm.appendChild(postIdField);
      deleteForm.appendChild(commentIdField);
      deleteForm.appendChild(actionField);

      document.body.appendChild(deleteForm);
      deleteForm.submit();
    }
  }
</script>
<script>
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
</script>
<script>// Get elements
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
});</script>
<script src="./asset/tinymce/tinymce.min.js"></script>
<script>
  tinymce.init({
    selector: 'textarea#default',
    width: '100%',
    height: 300,
    plugins: [
      'advlist', 'autolink', 'link', 'image', 'lists', 'charmap', 'prewiew', 'anchor', 'pagebreak',
      'searchreplace', 'wordcount', 'visualblocks', 'code', 'fullscreen', 'insertdatetime', 'media',
      'table', 'emoticons', 'template', 'codesample'
    ],
    toolbar: 'undo redo | styles | bold italic underline | alignleft aligncenter alignright alignjustify |' +
            'bullist numlist outdent indent | link image | print preview media fullscreen | ' +
            'forecolor backcolor emoticons',
    menu: {
      favs: { title: 'menu', items: 'code visualaid | searchreplace | emoticons' }
    },
    menubar: 'favs file edit view insert format tools table',
    file_picker_types: 'image',
    /* and here's our custom image picker*/
    file_picker_callback: (cb, value, meta) => {
      const input = document.createElement('input');
      input.setAttribute('type', 'file');
      input.setAttribute('accept', 'image/*');

      input.addEventListener('change', (e) => {
        const file = e.target.files[0];
        const reader = new FileReader();
        reader.addEventListener('load', () => {
          const id = 'blobid' + (new Date()).getTime();
          const blobCache = tinymce.activeEditor.editorUpload.blobCache;
          const base64 = reader.result.split(',')[1];
          const blobInfo = blobCache.create(id, file, base64);
          blobCache.add(blobInfo);
          cb(blobInfo.blobUri(), { title: file.name });
        });
        reader.readAsDataURL(file);
      });

      input.click();
    },
    content_style: 'body{font-family:Helvetica,Arial,sans-serif; font-size:16px}'
  });

</script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</body>
</html>

