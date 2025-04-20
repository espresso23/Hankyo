package model;

public class Documentary {
    private int docID;
    private String title;
    private String author;
    private String source;
    private String docContent;
    private String type;
    private String audioPath;
    private String thumbnail;

    public Documentary(int docID, String title, String author, String source, String docContent, String type, String audioPath, String thumbnail) {
        this.docID = docID;
        this.title = title;
        this.author = author;
        this.source = source;
        this.docContent = docContent;
        this.type = type;
        this.audioPath = audioPath;
        this.thumbnail = thumbnail;
    }

    public int getDocID() {
        return docID;
    }

    public void setDocID(int docID) {
        this.docID = docID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDocContent() {
        return docContent;
    }

    public void setDocContent(String docContent) {
        this.docContent = docContent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
