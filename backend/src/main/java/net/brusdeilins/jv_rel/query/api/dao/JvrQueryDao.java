package net.brusdeilins.jv_rel.query.api.dao;

import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class JvrQueryDao {
    @Id
    private String name;
    private int maxHops;
    @OneToMany(mappedBy = "id.query", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<JvrRelationFilterDao> relationFilter;

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

    public Set<JvrRelationFilterDao> getRelationFilter() {
        return relationFilter;
    }

    public void setRelationFilter(Set<JvrRelationFilterDao> relationFilter) {
        this.relationFilter = relationFilter;
    }

    @Override
    public String toString() {
        return "JvrQuery [name=" + name + ", maxHops=" + maxHops + ", relationFilter=" + relationFilter + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JvrQueryDao other = (JvrQueryDao) obj;
        return Objects.equals(name, other.name);
    }

}
