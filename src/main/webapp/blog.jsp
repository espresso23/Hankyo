<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@ page import="dao.PostDAO" %>
<%@ page import="model.Post" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.UserDAO" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    User user = (User) session.getAttribute("user");
    PostDAO postDAO = new PostDAO();
    UserDAO userDAO = new UserDAO();
%>

<%--<script>--%>
<%--    // JavaScript function to toggle visibility of comment box--%>
<%--    function toggleCommentBox(postID) {--%>
<%--        var commentBox = document.getElementById("commentBox-" + postID);--%>
<%--        if (commentBox.style.display === "none" || commentBox.style.display === "") {--%>
<%--            commentBox.style.display = "block";  // Show the comment box--%>
<%--        } else {--%>
<%--            commentBox.style.display = "none";  // Hide the comment box--%>
<%--        }--%>
<%--    }--%>
<%--</script>--%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Hankyo Forum</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="asset/font/themify-icons/themify-icons.css" />
    <link rel="stylesheet" href="asset/css/style.css" />
    <link rel="stylesheet" href="asset/css/blog.css" />
    <link rel="stylesheet" href="asset/css/modal.css" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="./js/blog.js"></script>
</head>
<body>
<!-- HEADER -->
<c:import url="header.jsp"/>

<!-- PAGE INFO -->

<div id="page-info">
    <div class="page-info-more">
        <c:choose>
                    <c:when test="${user != null}">
                        <button id="openModalBtn" class="submit-button">Create post</button>
                    </c:when>
                    <c:otherwise>
                        <button class="openModalBtn" onclick="alert('Please log in to create a post.');">Create post</button>
                    </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- SLIDER IMG -->

<!-- CONTENT -->
<div id="content">
    <c:choose>
        <c:when test="${not empty postList}">
            <div class="post-row content-load">
                <c:forEach var="post" items="${postList}">
                    <a class="card card-post content" href="postDetails?postID=${post.getPostID()}" style="width: 100%">
                        <div class="card-post-author">
                            <img src="${post.getAvtUserImg() != null ? post.getAvtUserImg() : userDAO.getAvatarByUserId(post.getUserID())}"
                                 onerror="this.onerror=null;this.src='https://i.pinimg.com/564x/09/a9/2c/09a92c1cbe440f31d1818e4fe0bcf23a.jpg';"
                                 alt="Author Avatar" class="author-post-avatar" />
                            <p class="author_name author-post-name">${post.getUserFullName()}</p>
                        </div>
                        <div class="card-body">
                            <h5 class="card-title card-title-post">${post.getHeading()}</h5>
                            <p class="">${post.getContent()}</p>
                        </div>
                    </a>
                    <button class="btn btn-outline-primary" onclick="toggleCommentBox(${post.getPostID()})">Bình luận</button>
                    <!-- Comment Box for each post -->
                    <div class="comment-box" id="commentBox-${post.getPostID()}" style="display: none;">
                        <textarea id="commentInput-${post.getPostID()}" rows="4" placeholder="Nhập bình luận của bạn..."></textarea><br><br>
                        <button class="btn btn-primary" id="submitComment-${post.getPostID()}">Gửi Bình luận</button>
                    </div>

                    <!-- Button to show/hide comment box -->


                </c:forEach>
            </div>
            <a href="#" id="loadMore">Load More</a>
        </c:when>
        <c:otherwise>
            <div class="no-posts-message">
                <h3>No posts available yet</h3>
                <p>Be the first to create a post!</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>


<c:import url="footer.jsp"/>

<!-- Modal Structure -->
<div id="myModal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <form action="postDetails" method="POST" enctype="multipart/form-data">
            <h2>Add Blog Post</h2>
            <label for="imgPost">Picture Cover:</label>
            <input id="imgPost" name="imgPost" type="file" class="input-field" required/><br>
            <label for="title">Heading:</label>
            <input name="title" id="title" type="text" required class="input-field"/><br>
            <label for="description">Description:</label>
            <input type="hidden" id="description" name="userID" value="${user.getUserID()}"/>
            <textarea id="default" name="description"></textarea><br>
            <input type="hidden" name="action" value="addPost">
            <button type="submit" class="submit-button">Submit</button>
        </form>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        let loadMoreButton = document.getElementById("loadMore");
        if (loadMoreButton) {
            let posts = document.querySelectorAll(".post-row .content");
            let currentPosts = 6;

            // Hide posts beyond the initial display count
            for (let i = currentPosts; i < posts.length; i++) {
                posts[i].style.display = "none";
            }

            // Hide the load more button if there aren't enough posts
            if (posts.length <= currentPosts) {
                loadMoreButton.style.display = "none";
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
        }
    });

    var modal = document.getElementById("myModal");
    var btn = document.getElementById("openModalBtn");
    var span = document.getElementsByClassName("close")[0];

    if (btn) {
        btn.onclick = function () {
            modal.style.display = "block";
        }
    }

    if (span) {
        span.onclick = function () {
            modal.style.display = "none";
        }
    }

    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
</script>

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
</body>
</html>