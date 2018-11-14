package net.brusdeilins.jv_rel.graph.api.dao;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import net.brusdeilins.jv_rel.core.api.dao.JvrRelationDao;

@Embeddable
public class JvrRelationViewId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graph_id")
    private JvrGraphDao graph;
    @ManyToOne
    @JoinColumn(name = "relation_id")
    private JvrRelationDao relation;

    public JvrGraphDao getGraph() {
        return graph;
    }

    public void setGraph(JvrGraphDao graph) {
        this.graph = graph;
    }

    public JvrRelationDao getRelation() {
        return relation;
    }

    public void setRelation(JvrRelationDao entity) {
        this.relation = entity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(relation, graph);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JvrRelationViewId other = (JvrRelationViewId) obj;
        return Objects.equals(relation, other.relation) && Objects.equals(graph, other.graph);
    }

    @Override
    public String toString() {
        return "JvrRelationViewId [graph=" + graph.getId() + ", relation=" + relation.getId() + "]";
    }

}
