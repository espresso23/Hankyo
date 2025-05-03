package model;

public class Media {
    private int mediaId;
    private String fileName;
    private String url;
    private String type;
    private String uploadDate;

    public Media() {
    }

    public Media(int mediaId, String fileName, String url, String type, String uploadDate) {
        this.mediaId = mediaId;
        this.fileName = fileName;
        this.url = url;
        this.type = type;
        this.uploadDate = uploadDate;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
} 