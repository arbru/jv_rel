package net.brusdeilins.jv_rel.rest.dto;

import java.util.Set;

public class JvrQueryDto {
    private String name;
    private Integer maxHops;
    private Set<JvrRelationFilterDto> relationFilter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxHops() {
        return maxHops;
    }

    public void setMaxHops(Integer maxHops) {
        this.maxHops = maxHops;
    }

    public Set<JvrRelationFilterDto> getRelationFilter() {
        return relationFilter;
    }

    public void setRelationFilter(Set<JvrRelationFilterDto> relationFilter) {
        this.relationFilter = relationFilter;
    }

    @Override
    public String toString() {
        return "QueryDto [name=" + name + ", maxHops=" + maxHops + ", relationFilter=" + relationFilter + "]";
    }

}
