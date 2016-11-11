package eu.oc.annotations.domain.dto;


public class AnnotationDTO {
    private long annotationId;
    private String assetUrn;
    private String tagUrn;
    private String datetime;
    private String user;
    private String application;
    private double numericValue;
    private String textValue;

    public AnnotationDTO() {
    }

    public AnnotationDTO(Long annotationId, String assetUrn, String tagUrn, String datetime, String user,
                         String application, Double numericValue, String textValue) {
        this.annotationId = annotationId;
        this.assetUrn = assetUrn;
        this.tagUrn = tagUrn;
        this.datetime = datetime;
        this.user = user;
        this.application = application;
        this.numericValue = numericValue;
        this.textValue = textValue;
    }

    public long getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(long annotationId) {
        this.annotationId = annotationId;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
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

    public double getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(double numericValue) {
        this.numericValue = numericValue;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnotationDTO that = (AnnotationDTO) o;

        return annotationId == that.annotationId;

    }

    @Override
    public int hashCode() {
        return (int) (annotationId ^ (annotationId >>> 32));
    }
}