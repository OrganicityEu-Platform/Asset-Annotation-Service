package eu.organicity.annotation.common.dto;

/**
 * Created by etheodor on 31/05/2016.
 */

public class CreateAnnotationDTO {
    private String assetUrn;
    private String tagUrn;
    private String user;
    private String application;
    private Double numericValue;
    private String textValue;


    public CreateAnnotationDTO() {
    }

    public String getAssetUrn() {
        return assetUrn;
    }

    public void setAssetUrn(String assetUrn) {
        this.assetUrn = assetUrn;
    }

    public String getTagUrn() {
        return tagUrn;
    }

    public void setTagUrn(String tagUrn) {
        this.tagUrn = tagUrn;
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
        return "Annotation{" +
                ", assetUrn='" + assetUrn + '\'' +
                ", tagUrn='" + tagUrn + '\'' +
                ", user='" + user + '\'' +
                ", application='" + application + '\'' +
                ", numericValue=" + numericValue +
                ", textValue='" + textValue + '\'' +
                '}';
    }
}
