package eu.oc.annotations.domain;

import org.neo4j.ogm.annotation.*;

/**
 * Created by etheodor on 31/05/2016.
 */

@RelationshipEntity(type = "TAGGING")
public class Tagging {

    @GraphId
    private Long taggingId;

    @StartNode
    private Asset asset;

    @EndNode
    private Tag tag;

    @Property
    private Long timestamp;

    @Property
    private String user;

    @Property
    private String application;

    @Property
    private Double numericValue;

    @Property
    private String textValue;


    public Tagging() {
    }

    public Long getTaggingId() {
        return taggingId;
    }

    public void setTaggingId(Long taggingId) {
        this.taggingId = taggingId;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public Double getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(Double numericValue) {
        this.numericValue = numericValue;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    @Override
    public String toString() {
        return "Tagging{" +
                "taggingId=" + taggingId +
                ", asset=" + ((asset!=null)?asset.getUrn():"NULL") +
                ", tag=" + ((tag!=null)?tag.getUrn():"NULL") +
                ", timestamp=" + timestamp +
                ", user='" + user + '\'' +
                ", application='" + application + '\'' +
                ", numericValue=" + numericValue +
                ", textValue='" + textValue + '\'' +
                '}';
    }


}
