package net.brusdeilins.jv_rel.core.impl.dao;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class JvrRelationTypeDao {
    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        JvrRelationTypeDao other = (JvrRelationTypeDao) obj;
        return Objects.equals(id, other.id);
    }

}
