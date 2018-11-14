package net.brusdeilins.jv_rel.query.api.dao;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import net.brusdeilins.jv_rel.core.api.dao.JvrRelationTypeDao;

@Embeddable
public class JvrRelationFilterId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "query_id")
    private JvrQueryDao query;
    @ManyToOne
    @JoinColumn(name = "relation_type")
    private JvrRelationTypeDao relationType;

    public JvrQueryDao getQuery() {
        return query;
    }

    public void setQuery(JvrQueryDao query) {
        this.query = query;
    }

    public JvrRelationTypeDao getRelationType() {
        return relationType;
    }

    public void setRelationType(JvrRelationTypeDao relationType) {
        this.relationType = relationType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, relationType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JvrRelationFilterId other = (JvrRelationFilterId) obj;
        return Objects.equals(query, other.query) && Objects.equals(relationType, other.relationType);
    }
}
