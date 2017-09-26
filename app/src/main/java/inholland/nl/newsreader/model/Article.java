package inholland.nl.newsreader.model;

import java.util.ArrayList;
import java.util.Date;

public class Article {
    private int Id;
    private int Feed;
    private String Title;
    private String Summary;
    private Date PublishDate;
    private String Image;
    private String Url;
    private ArrayList<String> Related;
    private ArrayList<Category> Categories;
    private boolean IsLiked;

    public int getId() {
        return this.Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getFeed() {
        return this.Feed;
    }

    public void setFeed(int Feed) {
        this.Feed = Feed;
    }

    public String getTitle() {
        return this.Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getSummary() {
        return this.Summary;
    }

    public void setSummary(String Summary) {
        this.Summary = Summary;
    }

    public Date getPublishDate() {
        return this.PublishDate;
    }

    public void setPublishDate(Date PublishDate) {
        this.PublishDate = PublishDate;
    }

    public String getImage() {
        return this.Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getUrl() {
        return this.Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public ArrayList<String> getRelated() {
        return this.Related;
    }

    public void setRelated(ArrayList<String> Related) {
        this.Related = Related;
    }

    public ArrayList<Category> getCategories() {
        return this.Categories;
    }

    public void setCategories(ArrayList<Category> Categories) {
        this.Categories = Categories;
    }

    public boolean getIsLiked() {
        return this.IsLiked;
    }

    public void setIsLiked(boolean IsLiked) {
        this.IsLiked = IsLiked;
    }
}
