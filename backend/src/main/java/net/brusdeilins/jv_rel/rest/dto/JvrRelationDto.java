package net.brusdeilins.jv_rel.rest.dto;

public class JvrRelationDto {
    private String id;
    private String type;
    private JvrEntityDto source;
    private JvrEntityDto target;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JvrEntityDto getSource() {
        return source;
    }

    public void setSource(JvrEntityDto source) {
        this.source = source;
    }

    public JvrEntityDto getTarget() {
        return target;
    }

    public void setTarget(JvrEntityDto target) {
        this.target = target;
    }
}
