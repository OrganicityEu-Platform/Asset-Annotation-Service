package eu.organicity.annotation.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDTO {
    private Long id;
    private String urn;
    private String name;
    private String user;
    private Long created;
    private Long modified;
    
    public TagDTO() {
    }

    public TagDTO(Long id, String urn, String name) {
        this.id = id;
        this.urn = urn;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
    public Long getCreated() {
        return created;
    }
    
    public void setCreated(Long created) {
        this.created = created;
    }
    
    public Long getModified() {
        return modified;
    }
    
    public void setModified(Long modified) {
        this.modified = modified;
    }
    
    @Override
    public String toString() {
        return "TagDTO{" +
                "id=" + id +
                ", urn='" + urn + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagDTO tagDTO = (TagDTO) o;

        return urn.equals(tagDTO.urn);

    }

    @Override
    public int hashCode() {
        return urn.hashCode();
    }
}