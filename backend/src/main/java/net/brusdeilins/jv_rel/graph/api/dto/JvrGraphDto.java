package net.brusdeilins.jv_rel.graph.api.dto;

import java.util.Set;

public class JvrGraphDto {
    private String id;
    private String name;
    private Set<JvrEntityViewDto> entities;
    private Set<JvrRelationViewDto> relations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<JvrEntityViewDto> getEntities() {
        return entities;
    }

    public void setEntities(Set<JvrEntityViewDto> entities) {
        this.entities = entities;
    }

    public Set<JvrRelationViewDto> getRelations() {
        return relations;
    }

    public void setRelations(Set<JvrRelationViewDto> relations) {
        this.relations = relations;
    }
}
