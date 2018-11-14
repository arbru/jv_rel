package net.brusdeilins.jv_rel.graph.api.dao;

import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class JvrGraphDao {
    @Id
    private String id;
    private String name;
    @OneToMany(mappedBy = "id.graph", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<JvrEntityViewDao> entities;
    @OneToMany(mappedBy = "id.graph", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<JvrRelationViewDao> relations;

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

    public Set<JvrEntityViewDao> getEntities() {
        return entities;
    }

    public void setEntities(Set<JvrEntityViewDao> entities) {
        this.entities = entities;
    }

    public Set<JvrRelationViewDao> getRelations() {
        return relations;
    }

    public void setRelations(Set<JvrRelationViewDao> relations) {
        this.relations = relations;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JvrGraphDao other = (JvrGraphDao) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "JvrGraphDao [id=" + id + ", name=" + name + ", entities=" + entities + ", relations=" + relations + "]";
    }

}
