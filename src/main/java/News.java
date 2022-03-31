import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class News {
    @SerializedName("id")
    private int id;
    @SerializedName("name_news")
    private String nameNews;
    @SerializedName("short_desc")
    private String shortDesc;
    @SerializedName("full_desc")
    private String fullDesc;
    @SerializedName("type_id")
    private int typeId;
    @SerializedName("source")
    private String source;
    @SerializedName("created_at")
    private int createdAt;
    @SerializedName("updated_at")
    private int updatedAt;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss");

    public News(int id, String nameNews, String shortDesc, String fullDesc, int typeId, String source, int createdAt, int updatedAt) {
        this.id = id;
        this.nameNews = nameNews;
        this.shortDesc = shortDesc;
        this.fullDesc = fullDesc;
        this.typeId = typeId;
        this.source = source;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public News() {}

    public void setNameNews(String nameNews) {
        this.nameNews = nameNews;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public void setFullDesc(String fullDesc) {
        this.fullDesc = fullDesc;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public String getNameNews() {
        return nameNews;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getFullDesc() {
        return fullDesc;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getSource() {
        return source;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public int getUpdatedAt() {
        return updatedAt;
    }

    public String getDateCreate() {
        return dateFormat.format(new Date((long) createdAt * 1000));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n#### Новость №").append(id).append(" дата: ").append(getDateCreate()).append(" тип: ").append(typeId).append("\n");
        sb.append(" ### ").append(nameNews).append(" ### ").append("\n");
        sb.append(" ### ").append(shortDesc).append(" ...").append("\n");
        sb.append(" ### Источник: ").append(source);
        return sb.toString();
    }
}
