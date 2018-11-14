package net.brusdeilins.jv_rel.query.api.dao;

import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class JvrRelationFilterDao {
    private Integer maxHops;
    private JvrDirection direction;
    @EmbeddedId
    private JvrRelationFilterId id;

    public Integer getMaxHops() {
        return maxHops;
    }

    public void setMaxHops(Integer maxHops) {
        this.maxHops = maxHops;
    }

    public JvrDirection getDirection() {
        return direction;
    }

    public void setDirection(JvrDirection direction) {
        this.direction = direction;
    }

    public JvrRelationFilterId getId() {
        return id;
    }

    public void setId(JvrRelationFilterId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RelationFilter [maxHops=" + maxHops + ", direction=" + direction + ", id=" + id + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(direction, id, maxHops);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JvrRelationFilterDao other = (JvrRelationFilterDao) obj;
        return direction == other.direction && Objects.equals(id, other.id) && Objects.equals(maxHops, other.maxHops);
    }

}
