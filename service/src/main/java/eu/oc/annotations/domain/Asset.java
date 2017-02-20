//package eu.oc.annotations.domain;
//
//
//import org.neo4j.ogm.annotation.Relationship;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@Entity
//@Table(name = "Asset")
//public class Asset {
//
//    @Id
//    @GeneratedValue
//    private Long id;
//
//    @NotNull
//    private String urn;
//
//    @Relationship(type = "TAGGING")
//    private List<Tagging> taggings;
//
//    public Asset() {
//    }
//
//    public String getUrn() {
//        return urn;
//    }
//
//    public void setUrn(String urn) {
//        this.urn = urn;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public List<Tagging> getTaggings() {
//        if (taggings == null) taggings = new ArrayList<>();
//        return taggings;
//    }
//
//    public void setTagging(List<Tagging> tagging) {
//        this.taggings = tagging;
//    }
//
//    @Override
//    public String toString() {
//        return "Asset{" +
//                "id=" + id +
//                ", urn='" + urn + '\'' +
//                ", taggings=" + ((taggings == null) ? "NULL" : Arrays.toString(taggings.toArray())) +
//                '}';
//    }
//}
