package nl.arkenbout.geoffrey.newsreader.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Article implements Parcelable {
    // Parcelable creator implementation
    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
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

    public Article(int articleNumber) {
        Id = articleNumber;
        Title = "Title " + articleNumber;
        Summary = "";
        PublishDate = new Date();
        Image = "";
        Url = "www.google.com";
        Related = new ArrayList<>();
        Categories = new ArrayList<>();
        IsLiked = false;
    }

    // Parcelable implementation
    protected Article(Parcel in) {
        Id = in.readInt();
        Feed = in.readInt();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        try {
            PublishDate = df.parse(in.readString());
        } catch (ParseException e) {
            PublishDate = new Date();
        }
        Title = in.readString();
        Summary = in.readString();
        Image = in.readString();
        Url = in.readString();
        Related = in.createStringArrayList();
        IsLiked = in.readByte() != 0;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeInt(Feed);
        parcel.writeString(PublishDate.toString());
        parcel.writeString(Title);
        parcel.writeString(Summary);
        parcel.writeString(Image);
        parcel.writeString(Url);
        parcel.writeStringList(Related);
        parcel.writeByte((byte) (IsLiked ? 1 : 0));
    }
}
