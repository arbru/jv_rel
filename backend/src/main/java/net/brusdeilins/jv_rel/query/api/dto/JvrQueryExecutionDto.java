package net.brusdeilins.jv_rel.query.api.dto;

import java.util.Set;

public class JvrQueryExecutionDto {
    Set<String> entities;
    String queryName;

    public Set<String> getEntities() {
        return entities;
    }

    public void setEntities(Set<String> entities) {
        this.entities = entities;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    @Override
    public String toString() {
        return "QueryExecutionDto [entities=" + entities + ", queryName=" + queryName + "]";
    }
}
