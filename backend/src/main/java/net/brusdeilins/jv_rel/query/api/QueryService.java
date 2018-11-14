package net.brusdeilins.jv_rel.query.api;

import java.util.Set;

import net.brusdeilins.jv_rel.core.api.dao.JvrEntityDao;
import net.brusdeilins.jv_rel.graph.api.dao.JvrGraphDao;
import net.brusdeilins.jv_rel.query.api.dao.JvrQueryDao;

public interface QueryService {
    JvrGraphDao process(Set<JvrEntityDao> startNodes, String queryId);

    Iterable<JvrQueryDao> getQueries();

    JvrQueryDao getQuery(String id);

    void saveQuery(JvrQueryDao query);
}
