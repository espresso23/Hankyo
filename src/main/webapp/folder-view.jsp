<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Folder Viewer</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<h2>📁 Danh sách thư mục</h2>
<ul id="folder-list"></ul>

<h2>📄 Danh sách file</h2>
<ul id="file-list"></ul>

<h2>📂 Xem nội dung file</h2>
<iframe id="file-frame" style="width: 100%; height: 600px; border: 1px solid #ccc;"></iframe>

<form action="upload" method="post" enctype="multipart/form-data">
    <input type="file" name="file" />
    <button type="submit">Upload</button>
</form>

<script>
    const folderID = new URLSearchParams(window.location.search).get("FolderID") || "";

    $.get("folder", { FolderID: folderID }, function(data) {
        const folders = JSON.parse(data.listFolder);
        const files = JSON.parse(data.listFile);

        folders.forEach(folder => {
            $('#folder-list').append(`<li><a href="folder-view.jsp?FolderID=${folder.folderID}">${folder.name}</a></li>`);
        });

        files.forEach(file => {
            $('#file-list').append(`<li><a href="#" onclick="previewFile(${file.fileID})">${file.name}</a></li>`);
        });
    });
    function previewFile(fileID) {
        const url = 'file?FileID=' + fileID;
        $('#file-frame').attr('src', url);
    }

</script>
</body>
</html>
