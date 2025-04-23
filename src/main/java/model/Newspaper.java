package model;

import java.io.Serializable;

public class Newspaper implements Serializable {
    private static final long serialVersionUID = 1L;
    int newspaperID;
    String title;
    String content;
    String pictureFilePath;

    public Newspaper() {
    }

    public Newspaper(int newspaperID, String title, String content, String pictureFilePath) {
        this.newspaperID = newspaperID;
        this.title = title;
        this.content = content;
        this.pictureFilePath = pictureFilePath;
    }

    public int getNewspaperID() {
        return newspaperID;
    }

    public void setNewspaperID(int newspaperID) {
        this.newspaperID = newspaperID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPictureFilePath() {
        return pictureFilePath;
    }

    public void setPictureFilePath(String pictureFilePath) {
        this.pictureFilePath = pictureFilePath;
    }

    @Override
    public String toString() {
        return "Newspaper{" +
                "newspaperID=" + newspaperID +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", pictureFilePath='" + pictureFilePath + '\'' +
                '}';
    }
}
