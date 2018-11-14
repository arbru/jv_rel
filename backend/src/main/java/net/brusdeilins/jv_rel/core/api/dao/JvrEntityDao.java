package net.brusdeilins.jv_rel.core.api.dao;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class JvrEntityDao {
    @Id
    private String id;
    @ManyToOne(optional = false)
    private JvrEntityTypeDao type;
    @Column(nullable = false, updatable = false)
    private String name;
    @Column(length = 4096, nullable = false, updatable = false, unique = true)
    private String qualifiedName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JvrEntityTypeDao getType() {
        return type;
    }

    public void setType(JvrEntityTypeDao type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
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
        JvrEntityDao other = (JvrEntityDao) obj;
        return Objects.equals(id, other.id);
    }
}
