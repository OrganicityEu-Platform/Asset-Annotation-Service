package eu.oc.annotations.domain.dto;


public class TagDto {
    private long id;
    private String urn;
    private String name;

    public TagDto(long id, String urn, String name) {
        this.id = id;
        this.urn = urn;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagDto tagDto = (TagDto) o;

        if (id != tagDto.id) return false;
        return urn.equals(tagDto.urn);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + urn.hashCode();
        return result;
    }
}