package eu.organicity.annotation.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by etheodor on 31/05/2016.
 */

@Entity
@Table(name = "annotation")
public class Tagging {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String urn;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tagId")
    private Tag tag;
    
    private Long created= System.currentTimeMillis();
    
    private Long lastModified;
    
    private String user;
    
    private String application;
    
    private Double numericValue;
    
    private String textValue;
    
    
    public Tagging() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUrn() {
        return urn;
    }
    
    public void setUrn(String urn) {
        this.urn = urn;
    }
    
    public Tag getTag() {
        return tag;
    }
    
    public void setTag(Tag tag) {
        this.tag = tag;
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
    
    public Long getCreated() {
        return created;
    }
    
    public void setCreated(Long created) {
        this.created = created;
    }
    
    public Long getLastModified() {
        return lastModified;
    }
    
    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }
    
    @Override
    public String toString() {
        return "Tagging{" + "id=" + id + ", urn='" + urn + '\'' + ", tag=" + tag + ", created=" + created + ", lastModified=" + lastModified + ", user='" + user + '\'' + ", application='" + application + '\'' + ", numericValue=" + numericValue + ", textValue='" + textValue + '\'' + '}';
    }
}
