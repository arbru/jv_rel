package net.brusdeilins.jv_rel.graph.api.dao;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import net.brusdeilins.jv_rel.core.api.dao.JvrEntityDao;

@Embeddable
public class JvrEntityViewId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graph_id")
    private JvrGraphDao graph;
    @ManyToOne
    @JoinColumn(name = "entity_id")
    private JvrEntityDao entity;

    public JvrGraphDao getGraph() {
        return graph;
    }

    public void setGraph(JvrGraphDao graph) {
        this.graph = graph;
    }

    public JvrEntityDao getEntity() {
        return entity;
    }

    public void setEntity(JvrEntityDao entity) {
        this.entity = entity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, graph);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JvrEntityViewId other = (JvrEntityViewId) obj;
        return Objects.equals(entity, other.entity) && Objects.equals(graph, other.graph);
    }

    @Override
    public String toString() {
        return "JvrEntityViewId [graph=" + graph.getId() + ", entity=" + entity.getId() + "]";
    }

}
