package net.brusdeilins.jv_rel.core.api.dao;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class JvrRelationDao {
    @Id
    private String id;
    @ManyToOne(optional = false)
    private JvrRelationTypeDao type;
    @ManyToOne(optional = false)
    private JvrEntityDao source;
    @ManyToOne(optional = false)
    private JvrEntityDao target;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JvrRelationTypeDao getType() {
        return type;
    }

    public void setType(JvrRelationTypeDao type) {
        this.type = type;
    }

    public JvrEntityDao getSource() {
        return source;
    }

    public void setSource(JvrEntityDao source) {
        this.source = source;
    }

    public JvrEntityDao getTarget() {
        return target;
    }

    public void setTarget(JvrEntityDao target) {
        this.target = target;
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
        JvrRelationDao other = (JvrRelationDao) obj;
        return Objects.equals(id, other.id);
    }

}
