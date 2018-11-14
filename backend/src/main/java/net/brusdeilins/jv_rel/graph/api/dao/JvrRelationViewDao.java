package net.brusdeilins.jv_rel.graph.api.dao;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class JvrRelationViewDao {
    @Id
    private JvrRelationViewId id;
    private int x;
    private int y;

    public JvrRelationViewId getId() {
        return id;
    }

    public void setId(JvrRelationViewId id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
        JvrRelationViewDao other = (JvrRelationViewDao) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "JvrRelationViewDao [id=" + id + ", x=" + x + ", y=" + y + "]";
    }

}
