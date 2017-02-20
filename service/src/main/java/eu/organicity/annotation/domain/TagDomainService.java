package eu.organicity.annotation.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tagDomainService")
public class TagDomainService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tagDomainId")
    private TagDomain tagDomain;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "serviceId")
    private Service service;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TagDomain getTagDomain() {
        return tagDomain;
    }

    public void setTagDomain(TagDomain tagDomain) {
        this.tagDomain = tagDomain;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
